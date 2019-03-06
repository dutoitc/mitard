package ch.mno.talend.mitard.writers.dependencies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Object representing process (jobs), routes and services dependencies.
 * Key is X_Y_Z where X is P for process, S for service, R for route, Y is name, Z is version. Example: S_Airport_0_1 for Service Airport 0.1.
 * Value is same as key (but as list, as for example a job can call many subjobs)
 */
public class DependenciesJSON {

    private Map<String, List<String>> processDependencies = new HashMap<>();
    private Map<String, List<String>> routeDependencies = new HashMap<>();
    private Map<String, List<String>> serviceDependencies = new HashMap<>();


    public DependenciesJSON(DependenciesData dependenciesData) {
        this.processDependencies = dependenciesData.getProcessDependencies();
        this.routeDependencies = dependenciesData.getRouteDependencies();
        this.serviceDependencies = dependenciesData.getServiceDependencies();
        // TODO: WorkflowDependendies
    }

    public Map<String, List<String>> getProcessDependencies() {
        return processDependencies;
    }

    public void setProcessDependencies(Map<String, List<String>> processDependencies) {
        this.processDependencies = processDependencies;
    }

    public Map<String, List<String>> getRouteDependencies() {
        return routeDependencies;
    }

    public void setRouteDependencies(Map<String, List<String>> routeDependencies) {
        this.routeDependencies = routeDependencies;
    }

    public Map<String, List<String>> getServiceDependencies() {
        return serviceDependencies;
    }

    public void setServiceDependencies(Map<String, List<String>> serviceDependencies) {
        this.serviceDependencies = serviceDependencies;
    }

}