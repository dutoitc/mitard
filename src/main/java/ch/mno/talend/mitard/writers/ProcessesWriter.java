package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.Context;
import ch.mno.talend.mitard.data.TalendFile;
import ch.mno.talend.mitard.data.TalendFiles;
import ch.mno.talend.mitard.data.TalendUserType;
import ch.mno.talend.mitard.out.JsonProcesses;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dutoitc on 13.05.2015.
 */
public class ProcessesWriter extends AbstractNodeWriter {
    public static Logger LOG = LoggerFactory.getLogger(ProcessesWriter.class);


    public ProcessesWriter(Context context) {
        super(context);
    }

    public void write(TalendFiles talendFiles) throws IOException {
        JsonProcesses jsonProcesses = new JsonProcesses();


        for (TalendFile file : talendFiles.getProcesses()) {
            if (isBlacklisted(file.getName())|| isBlacklisted(file.getPath())) continue;
            LOG.debug("Reading " + new File(file.getItemFilename()).getName());

            List<String> screenshots = extractScreenshots(file);

            String dataProperties = IOUtils.toString(new InputStreamReader(new FileInputStream(file.getPropertiesFilename()), "UTF-8"));


            // Read author
            Matcher matcherItem = Pattern.compile("author.*?talend.project.(.*?)\"").matcher(dataProperties);
            matcherItem.find();
            String authorId = matcherItem.group(1);
            TalendUserType author = talendFiles.getProject().getUserById(authorId);

            jsonProcesses.addProcess(file.getPath(), file.getName(), file.getVersion(), readPurpose(dataProperties), readDescription(dataProperties), readCreationDate(dataProperties), readModificationDate(dataProperties), screenshots, author);
        }
        writeJson("processes.json", jsonProcesses);
    }

}