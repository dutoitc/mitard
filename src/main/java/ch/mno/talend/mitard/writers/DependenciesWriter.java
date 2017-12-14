package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.*;
import ch.mno.talend.mitard.readers.ProcessReader;
import ch.mno.talend.mitard.readers.WorkflowReader;
import ch.mno.talend.mitard.tools.DotWrapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DependenciesWriter extends AbstractWriter {


    public static Logger LOG = LoggerFactory.getLogger(DependenciesWriter.class);

    public DependenciesWriter(Context context) {
        super(context);
    }

    private Map<String, List<String>> processDependencies = new HashMap<>();
    private Map<String, List<String>> routeDependencies = new HashMap<>();
    private Map<String, List<String>> serviceDependencies = new HashMap<>();
    private Map<String, List<String>> workflowDependencies = new HashMap<>();


    /**
     * produce dependencies.dot, dependencies.png depending on Graphviz'dot
     *
     * @throws JAXBException
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public void write(TalendFiles talendFiles) {
        try {
            computeDependencies(talendFiles);

            // DOT
            String dot = buildDot();
            File tempFile = new File(getContext().getProductionPath() + File.separatorChar + "data" + File.separatorChar + "dependencies.dot");
            FileWriter writer = new FileWriter(tempFile);
            IOUtils.write(dot, writer);
            writer.flush();
            try {
                DotWrapper.generatePNG(getContext().getDotPath(), getContext().getProductionPath() + File.separatorChar + "data" + File.separatorChar + "dependencies.png", tempFile);
            } catch (RuntimeException e) {
                LOG.error("Ignoring Dot generation due to an error: " + e.getMessage());
            }

            // JSON
            writeJson("dependencies.json", new DependenciesJSON(processDependencies, routeDependencies, serviceDependencies));

            //System.out.println(sb.toString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /** Normalize name, version to [name]_[version] with replacements. '-' and '.' to' _'.*/
    private String normalize(String name, String version) {
        if (version == null) return "";

        // Convert context.xxx to the value given by xxx in context/properties file
        if (version.startsWith("context")) {
            version = getContext().getProjectProperties(version.substring(8));
        }
        String s = name + "_" + version;
        return s.replace("-", "_").replace(".", "_");
    }

    private void addProcessDependency(String process, String dep) {
        addDependency(processDependencies, process, dep);
    }

    private void addRoutesDependency(String process, String dep) {
        addDependency(routeDependencies, process, dep);
    }

    private void addServiceDependency(String process, String dep) {
        addDependency(serviceDependencies, process, dep);
    }

    private void addWorkflowDependency(String process, String dep) {
        addDependency(workflowDependencies, process, dep);
    }

    private void addDependency(Map<String, List<String>> dependencies, String process, String dep) {
        if (dependencies.containsKey(process)) {
            dependencies.get(process).add(dep);
        } else {
            List<String> list = new ArrayList<>();
            list.add(dep);
            dependencies.put(process, list);
        }
    }

    private void computeDependencies(TalendFiles talendFiles) throws IOException, SAXException, ParserConfigurationException {
        // 1st-pass: keep latests versions, processes by id, dependencies
        Map<String, String> latestsVersions = new HashMap<>();
        Map<String, String> processesById = new HashMap<>();
        for (TalendFile f : talendFiles.getProcesses()) {
            try {
                if (isBlacklisted(f.getName()) || isBlacklisted(f.getPath())) continue;
                String name = "P_" + normalize(f.getName(), f.getVersion());
                processDependencies.put(name, new ArrayList<String>());
                latestsVersions.put("P_" + f.getName(), f.getVersion());

                // Read id for services which are linked to processes
                try {
                    String properties = IOUtils.toString(new FileInputStream(f.getPropertiesFilename()));
                    Matcher matcherItem = Pattern.compile("TalendProperties:Property.*?\\ id=\"(.*?)\"").matcher(properties);
                    matcherItem.find();
                    String fileId = matcherItem.group(1);
                    processesById.put(fileId, name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                LOG.error("Error writing process dependencies for process " + f.getName() + " (ignoring file): " + e.getMessage());
            }
        }
        for (TalendFile f : talendFiles.getRoutes()) {
            try {
                if (isBlacklisted(f.getName()) || isBlacklisted(f.getPath())) continue;
                String name = "R_" + normalize(f.getName(), f.getVersion());
                routeDependencies.put(name, new ArrayList<String>());
                latestsVersions.put("R_" + f.getName(), f.getVersion());


                FileInputStream fis = new FileInputStream(f.getItemFilename());
                // TODO: pour chaque noed, lire SELECTED_JOB_NAME et SELECTED_JOB_NAME:PROCESS_TYPE_VERSION, faire le lien

                ProcessType process = ProcessReader.read(fis);
                for (AbstractNodeType node : process.getNodeList()) {
                    if (node instanceof CTalendJobType) {
                        String processName1 = ((CTalendJobType) node).getProcessName();
                        String version = ((CTalendJobType) node).getProcessVersion();
                        if (version.startsWith("context"))
                            version = getContext().getProjectProperties(version.substring(8));
                        if (version.toLowerCase().equals("latest"))
                            version = getLatestVersion(latestsVersions, "P_" + processName1);
                        String processName = "P_" + normalize(processName1, version);
                        addProcessDependency(name, processName);
                    }
                }

            } catch (Exception e) {
                LOG.error("Error writing process stats for route " + f.getName() + " (ignoring file): " + e.getMessage());
            }
        }
        for (TalendFile f : talendFiles.getServices()) {
            if (isBlacklisted(f.getName()) || isBlacklisted(f.getPath())) continue;
            String name = "S_" + normalize(f.getName(), f.getVersion());
            serviceDependencies.put(name, new ArrayList<String>());
            latestsVersions.put("S_" + f.getName(), f.getVersion());

            // Services -> P_xxx (implementing process)
            String properties = IOUtils.toString(new FileInputStream(f.getItemFilename()));
//            System.out.println(properties);
            Matcher matcherItem = Pattern.compile("referenceJobId=\"(.*?)\"").matcher(properties);
            if (matcherItem.find()) {
                // Note: some external services could be defined, to call for example services on the internet, which of course are not implemented in the workspace !
                String fileId = matcherItem.group(1);
                String process = processesById.get(fileId);
                if (process == null) {
                    System.err.println("Warning: Cannot find process " + fileId + " called in service " + f.getName());
                } else {
                    addProcessDependency(name, process);
                }
            }
        }

        for (TalendFile f : talendFiles.getMDMWorkflowProc()) {
            try {
                if (isBlacklisted(f.getName()) || isBlacklisted(f.getPath())) continue;
                String filename = f.getProcFilename();
                LOG.debug("Reading " + filename);
                FileInputStream fis = new FileInputStream(filename);
                WorkflowType workflow = WorkflowReader.read(fis);
                String source = "B_" + normalize(f.getName(), f.getVersion());

                for (String service : workflow.getServices()) {
                    String dest = findLatestService(service);
                    addWorkflowDependency(source, dest);
                }
            } catch (Exception e) {
                LOG.error("Error writing process stats for proc " + f.getName() + " (ignoring file): " + e.getMessage());
            }
        }


        for (TalendFile file : talendFiles.getProcesses()) {
            try {
//            if (file.getItemFilename().contains("process")) {
            if (isBlacklisted(file.getName()) || isBlacklisted(file.getPath())) continue;
            LOG.debug("Reading " + new File(file.getItemFilename()).getName());
            FileInputStream fis = new FileInputStream(file.getItemFilename());
            ProcessType process = ProcessReader.read(fis);
            for (AbstractNodeType node : process.getNodeList()) {
                if (node instanceof TESBConsumerType) {
                    String name = "P_" + normalize(file.getName(), file.getVersion());
                    String serviceName1 = ((TESBConsumerType) node).getServiceName();
                    String version = getLatestVersion(latestsVersions, "S_" + serviceName1);
                    String serviceName = "S_" + normalize(serviceName1, version);
                    if (file.getName().equals(serviceName1) || file.getName().equals(serviceName1+"Operation")) {
                        LOG.debug("Skipping P->S with same name and version:"+name);
                    } else {
                        addProcessDependency(name, serviceName);
                    }

                    // Deactivated: the link service -> implementing process is more interesting
//                } else if (node instanceof TESBProviderRequestType) {
//                    String name = "P_" + normalize(file.getName(), file.getVersion());
//                    String serviceName1 = ((TESBProviderRequestType) node).getServiceName();
//                    String version = getLatestVersion(latestsVersions, "S_"+serviceName1);
//                    String serviceName = "S_" + normalize(serviceName1, version);
//                    addProcessDependency(name, serviceName);

                } else if (node instanceof TBonitaInstanciateProcessType) {
                    String name = "P_" + normalize(file.getName(), file.getVersion());
                    String processName = ((TBonitaInstanciateProcessType) node).getProcessName();
                    if (processName.startsWith("context.")) {
                        processName = getContext().getProjectProperties(processName.substring(8));
                    }
                    String version = ((TBonitaInstanciateProcessType) node).getProcessVersion();
                    if (version != null && version.startsWith("context"))
                        version = getContext().getProjectProperties(version.substring(8));
                    if (version != null && version.toLowerCase().equals("latest"))
                        version = getLatestVersion(latestsVersions, "S_" + processName);
                    String serviceName = "B_" + normalize(processName, version);
                    addProcessDependency(name, serviceName);

                } else if (node instanceof TRunJobType) {
                    String name = "P_" + normalize(file.getName(), file.getVersion());
                    String processName1 = ((TRunJobType) node).getProcessName();
                    String version = ((TRunJobType) node).getProcessVersion();
                    if (version.startsWith("context"))
                        version = getContext().getProjectProperties(version.substring(8));
                    if (version.toLowerCase().equals("latest"))
                        version = getLatestVersion(latestsVersions, "P_" + processName1);
                    String processName = "P_" + normalize(processName1, version);
                    addProcessDependency(name, processName);
//                    } else  if (node instanceof TBonitaInstantiateProcessType) {
////                        <elementParameter field="TEXT" name="PROCESS_NAME" value="&quot;AUDIT_instanciationBPM&quot;"/>
//                        String name = "P_"+normalize(file.getName());
//                        String processName = "P_"+normalize(((TRunJobType) node).getProcessName());
//                        addProcessDependency(name, processName);

//                    } else  if (node instanceof TRestRequestType) {
//                        String name = file.getName();
//                        String serviceName = ((TRestRequestType)node).getServiceName();
//                        System.out.println("   P_" + name + "->S_" + serviceName);
                }
            }

            } catch (Exception e) {
                LOG.error("Error writing process stats for process(2) " + file.getName() + " (ignoring file): " + e.getMessage());
            }
        }
    }

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

    private String findLatestService(String service) {
        String needle = "S_" + service;
        String best = "";
        for (String key : serviceDependencies.keySet()) {
            if (key.startsWith(needle) && (best == "" || isBetter(needle, best, key))) {
                best = key;
            }
        }
        return best;
    }

    private String getLatestVersion(Map<String, String> latestsVersions, String id) {
        String value = latestsVersions.get(id);
        if (value == null) {
            return "latest";
        } else {
            return value;
        }
    }

    private String buildDot() {
        StringBuffer sb = new StringBuffer();
        sb.append("digraph G{\r\n");
//        sb.append("graph [rankdir=LR, fontsize=10, margin=0.001];\r\n");

        // Count number of relations
        Map<String, Integer> count = new HashMap<>();
        List<Map<String, List<String>>> sources = new ArrayList<>();
        sources.add(processDependencies);
        sources.add(routeDependencies);
        sources.add(serviceDependencies);
        sources.add(workflowDependencies);
        for (Map<String, List<String>> source: sources) {
            for (Map.Entry<String, List<String>> entry : source.entrySet()) {
                if (count.containsKey(entry.getKey())) {
                    count.put(entry.getKey(), count.get(entry.getKey())+1);
                } else {
                    count.put(entry.getKey(), 1);
                }

                for (String target : entry.getValue()) {
                    if (count.containsKey(target)) {
                        count.put(target, count.get(target)+1);
                        count.put(entry.getKey(), count.get(entry.getKey())+1);
                    } else {
                        count.put(target, 2); // Count link to avoid orphan
                        count.put(entry.getKey(), count.get(entry.getKey())+1);
                    }
                }
            }
        }

        // Boxes
        for (Map<String, List<String>> source: sources) {
            String shape="box";
            boolean rankSame=false;
            if (source==processDependencies) {
                shape="box";
            }
            if (source==routeDependencies) {
                shape="trapezium";
                rankSame=true;
            }
            if (source==serviceDependencies) {
                shape="ellipse";
            }
            if (source==workflowDependencies) {
                shape="house";
                rankSame=true;
            }

            if (rankSame) sb.append("{ rank=same; \n");
            for (String name : source.keySet()) {
                if (count.get(name)>1)
                    sb.append("   " + name + " [shape=").append(shape).append(", style=filled, fillcolor=green, color=green, label=\"" + name + "\"];\r\n");
            }
            if (rankSame) sb.append("}\n");
        }

        // Boxes
        /*for (String name : processDependencies.keySet()) {
            if (count.get(name)>1)
            sb.append("   " + name + " [shape=box, style=filled, fillcolor=green, color=green, label=\"" + name + "\"];\r\n");
        }
        sb.append("{ rank=same; \n");
        for (String name : routeDependencies.keySet()) {
            if (count.get(name)>1)
            sb.append("   " + name + " [shape=trapezium, style=filled, fillcolor=green, color=green, label=\"" + name + "\"];\r\n");
        }
        sb.append("}\n");
        for (String name : serviceDependencies.keySet()) {
            if (count.get(name)>1)
            sb.append("   " + name + " [shape=ellipse, style=filled, fillcolor=green, color=green, label=\"" + name + "\"];\r\n");
        }
        for (String name : workflowDependencies.keySet()) {
            if (count.get(name)>1)
            sb.append("   " + name + " [shape=house, style=filled, fillcolor=green, color=green, label=\"" + name + "\"];\r\n");
        }*/

        // Isolated boxes / orpheans
        sb.append("subgraph cluster_orphans {\r\n");
        sb.append("rankdir=LR\r\n");
        for (String name : processDependencies.keySet()) {
            if (count.get(name)==1)
                sb.append("   " + name + " [shape=box, style=filled, fillcolor=green, color=green, label=\"" + name + "\"];\r\n");
        }
        for (String name : routeDependencies.keySet()) {
            if (count.get(name)==1)
                sb.append("   " + name + " [shape=trapezium, style=filled, fillcolor=green, color=green, label=\"" + name + "\"];\r\n");
        }
        for (String name : serviceDependencies.keySet()) {
            if (count.get(name)==1)
                sb.append("   " + name + " [shape=ellipse, style=filled, fillcolor=green, color=green, label=\"" + name + "\"];\r\n");
        }
        for (String name : workflowDependencies.keySet()) {
            if (count.get(name)==1)
                sb.append("   " + name + " [shape=house, style=filled, fillcolor=green, color=green, label=\"" + name + "\"];\r\n");
        }
        sb.append("}\r\n");




        for (Map<String, List<String>> source: sources) {
            for (Map.Entry<String, List<String>> entry : source.entrySet()) {
                for (String target : entry.getValue()) {
                    if (!StringUtils.isEmpty(target)) {
                        sb.append("   " + entry.getKey() + "->" + target + "\r\n");
                    }
                }
            }
        }

        sb.append("}\n");
        return sb.toString();
    }


    private class DependenciesJSON {


        private Map<String, List<String>> processDependencies = new HashMap<>();
        private Map<String, List<String>> routeDependencies = new HashMap<>();
        private Map<String, List<String>> serviceDependencies = new HashMap<>();

        public DependenciesJSON(Map<String, List<String>> processDependencies, Map<String, List<String>> routeDependencies, Map<String, List<String>> serviceDependencies) {
            this.processDependencies = processDependencies;
            this.routeDependencies = routeDependencies;
            this.serviceDependencies = serviceDependencies;
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
}