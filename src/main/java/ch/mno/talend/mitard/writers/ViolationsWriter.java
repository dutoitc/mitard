package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.*;
import ch.mno.talend.mitard.out.JsonFileViolations;
import ch.mno.talend.mitard.out.JsonViolationEnum;
import ch.mno.talend.mitard.readers.ProcessReader;
import ch.mno.talend.mitard.readers.PropertiesReader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dutoitc on 13.05.2015.
 */
public class ViolationsWriter extends AbstractNodeWriter {

    public static Logger LOG = LoggerFactory.getLogger(ViolationsWriter.class);

    public ViolationsWriter(Context context) {
        super(context);
    }

    public void write(TalendFiles talendFiles) {
        try {
            JsonViolations allViolations = new JsonViolations();

            writeUnstableFiled(talendFiles, allViolations);

            for (TalendFile file : talendFiles.getProcesses()) {
                try {
                    if (isBlacklisted(file.getName()) || isBlacklisted(file.getPath())) continue;
                    addProcessViolations(allViolations, file);
                } catch (Exception e) {
                    LOG.error("Error writing violations(2) for " + file.getName() + " (ignoring file): " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // TODO: routes

            // TODO: services


            // Workflow
            for (TalendFile file : talendFiles.getMDMWorkflowProc()) {
                try {
                    if (isBlacklisted(file.getName()) || isBlacklisted(file.getPath())) continue;
                    addMDMViolations(allViolations, file);
                } catch (Exception e) {
                    LOG.error("Error writing violations for proc " + file.getName() + " (ignoring file): " + e.getMessage());
                }
            }


            // Count
            countViolations(allViolations);

            writeJson("violations.json", allViolations);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Count fileViolations.generalViolations and fileViolations.componentViolations
     * @param allViolations
     */
    private void countViolations(JsonViolations allViolations) {
        int nbGeneralViolations = 0;
        int nbComponentViolations = 0;
        for (JsonFileViolations fileViolations : allViolations.getFileViolationses()) {
            try {
                nbGeneralViolations += fileViolations.getGeneralViolations().size();
                for (List<JsonViolationEnum> componentViolations : fileViolations.getComponentViolations().values()) {
                    nbComponentViolations += componentViolations.size();
                }
            } catch (Exception e) {
                LOG.error("Error writing violations(4) for " + fileViolations.getName() + " (ignoring file): " + e.getMessage());
            }
        }
        allViolations.setNbGeneralViolations(nbGeneralViolations);
        allViolations.setNbComponentViolations(nbComponentViolations);
    }

    private void addMDMViolations(JsonViolations allViolations, TalendFile file) throws IOException {
        JsonFileViolations fileViolations = new JsonFileViolations(file.getPath(), file.getName(), file.getVersion());

        String procFilename = file.getProcFilename();
        if (!new File(procFilename).exists()) {
            procFilename = file.getProcFilenameTalend6();
        }

        String data = IOUtils.toString(new FileInputStream(procFilename));
        if (data.contains("synchronous=\"false\"")) {
            fileViolations.addGeneralViolation(JsonViolationEnum.BPM_SHOULD_NOT_BE_ASYNCHRONOUS);
        }

        if (fileViolations.hasViolations()) {
            allViolations.add(fileViolations);
        }
    }

    private void writeUnstableFiled(TalendFiles talendFiles, JsonViolations allViolations) {
        for (TalendFile file : talendFiles.getUnstableFiles()) {
            try {
                JsonFileViolations fileViolations = new JsonFileViolations(file.getPath(), file.getName(), file.getVersion());
                fileViolations.addGeneralViolation(JsonViolationEnum.UNSTABLE_FILES);
                allViolations.add(fileViolations);
            } catch (Exception e) {
                LOG.error("Error writing violations for " + file.getName() + " (ignoring file): " + e.getMessage());
            }
        }
    }

    private void addProcessViolations(JsonViolations allViolations, TalendFile file) throws ParserConfigurationException, SAXException, IOException {
        LOG.debug("AddProcessViolations " + new File(file.getItemFilename()).getName());
        //            String properties = IOUtils.toString(new FileReader(file.getPropertiesFilename()));
//            String item = IOUtils.toString(new FileReader(file.getItemFilename()));
        JsonFileViolations fileViolations = new JsonFileViolations(file.getPath(), file.getName(), file.getVersion());


        FileInputStream fis = new FileInputStream(file.getItemFilename());
        ProcessType process = ProcessReader.read(fis);
        int nbInactive = 0;
        for (AbstractNodeType node : process.getNodeList()) {
            if (!node.isActive()) {
                nbInactive++;
                continue;
            }
            checkSERVICE_PORT_MUST_BE_8040(fileViolations, node);
            checkCOMPONENT_MUST_USE_EXISTING_CONNECTION(fileViolations, node);
            checkCOMPONENT_MUST_NOT_CLOSE_CONNECTION(fileViolations, node);
            checkAVOID_SYSTEM_OUT(fileViolations, node);
            checkTLOGCATCHER_MUST_NOT_CHAIN_TDIE(fileViolations, node, process);
            checkFIRECREATEEVENT_MUST_BE_SET(fileViolations, node, process);
            checkTRUNJOB_MUST_PROPAGATE_CHILD_RESULT(fileViolations, node);
        }
        checkSERVICE_MUST_NOT_SET_DB_CONNECTION_IN_PREJOB(fileViolations, process);
        checkMDM_MUST_AUTOCOMMIT_OR_MANAGE_CONNECTION(fileViolations, process);
        checkProcessMustNotUseRollbackWithAutocommit(fileViolations, process);
        checkNonAutocommitConnectionMustHaveCommitOrRollback(fileViolations, process);
        checkAVOID_MDMCONNECTION_IN_SERVICE_PREJOB(file, fileViolations, process);
        checkAVOID_DBCONNECTION_IN_SERVICE_PREJOB(file, fileViolations, process);
        checkMDMCONNECTION_MUST_BE_CLOSED(fileViolations, process);
        checkDBCONNECTION_MUST_BE_CLOSED(fileViolations, process);

        // RATIO_INACTIVE_MUST_BE_MAX_30_PERCENT
        if (nbInactive > process.getNodeList().size() / 3) {
            fileViolations.addGeneralViolation(JsonViolationEnum.RATIO_INACTIVE_MUST_BE_MAX_30_PERCENT);
        }

        // TOO_MUCH_COMPONENTS
        // FAR_TOO_MUCH_COMPONENTS
        if (process.getNodeList().size() > 100) {
            fileViolations.addGeneralViolation(JsonViolationEnum.FAR_TOO_MUCH_COMPONENTS);
        } else if (process.getNodeList().size() > 50) {
            fileViolations.addGeneralViolation(JsonViolationEnum.TOO_MUCH_COMPONENTS);
        }


        // Properties
        fis = new FileInputStream(file.getPropertiesFilename());
        PropertiesType properties = PropertiesReader.reader(fis);


        // MISSING_DOCUMENTATION_PURPOSE
        if (StringUtils.isEmpty(properties.getPurpose())) {
            fileViolations.addGeneralViolation(JsonViolationEnum.MISSING_DOCUMENTATION_PURPOSE);
        }

        // MISSING_DOCUMENTATION_DESCRIPTION
        if (StringUtils.isEmpty(properties.getDescription())) {
            fileViolations.addGeneralViolation(JsonViolationEnum.MISSING_DOCUMENTATION_DESCRIPTION);
        }


        // Missing test

        if (fileViolations.hasViolations()) {
            allViolations.add(fileViolations);
        }
    }

    private void checkSERVICE_MUST_NOT_SET_DB_CONNECTION_IN_PREJOB(JsonFileViolations fileViolations, ProcessType process) {
        boolean foundListener = false;
        for (AbstractNodeType node : process.getNodeList()) {
            if (!node.isActive()) continue;
            if ((node.getComponentName().equals("tESBProviderRequest") || node.getComponentName().equals("tRESTRequest")) && node.isActive()) {
                foundListener = true;
                break;
            }
        }
        if (!foundListener) return;
        for (Map.Entry<String, List<String>> entry : process.getConnections().entrySet()) {
            if (entry.getKey().startsWith("tPrejob")) {
                for (String target : entry.getValue()) {
                    if (target.startsWith("tOracleConnection") || target.startsWith("tMDMConnection")) {
                        fileViolations.addGeneralViolation(JsonViolationEnum.SERVICE_MUST_NOT_SET_DB_CONNECTION_IN_PREJOB);
                        return;
                    }
                }
            }
        }
    }


    private void checkFIRECREATEEVENT_MUST_BE_SET(JsonFileViolations fileViolations, AbstractNodeType node, ProcessType process) {
        if (node.getComponentName().equals("tMDMOutput")) {
            TNodeType nodeType = (TNodeType) node;
            if ("false".equals(nodeType.getValue("WITHREPORT"))) {
                fileViolations.addComponentViolation(node.getUniqueName(), JsonViolationEnum.FIRECREATEEVENT_MUST_BE_SET);
            }
        }
    }


    private void checkTLOGCATCHER_MUST_NOT_CHAIN_TDIE(JsonFileViolations fileViolations, AbstractNodeType node, ProcessType process) {
        if (node.getComponentName().equals("tLogCatcher")) {
            String source = node.getUniqueName();
            if (isConnectedToTDie(process, source)) {
                fileViolations.addComponentViolation(node.getUniqueName(), JsonViolationEnum.LOGCATCHER_MUST_NOT_CHAIN_TDIE);
            }

            // TODO: only check depending on theses values ?
//            <elementParameter field="CHECK" name="CATCH_JAVA_EXCEPTION" value="true"/>
//            <elementParameter field="CHECK" name="CATCH_TDIE" value="true"/>
//            <elementParameter field="CHECK" name="CATCH_TWARN" value="true"/>

//            <connection connectorName="FLOW" label="row5" lineStyle="0" metaname="tLogCatcher_1" offsetLabelX="0" offsetLabelY="0" source="tLogCatcher_1" target="RCEntLog4jLogger_1">
//            <connection connectorName="FLOW" label="row6" lineStyle="0" metaname="RCEntLog4jLogger_1" offsetLabelX="0" offsetLabelY="0" source="RCEntLog4jLogger_1" target="tDie_1">
//            <node componentName="tDie" componentVersion="0.101" offsetLabelX="0" offsetLabelY="0" posX="1152" posY="0">
        }
    }

    private boolean isConnectedToTDie(ProcessType process, String source) {
        if (source == null) return false;
        if (source.startsWith("tDie")) return true;
        List<String> connections = process.getConnections(source);
        if (connections != null) {
            for (String target : connections) {
                if (isConnectedToTDie(process, target)) return true;
            }
        }
        return false;
    }

    private void checkSERVICE_PORT_MUST_BE_8040(JsonFileViolations fileViolations, AbstractNodeType node) {
        if (node instanceof TRestRequestType) {
            if (!((TRestRequestType) node).getEndpointURI().contains(":8040")) {
                fileViolations.addComponentViolation(node.getUniqueName(), JsonViolationEnum.SERVICE_PORT_MUST_BE_8040);
            }
        }
    }

    private void checkTRUNJOB_MUST_PROPAGATE_CHILD_RESULT(JsonFileViolations fileViolations, AbstractNodeType node) {
        if (node instanceof TRunJobType) {
            if (!((TRunJobType) node).getPropagateChildResult().equals("true")) {
                fileViolations.addComponentViolation(node.getUniqueName(), JsonViolationEnum.TRUNJOB_MUST_PROPAGATE_CHILD_RESULT);
            }
        }
    }


    private void checkCOMPONENT_MUST_USE_EXISTING_CONNECTION(JsonFileViolations fileViolations, AbstractNodeType node) {
        if (!node.isUseExistingConnection() && (node.getComponentName().equals("tMDMInput") || node.getComponentName().equals("tOracleInput"))) {
            fileViolations.addComponentViolation(node.getUniqueName(), JsonViolationEnum.COMPONENT_MUST_USE_EXISTING_CONNECTION);
        }
    }

    private void checkCOMPONENT_MUST_NOT_CLOSE_CONNECTION(JsonFileViolations fileViolations, AbstractNodeType node) {
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
    }

    private void checkProcessMustNotUseRollbackWithAutocommit(JsonFileViolations fileViolations, ProcessType process) {
        process.getNodeList().stream()
                .filter(n->n.isActive() && n instanceof TOracleRollbackType)
                .map(n->(TOracleRollbackType)n)
                .forEach(rollback-> {

                    process.getNodeList().stream()
                            .filter(n -> n.isActive() && n instanceof TOracleConnectionType)
                            .map(n->(TOracleConnectionType)n)
                            .filter(n->n.getUniqueName().equals(rollback.getConnection()) && n.isAutoCommit())
                            .forEach(d->fileViolations.addComponentViolation(rollback.getUniqueName(), JsonViolationEnum.ROLLBACK_MUST_NOT_BE_DEFINED_ON_AUTOCOMMIT_CONNECTION));

                });
    }

    private void checkNonAutocommitConnectionMustHaveCommitOrRollback(JsonFileViolations fileViolations, ProcessType process) {
        process.getNodeList().stream()
                .filter(n->n.isActive() && n instanceof TOracleConnectionType)
                .map(n->(TOracleConnectionType)n)
                .filter(n->!n.isAutoCommit())
                .forEach(conn-> {

                    List<TOracleCommitType> lstCommits = process.getNodeList().stream()
                            .filter(n -> n.isActive() && n instanceof TOracleCommitType)
                            .map(n -> (TOracleCommitType) n)
                            .filter(n -> n.getConnection().equals(conn.getUniqueName()))
                            .collect(Collectors.toList());

                    List<TOracleRollbackType> lstRollbacks = process.getNodeList().stream()
                            .filter(n -> n.isActive() && n instanceof TOracleRollbackType)
                            .map(n -> (TOracleRollbackType) n)
                            .filter(n -> n.getConnection().equals(conn.getUniqueName()))
                            .collect(Collectors.toList());
                    if (lstCommits.isEmpty() && lstRollbacks.isEmpty()) {
                        fileViolations.addGeneralViolation(JsonViolationEnum.NON_AUTOCOMMIT_CONNECTION_MUST_HAVE_COMMIT_OR_ROLLBACK);
                    }

                });
    }

    private void checkAVOID_MDMCONNECTION_IN_SERVICE_PREJOB(TalendFile file, JsonFileViolations fileViolations, ProcessType process) {
        if (!file.getName().contains("Operation")) return; // Only services
        process.getNodeList().stream()
                .filter(n->n.isActive() && n instanceof TMDMConnectionType)
                .map(n->(TMDMConnectionType)n)
                .filter(n->process.isConnectedToPrejob(n.getComponentName()))
                .forEach(n-> fileViolations.addComponentViolation(n.getComponentName(), JsonViolationEnum.AVOID_MDMCONNECTION_IN_SERVICE_PREJOB));
    }

    private void checkAVOID_DBCONNECTION_IN_SERVICE_PREJOB(TalendFile file, JsonFileViolations fileViolations, ProcessType process) {
        if (!file.getName().contains("Operation")) return; // Only services
        process.getNodeList().stream()
                .filter(n->n.isActive() && n instanceof TOracleConnectionType)
                .map(n->(TOracleConnectionType)n)
                .filter(n->process.isConnectedToPrejob(n.getComponentName()))
                .forEach(n-> fileViolations.addComponentViolation(n.getComponentName(), JsonViolationEnum.AVOID_DBCONNECTION_IN_SERVICE_PREJOB));
    }

    private void checkMDMCONNECTION_MUST_BE_CLOSED(JsonFileViolations fileViolations, ProcessType process) {
        process.getNodeList().stream()
                .filter(n->n.isActive() && n instanceof TMDMConnectionType)
                .map(n->(TMDMConnectionType)n)
                .filter(n-> !process.isConnectedToPrejob(n.getUniqueName()))
                .forEach(conn->
                {
                    // Pour toutes les connexions MDM, on va vérifier qu'il y ait un close (commit+close ou commit+tClose, rollback+close ou rollback+tclose)

                    List<TMDMCommitType> lstCommitsNotClosed = process.getNodeList().stream()
                            .filter(n -> n.isActive() && n instanceof TMDMCommitType)
                            .map(n -> (TMDMCommitType) n)
                            .filter(n -> n.getConnection().equals(conn.getUniqueName()))
                            .filter(n->!n.isClose())
                            .collect(Collectors.toList());

                    List<TMDMRollbackType> lstRollbacksNotClosed = process.getNodeList().stream()
                            .filter(n -> n.isActive() && n instanceof TMDMRollbackType)
                            .map(n -> (TMDMRollbackType) n)
                            .filter(n->!n.isClose())
                            .filter(n -> n.getConnection().equals(conn.getUniqueName()))
                            .collect(Collectors.toList());

                    List<TMDMCloseType> lstClose = process.getNodeList().stream()
                            .filter(n -> n.isActive() && n instanceof TMDMCloseType)
                            .map(n -> (TMDMCloseType) n)
                            .filter(n -> n.getConnection().equals(conn.getUniqueName()))
                            .collect(Collectors.toList());

                    // On écrit le premier des composant (peut mieux faire)
                    if (lstClose.isEmpty() && !lstCommitsNotClosed.isEmpty()) {
                        fileViolations.addComponentViolation(lstCommitsNotClosed.get(0).getComponentName(), JsonViolationEnum.MDMCONNECTION_MUST_BE_CLOSED);
                    }
                    if (lstClose.isEmpty() && !lstRollbacksNotClosed.isEmpty()) {
                        fileViolations.addComponentViolation(lstRollbacksNotClosed.get(0).getComponentName(), JsonViolationEnum.MDMCONNECTION_MUST_BE_CLOSED);
                    }
                }
                );
    }


    private void checkDBCONNECTION_MUST_BE_CLOSED(JsonFileViolations fileViolations, ProcessType process) {
        process.getNodeList().stream()
                .filter(n->n.isActive() && n instanceof TOracleConnectionType)
                .map(n->(TOracleConnectionType)n)
                .filter(n-> !process.isConnectedToPrejob(n.getUniqueName()))
                .forEach(conn->
                        {
                            // Pour toutes les connexions MDM, on va vérifier qu'il y ait un close (commit+close ou commit+tClose, rollback+close ou rollback+tclose)

                            List<TOracleCommitType> lstCommitsNotClosed = process.getNodeList().stream()
                                    .filter(n -> n.isActive() && n instanceof TOracleCommitType)
                                    .map(n -> (TOracleCommitType) n)
                                    .filter(n -> n.getConnection().equals(conn.getUniqueName()))
                                    .filter(n->!n.isClose())
                                    .collect(Collectors.toList());

                            List<TOracleRollbackType> lstRollbacksNotClosed = process.getNodeList().stream()
                                    .filter(n -> n.isActive() && n instanceof TOracleRollbackType)
                                    .map(n -> (TOracleRollbackType) n)
                                    .filter(n->!n.isClose())
                                    .filter(n -> n.getConnection().equals(conn.getUniqueName()))
                                    .collect(Collectors.toList());

                            List<TOracleCloseType> lstClose = process.getNodeList().stream()
                                    .filter(n -> n.isActive() && n instanceof TOracleCloseType)
                                    .map(n -> (TOracleCloseType) n)
                                    .filter(n -> n.getConnection().equals(conn.getUniqueName()))
                                    .collect(Collectors.toList());

                            // On écrit le premier des composant (peut mieux faire)
                            if (lstClose.isEmpty() && !lstCommitsNotClosed.isEmpty()) {
                                fileViolations.addComponentViolation(lstCommitsNotClosed.get(0).getComponentName(), JsonViolationEnum.DBCONNECTION_MUST_BE_CLOSED);
                            }
                            if (lstClose.isEmpty() && !lstRollbacksNotClosed.isEmpty()) {
                                fileViolations.addComponentViolation(lstRollbacksNotClosed.get(0).getComponentName(), JsonViolationEnum.DBCONNECTION_MUST_BE_CLOSED);
                            }
                        }
                );
    }

    private void checkMDM_MUST_AUTOCOMMIT_OR_MANAGE_CONNECTION(JsonFileViolations fileViolations, ProcessType process) {
        boolean found = false;

        for (AbstractNodeType node : process.getNodeList()) {
            if (!node.isActive()) continue;
            if ((node.getComponentName().equals("tMDMConnection"))) {
                if (((TMDMConnectionType) node).isAutoCommit()) {
                    return; // OK
                } else {
                    found = true;
                }
                break;
            }
        }

        boolean foundMDMOutput = false;
        for (AbstractNodeType node : process.getNodeList()) {
            if (!node.isActive()) continue;
            if ((node.getComponentName().equals("tMDMOutput"))) {
                foundMDMOutput = true;
                break;
            }
        }

        // No warning if no output
        if (!foundMDMOutput) {
            return;
        }

        if (found) {
            // Should at least one commit and rollback component if no autocommit is set on connection
            boolean foundCommit = false;
            boolean foundRollback = false;
            for (AbstractNodeType node : process.getNodeList()) {
                if (!node.isActive()) continue;
                foundCommit = foundCommit || node.getComponentName().equals("tMDMCommit");
                foundRollback = foundRollback || node.getComponentName().equals("tMDMRollback");
            }

            if (!foundCommit) {
                fileViolations.addGeneralViolation(JsonViolationEnum.MDM_MUST_HAVE_COMMIT);
            }
            if (!foundRollback) {
                fileViolations.addGeneralViolation(JsonViolationEnum.MDM_MUST_HAVE_ROLLBACK);
            }
        }
    }


    private void checkAVOID_SYSTEM_OUT(JsonFileViolations fileViolations, AbstractNodeType node) {
        if (node.getComponentName().equals("tJava")) {
            TJavaType tJava = (TJavaType) node;
            if (tJava.getCode().contains("System.out") || tJava.getCode().contains("System.err")) {
                fileViolations.addComponentViolation(node.getUniqueName(), JsonViolationEnum.AVOID_SYSTEM_OUT);
            }
        }
        if (node.getComponentName().equals("tJavaRow")) {
            TJavaRowType tJavaRow = (TJavaRowType) node;
            if (tJavaRow.getCode().contains("System.out") || tJavaRow.getCode().contains("System.err")) {
                fileViolations.addComponentViolation(node.getUniqueName(), JsonViolationEnum.AVOID_SYSTEM_OUT);
            }
        }
        if (node.getComponentName().equals("tJavaFlex")) {
            TJavaFlexType tJava = (TJavaFlexType) node;
            if (tJava.getCodeStart().contains("System.out") || tJava.getCodeStart().contains("System.err")
                    || tJava.getCodeMain().contains("System.out") || tJava.getCodeMain().contains("System.err")
                    || tJava.getCodeEnd().contains("System.out") || tJava.getCodeEnd().contains("System.err")) {
                fileViolations.addComponentViolation(node.getUniqueName(), JsonViolationEnum.AVOID_SYSTEM_OUT);
            }
        }
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