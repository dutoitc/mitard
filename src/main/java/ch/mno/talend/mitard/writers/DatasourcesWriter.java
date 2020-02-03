package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.*;
import ch.mno.talend.mitard.readers.ProcessReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dutoitc on 13.05.2015.
 */
public class DatasourcesWriter extends AbstractWriter {


    public static Logger LOG = LoggerFactory.getLogger(DatasourcesWriter.class);

    public DatasourcesWriter(Context context) {
        super(context);
    }


    public void write(TalendFiles talendFiles) {
        try {

            UsageKeeper usageKeeper = new UsageKeeper();

            //
            for (TalendFile file : filterBlacklisted(talendFiles.getProcesses())) {
                try {
                    LOG.debug("Reading " + new File(file.getItemFilename()).getName());
                    FileInputStream fis = new FileInputStream(file.getItemFilename());
                    ProcessType process = ProcessReader.read(fis);
                    usageKeeper.handleProcess(process, file);
                } catch (Exception e) {
                    LOG.error("Error writing process stats for " + file.getName() + " (ignoring file): " + e.getMessage());
                }
            }


            // TODO: number of components of each type
            writeJson("datasources.json", usageKeeper);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private class UsageKeeper implements Serializable  {
        private Map<String, List<String>> datasourceUsage = new HashMap<>();

        // For jackson serialization
        public Map<String, List<String>> getDatasourceUsage() {
            return datasourceUsage;
        }

        // For jackson serialization
        public void setDatasourceUsage(Map<String, List<String>> datasourceUsage) {
            this.datasourceUsage = datasourceUsage;
        }

        public void handleProcess(ProcessType process, TalendFile file) {
            boolean foundComponentWithoutAlias = false;
            for (AbstractNodeType node : process.getNodeList()) {
                if (node.isActive()) {
                    if (node instanceof TOracleConnectionType) {
                        foundComponentWithoutAlias = foundComponentWithoutAlias || (!handleTOracleConnection((TOracleConnectionType) node, file));
                    } else if (node instanceof TOracleInputType) {
                        foundComponentWithoutAlias = handleTOracleInput(foundComponentWithoutAlias, (TOracleInputType) node);
                    } else if (node instanceof TOracleOutputType) {
                        foundComponentWithoutAlias = handleTOracleOutput(foundComponentWithoutAlias, (TOracleOutputType) node);
                    }
                }
            }
            if (foundComponentWithoutAlias) {
                addUsage("none", file.getProcessFullPath());
            }
        }

        private boolean handleTOracleConnection(TOracleConnectionType node, TalendFile file) {
            boolean found = false;
            TOracleConnectionType conn = node;
            if (conn.isSpecifyDatasourceAlias()) {
                addUsage(conn.getDatasourceAlias(), file.getProcessFullPath());
                found = true;
            }
            return found;
        }

        private boolean handleTOracleOutput(boolean foundComponentWithoutAlias, TOracleOutputType node) {
            TOracleOutputType inputType = node;
            if (!inputType.isUseExistingConnection()) {
                foundComponentWithoutAlias = foundComponentWithoutAlias || (!inputType.isSpecifyDatasourceAlias());
            }
            return foundComponentWithoutAlias;
        }

        private boolean handleTOracleInput(boolean foundComponentWithoutAlias, TOracleInputType node) {
            TOracleInputType inputType = node;
            if (!inputType.isUseExistingConnection()) {
                foundComponentWithoutAlias = foundComponentWithoutAlias || (!inputType.isSpecifyDatasourceAlias());
            }
            return foundComponentWithoutAlias;
        }

        private void addUsage(String datasourceAlias, String processFullPath) {
            // Normalization and properties replacment
            datasourceAlias=datasourceAlias.replaceAll("\"", "");
            if (datasourceAlias.startsWith("context")) {
                String datasourceAlias2 = getContext().getProjectProperties(datasourceAlias.substring(8));
                if (datasourceAlias2==null) {
                    System.err.println("Not found alias in context.csv:"+datasourceAlias);
                } else {
                    datasourceAlias = datasourceAlias2;
                }
            }

            if (datasourceUsage.containsKey(datasourceAlias)) {
                datasourceUsage.get(datasourceAlias).add(processFullPath);
            } else {
                List<String> processes = new ArrayList<>();
                processes.add(processFullPath);
                datasourceUsage.put(datasourceAlias, processes);
            }
        }
    }



}
