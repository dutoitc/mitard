package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.Context;
import ch.mno.talend.mitard.data.TalendFile;
import ch.mno.talend.mitard.data.TalendFiles;
import ch.mno.talend.mitard.data.TalendUserType;
import ch.mno.talend.mitard.out.JsonServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Read all processes and write services.json
 */
public class ServicesWriter extends AbstractNodeWriter {

    public static Logger LOG = LoggerFactory.getLogger(ServicesWriter.class);

    public ServicesWriter(Context context) {
        super(context);
    }

    public void write(TalendFiles talendFiles) {
        try {
            JsonServices jsonServices = new JsonServices();
            for (TalendFile file : filterBlacklisted(talendFiles.getServices())) {
                LOG.debug("Reading " + new File(file.getItemFilename()).getName());

                List<String> screenshots = extractScreenshots(file);

                String data = new String(Files.readAllBytes(Paths.get(file.getPropertiesFilename())), "UTF-8");

                // Read author
                TalendUserType author = extractAuthor(talendFiles, data);

                jsonServices.addService(file.getPath(), file.getName(), file.getVersion(), readPurpose(data), readDescription(data), readCreationDate(data), readModificationDate(data), screenshots, author);
            }
            writeJson("services.json", jsonServices);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
