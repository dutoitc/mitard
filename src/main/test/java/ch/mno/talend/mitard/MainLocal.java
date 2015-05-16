package ch.mno.talend.mitard;

import ch.mno.talend.mitard.data.AbstractNodeType;
import ch.mno.talend.mitard.data.ProcessType;
import ch.mno.talend.mitard.data.TalendFile;
import ch.mno.talend.mitard.data.TalendFiles;
import ch.mno.talend.mitard.helpers.TalendFileHelper;
import ch.mno.talend.mitard.readers.ProcessReader;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class MainLocal {

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        TalendFiles talendFiles = new TalendFileHelper().findLatestVersions("C:\\projets\\talend-mitard\\mitard\\src\\main\\test\\resources\\ESBTUTORIALPROJECT");

        for (TalendFile file: talendFiles.getProcesses()) {
            System.out.println(new File(file.getItemFilename()).getName());
            FileInputStream fis = new FileInputStream(file.getItemFilename());
            ProcessType process = ProcessReader.reader(fis);
            for (AbstractNodeType node : process.getNodeList()) {
                System.out.println("   "+node.toString());
            }
        }

        ProcessType process = ProcessReader.reader(new FileInputStream("C:\\projets\\talend-mitard\\mitard\\src\\main\\test\\resources\\ESBTUTORIALPROJECT\\process\\GreetingServiceConsumer_0.1.item"));
    }
}
