package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.Context;
import ch.mno.talend.mitard.data.TalendFile;
import ch.mno.talend.mitard.data.TalendFiles;
import ch.mno.talend.mitard.data.TalendProjectType;
import ch.mno.talend.mitard.data.TalendUserType;
import ch.mno.talend.mitard.out.JsonServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  Read all processes and write services.json
 * Created by dutoitc on 13.05.2015.
 */
public class ServicesWriter extends AbstractNodeWriter {

    public static final Pattern PAT_AUTHOR = Pattern.compile("author .*?talend.project.(.*?)\"");
    public static Logger LOG = LoggerFactory.getLogger(ServicesWriter.class);

    public ServicesWriter(Context context) {
        super(context);
    }

    public void write(TalendFiles talendFiles, TalendProjectType project) throws IOException {
        JsonServices jsonServices = new JsonServices();
        for (TalendFile file : talendFiles.getServices()) {
            if (isBlacklisted(file.getName())|| isBlacklisted(file.getPath())) continue;
            LOG.info("Reading " + new File(file.getItemFilename()).getName());

            List<String> screenshots = extractScreenshots(file);


            String data = new String(Files.readAllBytes(Paths.get(file.getPropertiesFilename())), "UTF-8");
//            String data = IOUtils.toString(new InputStreamReader(new FileInputStream(file.getPropertiesFilename()), "UTF-8"));

            // Read author
            Matcher matcherItem = PAT_AUTHOR.matcher(data);
            if (matcherItem.find() ) {
                String authorId = matcherItem.group(1);
                TalendUserType author = project.getUserById(authorId);
                jsonServices.addService(file.getPath(), file.getName(), file.getVersion(), readPurpose(data), readDescription(data), readCreationDate(data), readModificationDate(data), screenshots, author);
            }
        }
        writeJson("services.json", jsonServices);
    }

}
