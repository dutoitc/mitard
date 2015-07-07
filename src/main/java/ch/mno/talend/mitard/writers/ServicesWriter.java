package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.Context;
import ch.mno.talend.mitard.data.TalendFile;
import ch.mno.talend.mitard.data.TalendFiles;
import ch.mno.talend.mitard.data.TalendProjectType;
import ch.mno.talend.mitard.data.TalendUserType;
import ch.mno.talend.mitard.out.JsonServices;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dutoitc on 13.05.2015.
 */
public class ServicesWriter extends AbstractNodeWriter {

    public ServicesWriter(Context context) {
        super(context);
    }

    public void write(TalendFiles talendFiles, TalendProjectType project) throws IOException {
        JsonServices jsonServices = new JsonServices();
        for (TalendFile file : talendFiles.getServices()) {
            if (isBlacklisted(file.getName())|| isBlacklisted(file.getPath())) continue;
            System.out.println("Reading " + new File(file.getItemFilename()).getName());

            List<String> screenshots = extractScreenshots(file);

            String data = IOUtils.toString(new InputStreamReader(new FileInputStream(file.getPropertiesFilename()), "UTF-8"));

            // Read author
            Matcher matcherItem = Pattern.compile("author .*?talend.project.(.*?)\"").matcher(data);
            matcherItem.find();
            String authorId = matcherItem.group(1);
            TalendUserType author = project.getUserById(authorId);


            jsonServices.addService(file.getPath(), file.getName(), file.getVersion(), readPurpose(data), readDescription(data), readCreationDate(data), readModificationDate(data), screenshots, author);
        }
        writeJson("services.json", jsonServices);
    }

}
