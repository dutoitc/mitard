package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.Context;
import ch.mno.talend.mitard.data.TalendFile;
import ch.mno.talend.mitard.data.TalendFiles;
import ch.mno.talend.mitard.data.TalendUserType;
import ch.mno.talend.mitard.out.JsonRoutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Write routes.json (route is name, version, path, purpose, description, date, screenshots...)
 * Created by dutoitc on 13.05.2015.
 */
public class RoutesWriter extends AbstractNodeWriter {
    public static Logger LOG = LoggerFactory.getLogger(RoutesWriter.class);


    public RoutesWriter(Context context) {
        super(context);
    }

    public void write(TalendFiles talendFiles) {
        try {
            // Routes
            JsonRoutes jsonRoutes = new JsonRoutes();
            for (TalendFile file : talendFiles.getRoutes()) {
                if (isBlacklisted(file.getName()) || isBlacklisted(file.getPath())) continue;
                LOG.debug("Reading " + new File(file.getItemFilename()).getName());

                List<String> screenshots = extractScreenshots(file);

                String data = new String(Files.readAllBytes(Paths.get(file.getPropertiesFilename())), "UTF-8");

                // Read author
                TalendUserType author = extractAuthor(talendFiles, data);

                jsonRoutes.addRoute(file.getPath(), file.getName(), file.getVersion(), readPurpose(data), readDescription(data), readCreationDate(data), readModificationDate(data), screenshots, author);
            }
            writeJson("routes.json", jsonRoutes);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
