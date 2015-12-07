package ch.mno.talend.mitard;


import ch.mno.talend.mitard.data.Context;
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
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


    public static Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException, JAXBException, ParserConfigurationException, SAXException {
        long t0 = System.currentTimeMillis();

        // Pre-requisites
        LOG.info("_____./ MITARD  \\._______________________________________________________");
        if (args.length==0) {
            System.err.println("Missing context.properties as first program argument");
            System.exit(1);
        }

        // Context, files read
        Context context = new Context(new FileInputStream(args[0]));
        TalendFiles talendFiles = TalendFileHelper.findLatestVersions(context.getWorkspacePath());
        LOG.info("Found " + talendFiles.toString());

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
        new SearchWriter(context).write(talendFiles);
        LOG.info("Wrote Mitard site to " + context.getProductionPath());
        LOG.info("Mitard run finished after " + (System.currentTimeMillis()-t0)/1000.0 + "s");
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
                LOG.debug(context.getProductionPath() + File.separatorChar + line);
                Files.copy(Main.class.getResourceAsStream("/template/"+line),
                        Paths.get(context.getProductionPath() + File.separatorChar + line), StandardCopyOption.REPLACE_EXISTING);

            } else {
                // Folder
                String pathname = context.getProductionPath() + File.separatorChar + line;
                LOG.debug("Creating path " + pathname);
                new File(pathname).mkdirs();
            }
        }
    }

}
