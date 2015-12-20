package ch.mno.talend.mitard.writers;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Output writer, initialize and write on the production path.
 * Created by dutoitc on 18.12.2015.
 */
public class OutputWriter {

    public static Logger LOG = LoggerFactory.getLogger(OutputWriter.class);

    public void write(String productionPath) {
        File productionDir = new File(productionPath);
        productionDir.delete();
        productionDir.mkdir();
        new File(productionPath + File.separatorChar + "data").mkdir();
        try {
            initTemplate(productionPath);
        } catch (IOException e) {
            throw new RuntimeException("Cannot produce output: " + e.getMessage(), e);
        }
    }


    /** Initialize production path from template index.txt file: create folders and copy files */
    private static void initTemplate(String productionPath) throws IOException {
        String index = IOUtils.toString(OutputWriter.class.getResourceAsStream("/template/index.txt"));
        for (String line : index.split("\n")) {
            if (line.length() < 2) continue;
            line = line.substring(2); // Remove "./" at start
            if (line.endsWith("\r")) line = line.substring(0, line.length() - 1);
            if (line.contains(".")) {
                if (File.separatorChar == '\\') {
                    line = line.replaceAll("/", "\\\\");
                }
                // File
                LOG.debug(productionPath + File.separatorChar + line);
                Files.copy(OutputWriter.class.getResourceAsStream("/template/" + line),
                        Paths.get(productionPath + File.separatorChar + line), StandardCopyOption.REPLACE_EXISTING);

            } else {
                // Folder
                String pathname = productionPath + File.separatorChar + line;
                LOG.debug("Creating path " + pathname);
                new File(pathname).mkdirs();
            }
        }
    }

}
