package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.Context;
import ch.mno.talend.mitard.data.TalendFile;
import ch.mno.talend.mitard.data.TalendFiles;
import ch.mno.talend.mitard.data.TalendUserType;
import ch.mno.talend.mitard.data.WorkflowType;
import ch.mno.talend.mitard.out.JsonWorkflows;
import ch.mno.talend.mitard.readers.WorkflowReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Write workflows.json (BPM)
 * Created by dutoitc on 13.05.2015.
 */
public class WorkflowsWriter extends AbstractNodeWriter {
    public static Logger LOG = LoggerFactory.getLogger(WorkflowsWriter.class);


    public WorkflowsWriter(Context context) {
        super(context);
    }

    public void write(TalendFiles talendFiles) {
        try {
            JsonWorkflows jsonWorkflows = new JsonWorkflows();

            for (TalendFile file : filterBlacklisted(talendFiles.getMDMWorkflowProc())) {
                try {
                    String filename = file.getProcFilename();
                    FileInputStream fis = new FileInputStream(filename);
                    WorkflowType workflow = WorkflowReader.read(fis);
                    fis.close();

                    String data = new String(Files.readAllBytes(Paths.get(file.getPropertiesFilename())), "UTF-8");

                    // Read author
                    TalendUserType author = extractAuthor(talendFiles, data);

                    jsonWorkflows.addWorkflow(file.getPath(), file.getName(), file.getVersion(), readCreationDate(data), readModificationDate(data), author, workflow.getServices());
                } catch (Exception e) {
                    LOG.error("Error writing process stats for proc " + file.getName() + " (ignoring file): " + e.getMessage());
                }
            }

            writeJson("workflows.json", jsonWorkflows);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}