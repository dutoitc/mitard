package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.*;
import ch.mno.talend.mitard.readers.ProcessReader;
import ch.mno.talend.mitard.tools.DotHelper;
import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DependenciesWriter extends AbstractWriter {
    public DependenciesWriter(Context context) {
        super(context);
    }

    private Map<String, List<String>> processDependencies = new HashMap<>();
    private Map<String, List<String>> routeDependencies = new HashMap<>();
    private Map<String, List<String>> serviceDependencies = new HashMap<>();


    /**
     * produce dependencies.dot, dependencies.png depending on Graphviz'dot
     *
     * @throws JAXBException
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public void write(TalendFiles talendFiles) throws JAXBException, IOException, SAXException, ParserConfigurationException {
        computeDependencies(talendFiles);

        // DOT
        String dot = buildDot();
        File tempFile = new File(getContext().getProductionPath() + File.separatorChar + "data" + File.separatorChar + "dependencies.dot");
        FileWriter writer = new FileWriter(tempFile);
        IOUtils.write(dot, writer);
        writer.flush();
        try {
            DotHelper.generatePNG(getContext().getDotPath(), getContext().getProductionPath() + File.separatorChar + "data" + File.separatorChar + "dependencies.png", tempFile);
        } catch (RuntimeException e) {
            System.err.println("Ignoring Dot generation due to an error: " + e.getMessage());
        }

        // JSON
        writeJson("dependencies.json", new DependenciesJSON(processDependencies, routeDependencies, serviceDependencies));

        //System.out.println(sb.toString());
    }


    private String normalize(String name) {
        return name.replace("-", "_");
    }

    private void addProcessDependency(String process, String dep) {
        if (processDependencies.containsKey(process)) {
            processDependencies.get(process).add(dep);
        } else {
            List<String> list = new ArrayList<>();
            list.add(dep);
            processDependencies.put(process, list);
        }
    }

    private void addRoutesDependency(String process, String dep) {
        if (routeDependencies.containsKey(process)) {
            routeDependencies.get(process).add(dep);
        } else {
            List<String> list = new ArrayList<>();
            list.add(dep);
            routeDependencies.put(process, list);
        }
    }

    private void addServiceDependency(String process, String dep) {
        if (serviceDependencies.containsKey(process)) {
            serviceDependencies.get(process).add(dep);
        } else {
            List<String> list = new ArrayList<>();
            list.add(dep);
            serviceDependencies.put(process, list);
        }
    }

    private void computeDependencies(TalendFiles talendFiles) throws IOException, SAXException, ParserConfigurationException {
        for (TalendFile f : talendFiles.getProcesses()) {
            if (isBlacklisted(f.getName())) continue;
            String name = "P_" + normalize(f.getName());
            processDependencies.put(name, new ArrayList<String>());
        }
        for (TalendFile f : talendFiles.getRoutes()) {
            if (isBlacklisted(f.getName())) continue;
            String name = "R_" + normalize(f.getName());
            routeDependencies.put(name, new ArrayList<String>());
        }
        for (TalendFile f : talendFiles.getServices()) {
            if (isBlacklisted(f.getName())) continue;
            String name = "S_" + normalize(f.getName());
            serviceDependencies.put(name, new ArrayList<String>());
        }


        for (TalendFile file : talendFiles.getProcesses()) {
//            if (file.getItemFilename().contains("process")) {
            if (isBlacklisted(file.getName())) continue;
            System.out.println("Reading " + new File(file.getItemFilename()).getName());
            FileInputStream fis = new FileInputStream(file.getItemFilename());
            ProcessType process = ProcessReader.reader(fis);
            for (AbstractNodeType node : process.getNodeList()) {
                if (node instanceof TESBConsumerType) {
                    String name = "P_" + normalize(file.getName());
                    String serviceName = "S_" + normalize(((TESBConsumerType) node).getServiceName());
                    addProcessDependency(name, serviceName);
                } else if (node instanceof TESBProviderRequestType) {
                    String name = "P_" + normalize(file.getName());
                    String serviceName = "S_" + normalize(((TESBProviderRequestType) node).getServiceName());
                    addProcessDependency(name, serviceName);
                } else if (node instanceof TRunJobType) {
                    String name = "P_" + normalize(file.getName());
                    String processName = "P_" + normalize(((TRunJobType) node).getProcessName());
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
        }



    }

    private String buildDot() {
        StringBuffer sb = new StringBuffer();
        sb.append("digraph G{\r\n");
        for (String name : processDependencies.keySet()) {
            sb.append("   " + name + " [shape=box, style=filled, fillcolor=green, color=green, label=\"" + name + "\"];\r\n");
        }
        for (String name : routeDependencies.keySet()) {
            sb.append("   " + name + " [shape=trapezium, style=filled, fillcolor=green, color=green, label=\"" + name + "\"];\r\n");
        }
        for (String name : serviceDependencies.keySet()) {
            sb.append("   " + name + " [shape=ellipse, style=filled, fillcolor=green, color=green, label=\"" + name + "\"];\r\n");
        }

        for (Map.Entry<String, List<String>> entry : processDependencies.entrySet()) {
            for (String target : entry.getValue()) {
                sb.append("   " + entry.getKey() + "->" + target + "\r\n");
            }
        }
        for (Map.Entry<String, List<String>> entry : routeDependencies.entrySet()) {
            for (String target : entry.getValue()) {
                sb.append("   " + entry.getKey() + "->" + target + "\r\n");
            }
        }
        for (Map.Entry<String, List<String>> entry : serviceDependencies.entrySet()) {
            for (String target : entry.getValue()) {
                sb.append("   " + entry.getKey() + "->" + target + "\r\n");
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