package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.*;
import ch.mno.talend.mitard.readers.ProcessReader;
import ch.mno.talend.mitard.readers.treaders.TMDMRestInput;
import ch.mno.talend.mitard.readers.treaders.TMDMViewSearch;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Read all processes and write textFiles.json, including Fulltext data.
 */
public class SearchWriter extends AbstractNodeWriter {

    public static Logger LOG = LoggerFactory.getLogger(SearchWriter.class);

    public SearchWriter(Context context) {
        super(context);
    }

    public void write(TalendFiles talendFiles) {
        try {
            JSonTextFiles textFiles = new JSonTextFiles();

            for (TalendFile file : filterBlacklisted(talendFiles.getProcesses())) {
                try {
                    LOG.debug("Reading " + new File(file.getItemFilename()).getName());

                    JSonTextFile textFile = new JSonTextFile(file.getName());
                    textFiles.addJSonTextFile(textFile);

                    // Extract texts from all nodes of file
                    ProcessType process = ProcessReader.read(file.getItemFilename());
                    process.getNodeList().stream()
                            .filter(node -> node.isActive())
                            .forEach(node -> textFile.addText(node.getUniqueName(), extractText(node).toLowerCase()));

                } catch (Exception e) {
                    LOG.error("Error writing search infosfor " + file.getName() + " (ignoring file): " + e.getMessage());
                }
            }

            writeJson("textFiles.json", textFiles);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * Extract text from node
     */
    private String extractText(AbstractNodeType abstractNode) {
        if (abstractNode instanceof TJavaFlexType) {
            TJavaFlexType node = (TJavaFlexType) abstractNode;
            return node.getCodeStart() + node.getCodeMain() + node.getCodeEnd();
        } else if (abstractNode instanceof TJavaType) {
            TJavaType node = (TJavaType) abstractNode;
            return node.getCode();
        } else if (abstractNode instanceof TDieType) {
            TDieType node = (TDieType) abstractNode;
            return node.getMessage();
        } else if (abstractNode instanceof TFixedFlowInputType) {
            TFixedFlowInputType node = (TFixedFlowInputType) abstractNode;
            return node.getText();
        } else if (abstractNode instanceof TOracleInputType) {
            TOracleInputType node = (TOracleInputType) abstractNode;
            return node.getText();
        } else if (abstractNode instanceof TOracleOutputType) {
            TOracleOutputType node = (TOracleOutputType) abstractNode;
            return node.getText();
        } else if (abstractNode instanceof TOracleConnectionType) {
            TOracleConnectionType node = (TOracleConnectionType) abstractNode;
            return node.getDbName() + " " + (node.isSpecifyDatasourceAlias() ? node.getDatasourceAlias() : "");
        } else if (abstractNode instanceof TOracleRowType) {
            TOracleRowType node = (TOracleRowType) abstractNode;
            return node.getQuery();
        } else if (abstractNode instanceof TMDMInputType) {
            TMDMInputType node = (TMDMInputType) abstractNode;
            return node.getConcept();
        } else if (abstractNode instanceof TMDMOutputType) {
            TMDMOutputType node = (TMDMOutputType) abstractNode;
            return StringUtils.join(node.getPath(), ';');
        } else if (abstractNode instanceof TMDMViewSearchType) {
            TMDMViewSearchType node = (TMDMViewSearchType) abstractNode;
            return StringUtils.join(node.getOperations(), ';');
        } else if (abstractNode instanceof TMDMRestInputType) {
            TMDMRestInputType node = (TMDMRestInputType) abstractNode;
            return node.getQuery();
        }
        return "";
    }


    /**
     * Object with many JSonTextFile
     */
    class JSonTextFiles {
        private List<JSonTextFile> textFiles = new ArrayList<>();

        public void addJSonTextFile(JSonTextFile f) {
            textFiles.add(f);
        }

        public List<JSonTextFile> getTextFiles() {
            return textFiles;
        }

    }

    /**
     * Object with filename and many JsonText
     */
    class JSonTextFile {
        private String filename;
        private List<JsonText> textList = new ArrayList<>();

        public JSonTextFile(String filename) {
            this.filename = filename;
        }

        public void addText(String nodeName, String text) {
            textList.add(new JsonText(nodeName, text));
        }

        public String getFilename() {
            return filename;
        }

        public List<JsonText> getTextList() {
            return textList;
        }

    }

    /**
     * Object with node name and text
     */
    class JsonText {

        private String nodeName;
        private String text;

        public JsonText(String nodeName, String text) {
            this.nodeName = nodeName;
            this.text = text;
        }

        public String getNodeName() {
            return nodeName;
        }

        public String getText() {
            return text;
        }
    }

}