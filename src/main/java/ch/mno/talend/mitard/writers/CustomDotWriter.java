package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.Context;
import ch.mno.talend.mitard.data.TalendFiles;
import ch.mno.talend.mitard.tools.DotWrapper;
import ch.mno.talend.mitard.writers.dependencies.CustomDotBuilder;
import ch.mno.talend.mitard.writers.dependencies.DependenciesData;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by dutoitc on 06.03.2019.
 */
public class CustomDotWriter extends AbstractWriter {

    private final DependenciesData dependenciesData;

    public CustomDotWriter(Context context, DependenciesData dependenciesData) {
        super(context);
        this.dependenciesData = dependenciesData;
    }


    @Override
    public void write(TalendFiles talendFiles) {
        try {
            List<String> definitions = getContext().getCustomDots();
            List<CustomDotJSon> list4Json = new ArrayList<>();
            WritersExecutor executor = new WritersExecutor();
            int i=1;
            for (String definition: definitions) {
                //Definition: ["Alimentation;B_.*;Alimentation", ...]
                list4Json.add(new CustomDotJSon("custom"+i+".png",definition.split(";")[0]));
                executor.submit(new DotProducer(i++, definition));
            }
            executor.stop();
            writeJson("diagrams.json", list4Json);
        } catch (Exception e) {
            System.err.println("Ignoring Dot generation due to an error: " + e.getMessage());
        }
    }


    private class CustomDotJSon implements Serializable {
        String filename;
        String schemaName;

        public CustomDotJSon(String filename, String schemaName) {
            this.filename = filename;
            this.schemaName = schemaName;
        }

        public String getFilename() {
            return filename;
        }

        public String getSchemaName() {
            return schemaName;
        }
    }



    private class DotProducer {

        private final int id;
        private final String definition;

        DotProducer(int id, String definition) {
            this.id = id;
            this.definition = definition;
        }

        void run() {
            // Build DOT
            String dot = new CustomDotBuilder(dependenciesData, definition).getDot();
            String dotFilename = getContext().getProductionPath() + "/data/custom"+id+".dot";

            // Write DOT
            try (
                    FileWriter writer = new FileWriter(new File(dotFilename))
            ) {
                IOUtils.write(dot, writer);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // DOT to PNG
            try {
                DotWrapper.generatePNG(getContext().getDotPath(), getContext().getProductionPath() + "/data/custom"+id+".png", dotFilename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private static class WritersExecutor {
        ExecutorService executor = Executors.newFixedThreadPool(16);

        public void submit(DotProducer dotProducer) {
            executor.submit(() ->
                    {
                        try {
                            dotProducer.run();
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
