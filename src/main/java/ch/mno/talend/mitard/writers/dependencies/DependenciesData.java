package ch.mno.talend.mitard.writers.dependencies;

import ch.mno.talend.mitard.data.TalendFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dutoitc on 04.03.2019.
 */
public class DependenciesData {

    private Map<String, List<String>> processDependencies = new HashMap<>();
    private Map<String, List<String>> routeDependencies = new HashMap<>();
    private Map<String, List<String>> serviceDependencies = new HashMap<>();
    private Map<String, List<String>> workflowDependencies = new HashMap<>();
    private Map<String, String> latestsVersions = new HashMap<>();
    private Map<String, DependencyKey> processesById = new HashMap<>();
    private Map<String, TalendFile> talendFileByNormalized = new HashMap<>();

    public Map<String, List<String>> getProcessDependencies() {
        return processDependencies;
    }

    public Map<String, List<String>> getRouteDependencies() {
        return routeDependencies;
    }

    public Map<String, List<String>> getServiceDependencies() {
        return serviceDependencies;
    }

    public Map<String, List<String>> getWorkflowDependencies() {
        return workflowDependencies;
    }


    public TalendFile findTalendFile(String normalizedName) {
        return talendFileByNormalized.get(normalizedName);
    }


    public TalendFile guessTalendFileLatestVersion(String name) {
        String shortName = name.substring(0, name.lastIndexOf('_'));
        shortName = shortName.substring(0, shortName.lastIndexOf('_'));
        for (String itName:talendFileByNormalized.keySet()) {
            String itNameShort = itName.substring(0, itName.lastIndexOf('_'));
            itNameShort = itNameShort.substring(0, itNameShort.lastIndexOf('_'));
            if (shortName.equals(itNameShort)) return talendFileByNormalized.get(itName); // Only one version stored in map means latest is stored
        }

        return talendFileByNormalized.get(name);
    }


    /**
     *
     * @param key
     */
    public void addService(DependencyKey key, TalendFile f) {
        serviceDependencies.put(key.getNormalized(), new ArrayList());
        latestsVersions.put(key.getTypeName().toLowerCase(), key.getVersion());
        talendFileByNormalized.put(key.getNormalized(), f);
    }

    public void addProcess(DependencyKey processKey, TalendFile f) throws IOException {
        processDependencies.put(processKey.getNormalized(), new ArrayList());
        latestsVersions.put(processKey.getTypeName().toLowerCase(), processKey.getVersion());
        processesById.put(f.readPropertiesFileId(), processKey);
        talendFileByNormalized.put(processKey.getNormalized(), f);
    }

    public void addRoute(DependencyKey routeKey, TalendFile f) {
        routeDependencies.put(routeKey.getNormalized(), new ArrayList());
        latestsVersions.put(routeKey.getTypeName().toLowerCase(), routeKey.getVersion());
        talendFileByNormalized.put(routeKey.getNormalized(), f);
    }


//    private void addWorkflowDependency(String process, String dep) {
//        addDependency(workflowDependencies, process, dep);
//    }


    private boolean isBetter(String id1, String reference, String candidate) {
        if (candidate.equals("latest")) return true;
        int p = id1.length() + 1;
        String v1 = reference.substring(p);
        String v2 = candidate.substring(p);
        v1 = v1.replace("_", ".");
        v2 = v2.replace("_", ".");
        if (v2.charAt(0) <= '0' || v2.charAt(0) >= '9') return false;
        if (v1.charAt(0) <= '0' || v2.charAt(0) >= '9') return false;
        try {
            float v1f = Float.parseFloat(v1);
            float v2f = Float.parseFloat(v2);
            return v2f > v1f;
        } catch (Exception e) {
            return false;
        }
    }

    String findLatestService(String service) {
        if (service.endsWith("Operation")) {
            service = service.substring(0, service.length()-"Operation".length());
        }
        String needle = "S_" + service; // TODO: normalize ?
        String best = "";
        for (String key : serviceDependencies.keySet()) {
            if (key.startsWith(needle) && (best == "" || isBetter(needle, best, key))) {
                best = key;
            }
        }
        return best;
    }

    String getLatestVersion(String id) {
        String value = latestsVersions.get(id.toLowerCase());
        if (value == null) {
            return "latest";
        } else {
            return value;
        }
    }


    public DependencyKey findProcessKeyById(String fileId) {
        return processesById.get(fileId);
    }



    void addDependency(Map<String, List<String>> dependencies, String process, String dep) {
        if (dep.contains("globalMap")) {
            System.err.println("Ignoring dependency on " + dep);
            return;
        }
        if (dependencies.containsKey(process)) {
            dependencies.get(process).add(dep);
        } else {
            List<String> list = new ArrayList<>();
            list.add(dep);
            dependencies.put(process, list);
        }
    }

//    private void addProcessDependency(String process, String dep) {
//        addDependency(processDependencies, process, dep);
//    }

    public void addProcessDependency(DependencyKey key1, DependencyKey key2) {
        addDependency(processDependencies, key1.getNormalized(), key2.getNormalized());
    }

    public void addProcessDependency(DependencyKey key1, String key2) {
        addDependency(processDependencies, key1.getNormalized(), key2);
    }

    public void addWorkflowDependency(DependencyKey key1, DependencyKey key2) {
        addDependency(workflowDependencies, key1.getNormalized(), key2.getNormalized());
    }

    public void addWorkflowDependency(DependencyKey key1, String key2) {
        addDependency(workflowDependencies, key1.getNormalized(), key2);
    }

    public String getLatestProcessVersion(String processName) {
        return getLatestVersion("P_"+processName);
    }

    public String getLatestServiceVersion(String processName) {
        return getLatestVersion("S_"+(""+processName.charAt(0)).toUpperCase()+processName.substring(1));
    }



//    void addWorkflowDependency(String process, String dep) {
//        addDependency(workflowDependencies, process, dep);
//    }

}
