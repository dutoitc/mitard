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
            String definition = getContext().getCustomDot();
            if (definition!=null) {
                writeDot(definition);
            }
        } catch (Exception e) {
            System.err.println("Ignoring Dot generation due to an error: " + e.getMessage());
        }

    }

    private void writeDot(String customDotBuilderDefinition) throws IOException {
        // Build DOT
        String dot = new CustomDotBuilder(dependenciesData, customDotBuilderDefinition).getDot();
        String dotFilename = getContext().getProductionPath() + "/data/custom.dot";

        // Write DOT
        try (
                FileWriter writer = new FileWriter(new File(dotFilename))
        ) {
            IOUtils.write(dot, writer);
            writer.flush();
        }

        // DOT to PNG
        DotWrapper.generatePNG(getContext().getDotPath(), getContext().getProductionPath() + "/data/custom.png", dotFilename);
    }


}
