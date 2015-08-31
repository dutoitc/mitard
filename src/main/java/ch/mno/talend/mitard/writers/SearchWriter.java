package ch.mno.talend.mitard.writers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import ch.mno.talend.mitard.data.AbstractNodeType;
import ch.mno.talend.mitard.data.Context;
import ch.mno.talend.mitard.data.ProcessType;
import ch.mno.talend.mitard.data.PropertiesType;
import ch.mno.talend.mitard.data.TDieType;
import ch.mno.talend.mitard.data.TJavaFlexType;
import ch.mno.talend.mitard.data.TJavaRowType;
import ch.mno.talend.mitard.data.TJavaType;
import ch.mno.talend.mitard.data.TMDMCommitType;
import ch.mno.talend.mitard.data.TNodeType;
import ch.mno.talend.mitard.data.TOracleCommitType;
import ch.mno.talend.mitard.data.TRestRequestType;
import ch.mno.talend.mitard.data.TRunJobType;
import ch.mno.talend.mitard.data.TalendFile;
import ch.mno.talend.mitard.data.TalendFiles;
import ch.mno.talend.mitard.out.JsonFileViolations;
import ch.mno.talend.mitard.out.JsonViolationEnum;
import ch.mno.talend.mitard.readers.ProcessReader;
import ch.mno.talend.mitard.readers.PropertiesReader;

/**
 * Created by dutoitc on 13.05.2015.
 */
public class SearchWriter extends AbstractNodeWriter {


    public SearchWriter(Context context) {
        super(context);
    }

    public void write(TalendFiles talendFiles) throws IOException, ParserConfigurationException, SAXException {
        JSonTextFiles textFiles = new JSonTextFiles();


        for (TalendFile file : talendFiles.getProcesses()) {
            if (isBlacklisted(file.getName()) || isBlacklisted(file.getPath())) continue;
            System.out.println("Reading " + new File(file.getItemFilename()).getName());

            JSonTextFile textFile = new JSonTextFile(file.getName());
            textFiles.addJSonTextFile(textFile);

            FileInputStream fis = new FileInputStream(file.getItemFilename());
            ProcessType process = ProcessReader.reader(fis);
            int nbInactive = 0;
            for (AbstractNodeType node : process.getNodeList()) {
                if (!node.isActive()) {
                    nbInactive++;
                    continue;
                }

                if (node instanceof TJavaFlexType) {
                    TJavaFlexType node1 = (TJavaFlexType) node;
                    String text = node1.getCodeStart() + node1.getCodeMain() + node1.getCodeEnd();
                    textFile.addText(node.getUniqueName(), text);
                } else if (node instanceof TJavaType) {
                    TJavaType node1 = (TJavaType) node;
                    String text = node1.getCode();
                    textFile.addText(node.getUniqueName(), text);
                } else if (node instanceof TDieType) {
                    TDieType node1 = (TDieType) node;
                    String text = node1.getMessage();
                    textFile.addText(node.getUniqueName(), text);
                }
            }


        }

        writeJson("textFiles.json", textFiles);
    }


    class JSonTextFiles {
        private List<JSonTextFile> textFiles = new ArrayList<>();

        public void addJSonTextFile(JSonTextFile f) {
            textFiles.add(f);
        }

        public List<JSonTextFile> getTextFiles() {
            return textFiles;
        }

    }

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