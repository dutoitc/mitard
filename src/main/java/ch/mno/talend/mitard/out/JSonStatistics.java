package ch.mno.talend.mitard.out;

import java.util.Map;

public class JSonStatistics {
    private int nbRoutes;
    private int nbServices;
    private int nbProcesses;
    private Map<String, Integer> componentCounts;
    private long lastUpdate;
    private int nbWorkflows;

    public JSonStatistics() {
    }

    public int getNbRoutes() {
        return this.nbRoutes;
    }

    public void setNbRoutes(int nbRoutes) {
        this.nbRoutes = nbRoutes;
    }

    public int getNbServices() {
        return this.nbServices;
    }

    public void setNbServices(int nbServices) {
        this.nbServices = nbServices;
    }

    public int getNbProcesses() {
        return this.nbProcesses;
    }

    public void setNbProcesses(int nbProcesses) {
        this.nbProcesses = nbProcesses;
    }

    public void setComponentCounts(Map<String, Integer> componentCounts) {
        this.componentCounts = componentCounts;
    }

    public Map<String, Integer> getComponentCounts() {
        return componentCounts;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setNbWorkflows(int nbWorkflows) {
        this.nbWorkflows = nbWorkflows;
    }

    public int getNbWorkflows() {
        return nbWorkflows;
    }
}
