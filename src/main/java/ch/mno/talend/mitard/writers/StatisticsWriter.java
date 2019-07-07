package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.AbstractNodeType;
import ch.mno.talend.mitard.data.Context;
import ch.mno.talend.mitard.data.ProcessType;
import ch.mno.talend.mitard.data.TalendFile;
import ch.mno.talend.mitard.data.TalendFiles;
import ch.mno.talend.mitard.out.JSonStatistics;
import ch.mno.talend.mitard.readers.ProcessReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dutoitc on 13.05.2015.
 */
public class StatisticsWriter extends AbstractWriter {


    public static Logger LOG = LoggerFactory.getLogger(StatisticsWriter.class);

    public StatisticsWriter(Context context) {
        super(context);
    }

    // TODO: nb codelines, ...
    public void write(TalendFiles talendFiles) {
        try {
            Map<String, Integer> componentTypes = new HashMap<>();
            //
            for (TalendFile file : filterBlacklisted(talendFiles.getProcesses())) {
                try {
                    LOG.debug("Reading " + new File(file.getItemFilename()).getName());
                    FileInputStream fis = new FileInputStream(file.getItemFilename());
                    ProcessType process = ProcessReader.read(fis);
                    for (AbstractNodeType node : process.getNodeList()) {
                        if (node.isActive()) {
                            if (componentTypes.containsKey(node.getComponentName())) {
                                componentTypes.put(node.getComponentName(), componentTypes.get(node.getComponentName()) + 1);
                            } else {
                                componentTypes.put(node.getComponentName(), 1);
                            }
                        }
                    }
                } catch (Exception e) {
                    LOG.error("Error writing process stats for " + file.getName() + " (ignoring file): " + e.getMessage());
                }
            }

            JSonStatistics stats = new JSonStatistics();
            stats.setNbProcesses(filterBlacklisted(talendFiles.getProcesses()).size());
            stats.setNbRoutes(filterBlacklisted(talendFiles.getRoutes()).size());
            stats.setNbServices(filterBlacklisted(talendFiles.getServices()).size());
            stats.setNbWorkflows(filterBlacklisted(talendFiles.getMDMWorkflowProc()).size());
            stats.setComponentCounts(componentTypes);
            stats.setLastUpdate(System.currentTimeMillis());
            // TODO: number of components of each type
            writeJson("statistics.json", stats);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}