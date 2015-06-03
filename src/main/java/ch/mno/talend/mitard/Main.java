package ch.mno.talend.mitard;


import ch.mno.talend.mitard.data.Context;
import ch.mno.talend.mitard.data.TalendFiles;
import ch.mno.talend.mitard.helpers.TalendFileHelper;
import ch.mno.talend.mitard.writers.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
        initTemplate(context);


        // Dependencies
        new DependenciesWriter(context).write(talendFiles);

        // JSON
        new ProcessesWriter(context).write(talendFiles);
        new RoutesWriter(context).write(talendFiles);
        new ServicesWriter(context).write(talendFiles);
        new StatisticsWriter(context).write(talendFiles);
        new ViolationsWriter(context).write(talendFiles);
    }

    private static void initTemplate(Context context) throws IOException {
        String s = IOUtils.toString(Main.class.getResourceAsStream("/template/index.txt"));
        for (String line: s.split("\n")) {
            if (line.length()<2) continue;
            line = line.substring(2); // Remove "./" at start
            if (line.contains(".")) {
                // File
                InputStream is =Main.class.getResourceAsStream("/template/" + line);
                assert is!=null;
                File target = new File(context.getProductionPath() + File.separatorChar + line);
                try (FileOutputStream fis = new FileOutputStream(target)) {
                    IOUtils.copy(is, fis);
                }
            } else {
                // Folder
                String pathname = context.getProductionPath() + File.separatorChar + line;
                System.out.println("Creating path " + pathname);
                new File(pathname).mkdir();

            }
            //./app
            //./app/app.js
        }
    }

    private static void initTemplateOld(Context context) throws IOException {
        copyResources("/template/app", context.getProductionPath());
        copyResources("/template/css", context.getProductionPath());
        copyResources("/template/fonts", context.getProductionPath());
        copyResources("/template/js", context.getProductionPath());
        copyResourcesFile("/template/index.html", context.getProductionPath() + "/index.html");
        copyResourcesFile("/template/mitard.png", context.getProductionPath()+"/mitard.png");
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
