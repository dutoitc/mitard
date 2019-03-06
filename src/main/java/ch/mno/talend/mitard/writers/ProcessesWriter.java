package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.Context;
import ch.mno.talend.mitard.data.TalendFile;
import ch.mno.talend.mitard.data.TalendFiles;
import ch.mno.talend.mitard.data.TalendUserType;
import ch.mno.talend.mitard.out.JsonProcesses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Write processes.json (process is name, version, path, purpose, description, date, screenshots...)
 * Created by dutoitc on 13.05.2015.
 */
public class ProcessesWriter extends AbstractNodeWriter {
    public static Logger LOG = LoggerFactory.getLogger(ProcessesWriter.class);


    public ProcessesWriter(Context context) {
        super(context);
    }

    public void write(TalendFiles talendFiles) {
        try {
            JsonProcesses jsonProcesses = new JsonProcesses();

            for (TalendFile file : filterBlacklisted(talendFiles.getProcesses())) {
                LOG.debug("Reading " + new File(file.getItemFilename()).getName());
                if (!new File(file.getPropertiesFilename()).exists()) {
                    LOG.warn("Skipping inexistent properties file: " + file.getPropertiesFilename() + "; this could be a project stability error (did the studio crashed with the job opened ?)");
                    continue;
                }

                // Read screenshots and properties
                List<String> screenshots = extractScreenshots(file);
                String data = new String(Files.readAllBytes(Paths.get(file.getPropertiesFilename())), "UTF-8");

                // Read author
                TalendUserType author = extractAuthor(talendFiles, data);

                jsonProcesses.addProcess(file.getPath(), file.getName(), file.getVersion(), readPurpose(data), readDescription(data), readCreationDate(data), readModificationDate(data), screenshots, author);
            }
            writeJson("processes.json", jsonProcesses);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}