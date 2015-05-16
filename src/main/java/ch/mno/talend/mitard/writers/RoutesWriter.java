package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.Context;
import ch.mno.talend.mitard.data.TalendFile;
import ch.mno.talend.mitard.data.TalendFiles;
import ch.mno.talend.mitard.out.JsonRoutes;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by dutoitc on 13.05.2015.
 */
public class RoutesWriter extends AbstractNodeWriter {

    public RoutesWriter(Context context) {
        super(context);
    }

    public void write(TalendFiles talendFiles) throws IOException {
        // Routes
        JsonRoutes jsonRoutes = new JsonRoutes();
        for (TalendFile file: talendFiles.getRoutes()) {
            if (isBlacklisted(file.getName())) continue;
            System.out.println("Reading " + new File(file.getItemFilename()).getName());

            List<String> screenshots = extractScreenshots(file);

            String data = IOUtils.toString(new FileReader(file.getPropertiesFilename()));
            jsonRoutes.addRoute(file.getPath(), file.getName(), file.getVersion(), readPurpose(data), readDescription(data), readCreationDate(data), readModificationDate(data), screenshots);
        }
        writeJson("routes.json", jsonRoutes);
    }


}
