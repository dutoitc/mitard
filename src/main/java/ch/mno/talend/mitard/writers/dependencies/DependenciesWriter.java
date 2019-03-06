package ch.mno.talend.mitard.writers.dependencies;

import ch.mno.talend.mitard.data.AbstractNodeType;
import ch.mno.talend.mitard.data.CTalendJobType;
import ch.mno.talend.mitard.data.Context;
import ch.mno.talend.mitard.data.ProcessType;
import ch.mno.talend.mitard.data.TBonitaInstanciateProcessType;
import ch.mno.talend.mitard.data.TESBConsumerType;
import ch.mno.talend.mitard.data.TRunJobType;
import ch.mno.talend.mitard.data.TalendFile;
import ch.mno.talend.mitard.data.TalendFiles;
import ch.mno.talend.mitard.data.WorkflowType;
import ch.mno.talend.mitard.readers.ProcessReader;
import ch.mno.talend.mitard.readers.WorkflowReader;
import ch.mno.talend.mitard.tools.DotWrapper;
import ch.mno.talend.mitard.writers.AbstractWriter;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Produce dependencies.json (@see DependenciesJSON), dependencies.dot and generate dependencies.png
 */
public class DependenciesWriter extends AbstractWriter {

    public static final Pattern PAT_REFERENCE_JOB_ID = Pattern.compile("referenceJobId=\"(.*?)\"");
    public static Logger LOG = LoggerFactory.getLogger(DependenciesWriter.class);

    private DependenciesData dependenciesData = new DependenciesData();

    public DependenciesWriter(Context context) {
        super(context);
    }

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

            // Build DOT
            String dot = DotBuilder.buildDot(dependenciesData);
            String dotFilename = getContext().getProductionPath() + "/data/dependencies.dot";

            // Write DOT
            try (
                    FileWriter writer = new FileWriter(new File(dotFilename))
            ) {
                IOUtils.write(dot, writer);
                writer.flush();
            }

            // DOT to PNG
            try {
                DotWrapper.generatePNG(getContext().getDotPath(), getContext().getProductionPath() + "/data/dependencies.png", dotFilename);
            } catch (RuntimeException e) {
                LOG.error("Ignoring Dot generation due to an error: " + e.getMessage());
            }

            // Write JSON
            writeJson("dependencies.json", new DependenciesJSON(dependenciesData));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void computeDependencies(TalendFiles talendFiles) throws IOException, SAXException, ParserConfigurationException {
        // 1st-pass: keep latests versions, processes by id, dependencies
        Context context = getContext();

        // Processes: init processDependencies, store processesById
        for (TalendFile f : filterBlacklisted(talendFiles.getProcesses())) {
            try {
                DependencyKey processKey = DependencyKey.buildProcessKey(f.getName(), f.getVersion(), context);
                dependenciesData.addProcess(processKey, f);             // Read id for services which are linked to processes
            } catch (Exception e) {
                LOG.error("Error writing process dependencies for process " + f.getName() + " (ignoring file): " + e.getMessage());
            }
        }

        // Routes: init routeDependencies, keep latestsVersions, add processDependencies (route->process)
        for (TalendFile f : filterBlacklisted(talendFiles.getRoutes())) {
            try {
                DependencyKey routeKey = DependencyKey.buildRouteKey(f.getName(), f.getVersion(), context);
                dependenciesData.addRoute(routeKey, f);

                FileInputStream fis = new FileInputStream(f.getItemFilename());
                // TODO: pour chaque node, lire SELECTED_JOB_NAME et SELECTED_JOB_NAME:PROCESS_TYPE_VERSION, faire le lien

                ProcessType process = ProcessReader.read(fis);
                for (AbstractNodeType node : process.getNodeList()) {
                    if (node instanceof CTalendJobType) {
                        // Route calling job (process)
                        String processName = ((CTalendJobType) node).getProcessName();
                        String version = ((CTalendJobType) node).getProcessVersion();
                        if (version.startsWith("context"))
                            version = getContext().getProjectProperties(version.substring(8));
                        if (version.toLowerCase().equals("latest"))
                            version = dependenciesData.getLatestProcessVersion(processName);
                        DependencyKey processKey = DependencyKey.buildProcessKey(processName, version, context);
                        dependenciesData.addProcessDependency(routeKey, processKey);
                    }
                }
            } catch (Exception e) {
                LOG.error("Error writing process stats for route " + f.getName() + " (ignoring file): " + e.getMessage());
            }
        }

        // Services: init serviceDependencies, latestsVersions, processDependency (service->process)
        for (TalendFile f : filterBlacklisted(talendFiles.getServices())) {
            DependencyKey serviceKey = DependencyKey.buildServiceKey(f.getName(), f.getVersion(), context);
            dependenciesData.addService(serviceKey, f);

            // Services -> P_xxx (implementing process)
            String properties = IOUtils.toString(new FileInputStream(f.getItemFilename()));
            Matcher matcherItem = PAT_REFERENCE_JOB_ID.matcher(properties);
            if (matcherItem.find()) {
                // Note: some external services could be defined, to call for example services on the internet, which of course are not implemented in the workspace !
                String fileId = matcherItem.group(1);
                DependencyKey processKey = dependenciesData.findProcessKeyById(fileId);
                if (processKey == null) {
                    System.err.println("Warning: Cannot find process " + fileId + " called in service " + f.getName());
                } else {
                    dependenciesData.addProcessDependency(serviceKey, processKey);
                }
            }
        }

        // Workflow: add dependency workflow->service
        for (TalendFile f : filterBlacklisted(talendFiles.getMDMWorkflowProc())) {
            try {
                String filename = f.getProcFilename();
                DependencyKey workflowKey = DependencyKey.buildWorkflowKey(f.getName(), f.getVersion(), context);

                FileInputStream fis = new FileInputStream(filename);
                WorkflowType workflow = WorkflowReader.read(fis);
                fis.close();

                for (String service : workflow.getServices()) {
                    String latestService = dependenciesData.findLatestService(service);
                    dependenciesData.addWorkflowDependency(workflowKey, latestService);
                }
                // TODO: workflow->job dependencies
            } catch (Exception e) {
                LOG.error("Error writing process stats for proc " + f.getName() + " (ignoring file): " + e.getMessage());
            }
        }


        // Process: add dependencies
        for (TalendFile f : filterBlacklisted(talendFiles.getProcesses())) {
            try {
//            if (file.getItemFilename().contains("process")) {
                DependencyKey processKey = DependencyKey.buildProcessKey(f.getName(), f.getVersion(), context);

                LOG.debug("Reading " + new File(f.getItemFilename()).getName());
                FileInputStream fis = new FileInputStream(f.getItemFilename());
                ProcessType process = ProcessReader.read(fis);

                for (AbstractNodeType node : process.getNodeList()) {
                    // TODO: call by TREST* ?
                    if (node instanceof TESBConsumerType) {
                        // TESBConsumerType: process -> service
                        String name = processKey.getNormalized();
                        String serviceName1 = ((TESBConsumerType) node).getServiceName();
                        String version = dependenciesData.getLatestServiceVersion(serviceName1);
                        DependencyKey serviceKey = DependencyKey.buildServiceKey(serviceName1, version, context);
                        if (f.getName().equals(serviceName1) || f.getName().equals(serviceName1 + "Operation")) {
                            LOG.debug("Skipping P->S with same name and version:" + name);
                        } else {
                            dependenciesData.addProcessDependency(processKey, serviceKey);
                        }

                        // Deactivated: the link service -> implementing process is more interesting
//                } else if (node instanceof TESBProviderRequestType) {
//                    String name = "P_" + normalize(file.getName(), file.getVersion());
//                    String serviceName1 = ((TESBProviderRequestType) node).getServiceName();
//                    String version = getLatestVersion(latestsVersions, "S_"+serviceName1);
//                    String serviceName = "S_" + normalize(serviceName1, version);
//                    addProcessDependency(name, serviceName);

                    } else if (node instanceof TBonitaInstanciateProcessType) {
                        // TBonitaInstanciateProcessType: process -> workflow
                        String processName = ((TBonitaInstanciateProcessType) node).getProcessName();
                        if (processName.startsWith("context.")) {
                            processName = getContext().getProjectProperties(processName.substring(8));
                        }
                        String version = ((TBonitaInstanciateProcessType) node).getProcessVersion();
                        if (version != null && version.startsWith("context"))
                            version = getContext().getProjectProperties(version.substring(8));
                        if (version != null && version.toLowerCase().equals("latest"))
                            version = dependenciesData.getLatestServiceVersion(processName);
                        if (processName!=null) {
                            DependencyKey workflowKey = DependencyKey.buildWorkflowKey(processName, version, context);
                            dependenciesData.addProcessDependency(processKey, workflowKey);
                        }

                    } else if (node instanceof TRunJobType) {
                        // TRunJobType: process -> process
                        String processName1 = ((TRunJobType) node).getProcessName();
                        String version = ((TRunJobType) node).getProcessVersion();
                        if (version.startsWith("context"))
                            version = getContext().getProjectProperties(version.substring(8));
                        if (version.toLowerCase().equals("latest"))
                            version = dependenciesData.getLatestProcessVersion(processName1);
                        DependencyKey processKey2 = DependencyKey.buildProcessKey(processName1, version, context);
                        dependenciesData.addProcessDependency(processKey, processKey2);

//                    } else  if (node instanceof TRestRequestType) {
//                        String name = file.getName();
//                        String serviceName = ((TRestRequestType)node).getServiceName();
//                        System.out.println("   P_" + name + "->S_" + serviceName);
                    }
                }

            } catch (Exception e) {
                LOG.error("Error writing process stats for process(2) " + f.getName() + " (ignoring file): " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public DependenciesData getDependenciesData() {
        return dependenciesData;
    }
}