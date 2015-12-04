package ch.mno.talend.mitard;


import ch.mno.talend.mitard.data.Context;
import ch.mno.talend.mitard.data.TalendFile;
import ch.mno.talend.mitard.data.TalendFiles;
import ch.mno.talend.mitard.data.TalendProjectType;
import ch.mno.talend.mitard.helpers.TalendFileHelper;
import ch.mno.talend.mitard.readers.TalendProjectReader;
import ch.mno.talend.mitard.writers.DependenciesWriter;
import ch.mno.talend.mitard.writers.ProcessesWriter;
import ch.mno.talend.mitard.writers.RoutesWriter;
import ch.mno.talend.mitard.writers.SearchWriter;
import ch.mno.talend.mitard.writers.ServicesWriter;
import ch.mno.talend.mitard.writers.StatisticsWriter;
import ch.mno.talend.mitard.writers.ViolationsWriter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Main {

    public static void main(String[] args) throws IOException, JAXBException, ParserConfigurationException, SAXException {
        if (args.length==0) {
            throw new RuntimeException("Missing context.properties as first program argument");
        }

        Context context = new Context(new FileInputStream(args[0]));

        TalendFiles talendFiles = TalendFileHelper.findLatestVersions(context.getWorkspacePath());
        for (TalendFile f: talendFiles.getProcesses()) {
            System.out.println(f.getName() + " " + f.getVersion());
        }
        TalendProjectType project = TalendProjectReader.read(new FileInputStream(context.getWorkspacePath() + "/talend.project"));

        // Init production path
        File productionDir = new File(context.getProductionPath());
        productionDir.delete();
        productionDir.mkdir();
        new File(context.getProductionPath()+File.separatorChar+"data").mkdir();
        initTemplate(context);


        // Dependencies
        new DependenciesWriter(context).write(talendFiles);

        // JSON
        new ProcessesWriter(context).write(talendFiles, project);
        new RoutesWriter(context).write(talendFiles, project);
        new ServicesWriter(context).write(talendFiles, project);
        new StatisticsWriter(context).write(talendFiles);
        new ViolationsWriter(context).write(talendFiles);
        System.out.println("Wrote Mitard site to " + context.getProductionPath());
        new SearchWriter(context).write(talendFiles);
    }

    private static void initTemplate(Context context) throws IOException {
        String s = IOUtils.toString(Main.class.getResourceAsStream("/template/index.txt"));
        for (String line: s.split("\n")) {
            if (line.length()<2) continue;
            line = line.substring(2); // Remove "./" at start
            if (line.endsWith("\r")) line = line.substring(0, line.length()-1);
            if (line.contains(".")) {
                if (File.separatorChar=='\\') {
                    line = line.replaceAll("/", "\\\\");
                }
                // File
//                InputStream is =Main.class.getResourceAsStream("/template/" + line);
                System.out.println(context.getProductionPath() + File.separatorChar + line);
                Files.copy(Main.class.getResourceAsStream("/template/"+line),
                        Paths.get(context.getProductionPath() + File.separatorChar + line), StandardCopyOption.REPLACE_EXISTING);

//                try (FileOutputStream fis = new FileOutputStream(target)) {
//                    IOUtils.copy(is, fis);
//                }
            } else {
                // Folder
                String pathname = context.getProductionPath() + File.separatorChar + line;
                System.out.println("Creating path " + pathname);
                new File(pathname).mkdirs();
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
