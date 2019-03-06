package ch.mno.talend.mitard;


import ch.mno.talend.mitard.data.Context;
import ch.mno.talend.mitard.data.TalendFiles;
import ch.mno.talend.mitard.helpers.TalendFileHelper;
import ch.mno.talend.mitard.writers.*;
import ch.mno.talend.mitard.writers.dependencies.DependenciesData;
import ch.mno.talend.mitard.writers.dependencies.DependenciesWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException, JAXBException, ParserConfigurationException, SAXException {
        long t0 = System.currentTimeMillis();

        // Pre-requisites
        LOG.info("\n_____./ MITARD  \\._______________________________________________________");
        if (args.length == 0) {
            System.err.println("Missing context.properties as first program argument");
            System.exit(1);
        }

        // Context, files read
        Context context = new Context(new FileInputStream(args[0]));
        TalendFiles talendFiles = TalendFileHelper.findLatestVersions(context.getWorkspacePath());
        LOG.info("Found " + talendFiles.toString());

        // Dependencies (start first, dot takes long time to run)
        WritersExecutor writersExecutor = new WritersExecutor();
        DependenciesWriter dependenciesWriter = new DependenciesWriter(context);
        writersExecutor.submit(dependenciesWriter, talendFiles);
        DependenciesData dependenciesData = dependenciesWriter.getDependenciesData();

        // Init production path
        new OutputWriter().write(context.getProductionPath());

        // JSON
        writersExecutor.submit(new ProcessesWriter(context), talendFiles);
        writersExecutor.submit(new RoutesWriter(context), talendFiles);
        writersExecutor.submit(new ServicesWriter(context), talendFiles);
        writersExecutor.submit(new StatisticsWriter(context), talendFiles);
        writersExecutor.submit(new ViolationsWriter(context), talendFiles);
        writersExecutor.submit(new SearchWriter(context), talendFiles);
        writersExecutor.submit(new DatasourcesWriter(context), talendFiles);
        writersExecutor.submit(new CustomDotWriter(context, dependenciesData), talendFiles);

        // Finish
        writersExecutor.stop();
        LOG.info("Wrote Mitard site to " + context.getProductionPath());
        LOG.info("Mitard run took " + (System.currentTimeMillis() - t0) / 1000.0 + "s");
    }

    private static class WritersExecutor {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        public void submit(AbstractWriter writer, TalendFiles talendFiles) {
            executor.submit(() ->
                    {
                        try {
                            writer.write(talendFiles);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            );
        }

        public void stop() {
            executor.shutdown();
            try {
                executor.awaitTermination(15, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
