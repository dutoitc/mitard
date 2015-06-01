package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.*;
import ch.mno.talend.mitard.out.JSonStatistics;
import ch.mno.talend.mitard.readers.ProcessReader;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dutoitc on 13.05.2015.
 */
public class StatisticsWriter extends AbstractWriter {

    public StatisticsWriter(Context context) {
        super(context);
    }

    // TODO: nb codelines, ...
    public void write(TalendFiles talendFiles) throws IOException, ParserConfigurationException, SAXException {
        Map<String, Integer> componentTypes = new HashMap<>();
        //
        for (TalendFile file : talendFiles.getProcesses()) {
            if (isBlacklisted(file.getName())|| isBlacklisted(file.getPath())) continue;
            System.out.println("Reading " + new File(file.getItemFilename()).getName());
            FileInputStream fis = new FileInputStream(file.getItemFilename());
            ProcessType process = ProcessReader.reader(fis);
            for (AbstractNodeType node : process.getNodeList()) {
//                    if (node.isActive()) {
                if (componentTypes.containsKey(node.getComponentName())) {
                    componentTypes.put(node.getComponentName(), componentTypes.get(node.getComponentName()) + 1);
                } else {
                    componentTypes.put(node.getComponentName(), 1);
                }
            }
        }

        int nbProcesses=0;
        int nbRoutes=0;
        int nbServices = 0;
        for (TalendFile file : talendFiles.getProcesses()) {
            if (isBlacklisted(file.getName())|| isBlacklisted(file.getPath())) continue;
            nbProcesses++;
        }
        for (TalendFile file : talendFiles.getRoutes()) {
            if (isBlacklisted(file.getName())|| isBlacklisted(file.getPath())) continue;
            nbRoutes++;
        }
        for (TalendFile file : talendFiles.getServices()) {
            if (isBlacklisted(file.getName())|| isBlacklisted(file.getPath())) continue;
            nbServices++;
        }


        JSonStatistics stats = new JSonStatistics();
        stats.setNbProcesses(nbProcesses);
        stats.setNbRoutes(nbRoutes);
        stats.setNbServices(nbServices);
        stats.setComponentCounts(componentTypes);
        // TODO: number of components of each type
        writeJson("statistics.json", stats);
    }


}
