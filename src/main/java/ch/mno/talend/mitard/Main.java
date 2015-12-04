package ch.mno.talend.mitard;


import ch.mno.talend.mitard.data.Context;
import ch.mno.talend.mitard.data.TalendFiles;
import ch.mno.talend.mitard.helpers.TalendFileHelper;
import ch.mno.talend.mitard.writers.*;
import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, JAXBException, ParserConfigurationException, SAXException {
        if (args.length==0) {
            throw new RuntimeException("Missing context.properties as first program argument");
        }

        Context context = new Context(new FileInputStream(args[0]));

        TalendFiles talendFiles = TalendFileHelper.findLatestVersions(context.getWorkspacePath());

        // Init production path
        File productionDir = new File(context.getProductionPath());
        productionDir.delete();
        productionDir.mkdir();
        new File(context.getProductionPath()+File.separatorChar+"data").mkdir();
        copyResources("/template/app", context.getProductionPath());
        copyResources("/template/css", context.getProductionPath());
        copyResources("/template/fonts", context.getProductionPath());
        copyResources("/template/js", context.getProductionPath());
        copyResourcesFile("/template/index.html", context.getProductionPath() + "/index.html");
        copyResourcesFile("/template/mitard.png", context.getProductionPath()+"/mitard.png");


        // Dependencies
        new DependenciesWriter(context).write(talendFiles);

        // JSON
        new ProcessesWriter(context).write(talendFiles);
        new RoutesWriter(context).write(talendFiles);
        new ServicesWriter(context).write(talendFiles);
        new StatisticsWriter(context).write(talendFiles);
        new ViolationsWriter(context).write(talendFiles);
        System.out.println("Wrote Mitard site to " + context.getProductionPath());
    }

    private static void copyResources(String sourcePath, String targetPath) throws IOException {
        File source = new File(Main.class.getResource(sourcePath).getFile()); // FIXME: find a way to make it work if packaged
        File target = new File(targetPath);
        FileUtils.copyDirectoryToDirectory(source, target);
    }


    private static void copyResourcesFile(String sourcePath, String targetPath) throws IOException {
        File source = new File(Main.class.getResource(sourcePath).getFile()); // FIXME: find a way to make it work if packaged
        File target = new File(targetPath);
        FileUtils.copyFile(source, target);
    }

}
