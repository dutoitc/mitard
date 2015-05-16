package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.Context;
import ch.mno.talend.mitard.data.TalendFile;
import ch.mno.talend.mitard.data.TalendFiles;
import ch.mno.talend.mitard.out.JsonServices;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by dutoitc on 13.05.2015.
 */
public class ServicesWriter extends AbstractNodeWriter {

    public ServicesWriter(Context context) {
        super(context);
    }

    public void write(TalendFiles talendFiles) throws IOException {
        JsonServices jsonServices = new JsonServices();
        for (TalendFile file : talendFiles.getServices()) {
            if (isBlacklisted(file.getName())) continue;
            System.out.println("Reading " + new File(file.getItemFilename()).getName());

            List<String> screenshots = extractScreenshots(file);


            String data = IOUtils.toString(new FileReader(file.getPropertiesFilename()));
            jsonServices.addService(file.getPath(), file.getName(), file.getVersion(), readPurpose(data), readDescription(data), readCreationDate(data), readModificationDate(data), screenshots);
        }
        writeJson("services.json", jsonServices);
    }

}
