package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.Context;
import ch.mno.talend.mitard.data.TalendFile;
import ch.mno.talend.mitard.data.TalendFiles;
import ch.mno.talend.mitard.out.JsonProcesses;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by dutoitc on 13.05.2015.
 */
public class ProcessesWriter extends AbstractNodeWriter {


    public ProcessesWriter(Context context) {
        super(context);
    }

    public void write(TalendFiles talendFiles) throws IOException {
        JsonProcesses jsonProcesses = new JsonProcesses();


        for (TalendFile file : talendFiles.getProcesses()) {
            if (isBlacklisted(file.getName())) continue;
            System.out.println("Reading " + new File(file.getItemFilename()).getName());

            List<String> screenshots = extractScreenshots(file);

            String data = IOUtils.toString(new FileReader(file.getPropertiesFilename()));

            jsonProcesses.addProcess(file.getPath(), file.getName(), file.getVersion(), readPurpose(data), readDescription(data), readCreationDate(data), readModificationDate(data), screenshots);
        }
        writeJson("processes.json", jsonProcesses);
    }

}