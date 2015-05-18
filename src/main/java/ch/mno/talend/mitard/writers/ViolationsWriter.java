package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.*;
import ch.mno.talend.mitard.out.JsonFileViolations;
import ch.mno.talend.mitard.out.JsonViolationEnum;
import ch.mno.talend.mitard.readers.ProcessReader;
import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dutoitc on 13.05.2015.
 */
public class ViolationsWriter extends AbstractNodeWriter {


    public ViolationsWriter(Context context) {
        super(context);
    }

    public void write(TalendFiles talendFiles) throws IOException, ParserConfigurationException, SAXException {
        JsonViolations allViolations = new JsonViolations();


        for (TalendFile file : talendFiles.getProcesses()) {
            if (isBlacklisted(file.getName())) continue;
            System.out.println("Reading " + new File(file.getItemFilename()).getName());

            String properties = IOUtils.toString(new FileReader(file.getPropertiesFilename()));
            String item = IOUtils.toString(new FileReader(file.getItemFilename()));
            JsonFileViolations fileViolations = new JsonFileViolations(file.getPath(), file.getName(), file.getVersion());

            FileInputStream fis = new FileInputStream(file.getItemFilename());
            ProcessType process = ProcessReader.reader(fis);
            int nbInactive = 0;
            for (AbstractNodeType node : process.getNodeList()) {
                if (!node.isActive()) {
                    nbInactive++;
                    continue;
                }

                // REST_ENDPOINT should be with port 8040
                if (node instanceof TRestRequestType) {
                    if (!((TRestRequestType) node).getEndpointURI().contains(":8040")) {
                        fileViolations.addComponentViolation(node.getUniqueName(), JsonViolationEnum.SERVICE_PORT_MUST_BE_8040);
                    }
                }


                if (!node.isUseExistingConnection() && (node.getComponentName().equals("tMDMInput") || node.getComponentName().equals("tOracleInput"))) {
                    fileViolations.addComponentViolation(node.getUniqueName(), JsonViolationEnum.COMPONENT_MUST_USE_EXISTING_CONNECTION);
                }
                if (node.getComponentName().equals("tMDMCommit")) {
                    if (((TMDMCommitType) node).isClose()) {
                        fileViolations.addComponentViolation(node.getUniqueName(), JsonViolationEnum.COMPONENT_MUST_NOT_CLOSE_CONNECTION);
                    }
                }
                if (node.getComponentName().equals("tOracleCommit")) {
                    if (((TOracleCommitType) node).isClose()) {
                        fileViolations.addComponentViolation(node.getUniqueName(), JsonViolationEnum.COMPONENT_MUST_NOT_CLOSE_CONNECTION);
                    }
                }
                if (node.getComponentName().equals("tJava")) {
                    TJavaType tJava = (TJavaType)node;
                    if (tJava.getCode().contains("System.out") || tJava.getCode().contains("System.err")){
                        fileViolations.addComponentViolation(node.getUniqueName(), JsonViolationEnum.AVOID_SYSTEM_OUT);
                    }
                }
                if (node.getComponentName().equals("tJavaRow")) {
                    TJavaRowType tJavaRow = (TJavaRowType)node;
                    if (tJavaRow.getCode().contains("System.out") || tJavaRow.getCode().contains("System.err")){
                        fileViolations.addComponentViolation(node.getUniqueName(), JsonViolationEnum.AVOID_SYSTEM_OUT);
                    }
                }
                if (node.getComponentName().equals("tJavaFlex")) {
                    TJavaFlexType tJava = (TJavaFlexType)node;
                    if (tJava.getCodeStart().contains("System.out") || tJava.getCodeStart().contains("System.err")
                            || tJava.getCodeMain().contains("System.out") || tJava.getCodeMain().contains("System.err")
                            || tJava.getCodeEnd().contains("System.out") || tJava.getCodeEnd().contains("System.err")){
                        fileViolations.addComponentViolation(node.getUniqueName(), JsonViolationEnum.AVOID_SYSTEM_OUT);
                    }
                }

            }

            // RATIO_INACTIVE_MUST_BE_MAX_30_PERCENT
            if (nbInactive > process.getNodeList().size() / 3) {
                fileViolations.addGeneralViolation(JsonViolationEnum.RATIO_INACTIVE_MUST_BE_MAX_30_PERCENT);
            }


            /*if (process.getPurpose()==null || node.getPurpose().isEmpty()) {
                fileViolations.addGeneralViolation(JsonViolationEnum.MISSING_DOCUMENTATION_PURPOSE);
            }


            if (node.getDescription()==null || node.getDescription().isEmpty()) {
                fileViolations.addGeneralViolation(JsonViolationEnum.MISSING_DOCUMENTATION_DESCRIPTION);
            }*/



            // Missing test

            // Missing documentation

            if (fileViolations.hasViolations()) {
                allViolations.add(fileViolations);
            }

        }

        // TODO: routes

        // TODO: services

        // Count
        int nbGeneralViolations = 0;
        int nbComponentViolations = 0;
        for (JsonFileViolations fileViolations : allViolations.getFileViolationses()) {
            nbGeneralViolations += fileViolations.getGeneralViolations().size();
            for (List<JsonViolationEnum> componentViolations : fileViolations.getComponentViolations().values()) {
                nbComponentViolations += componentViolations.size();
            }
        }
        allViolations.setNbGeneralViolations(nbGeneralViolations);
        allViolations.setNbComponentViolations(nbComponentViolations);

        writeJson("violations.json", allViolations);
    }

    class JsonViolations {
        private List<JsonFileViolations> fileViolationses = new ArrayList<>();

        private List<JsonViolationDefinition> jsonViolationsDefinition = new ArrayList<>();
        private int nbGeneralViolations;
        private int nbComponentViolations;

        public JsonViolations() {
            for (JsonViolationEnum e : JsonViolationEnum.values()) {
                jsonViolationsDefinition.add(new JsonViolationDefinition(e.name(), e.getDescription(), e.getExplanations()));
            }
        }

        public void add(JsonFileViolations fileViolations) {
            fileViolationses.add(fileViolations);
        }

        public List<JsonFileViolations> getFileViolationses() {
            return fileViolationses;
        }

        public void setFileViolationses(List<JsonFileViolations> fileViolationses) {
            this.fileViolationses = fileViolationses;
        }

        public List<JsonViolationDefinition> getJsonViolationsDefinition() {
            return jsonViolationsDefinition;
        }

        public void setJsonViolationsDefinition(List<JsonViolationDefinition> jsonViolationsDefinition) {
            this.jsonViolationsDefinition = jsonViolationsDefinition;
        }

        public void setNbGeneralViolations(int nbGeneralViolations) {
            this.nbGeneralViolations = nbGeneralViolations;
        }

        public int getNbGeneralViolations() {
            return nbGeneralViolations;
        }

        public void setNbComponentViolations(int nbComponentViolations) {
            this.nbComponentViolations = nbComponentViolations;
        }

        public int getNbComponentViolations() {
            return nbComponentViolations;
        }
    }

    class JsonViolationDefinition {

        private String name;
        private String description;
        private String explanations;

        public JsonViolationDefinition(String name, String description, String explanations) {
            this.name = name;
            this.description = description;
            this.explanations = explanations;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getExplanations() {
            return explanations;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setExplanations(String explanations) {
            this.explanations = explanations;
        }
    }

}