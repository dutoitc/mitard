package ch.mno.talend.mitard.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by dutoitc on 13.05.2015.
 */
public class TalendFiles {

    private List<TalendFile> processes;
    private List<TalendFile> routes;
    private List<TalendFile> services;
    private List<TalendFile> mdmWorkflowProc;

    public List<TalendFile> getProcesses() {
        return processes;
    }

    public void setProcesses(List<TalendFile> processes) {
        this.processes = processes;
    }

    public List<TalendFile> getRoutes() {
        return routes;
    }

    public void setRoutes(List<TalendFile> routes) {
        this.routes = routes;
    }

    public List<TalendFile> getServices() {
        return services;
    }

    public void setServices(List<TalendFile> services) {
        this.services = services;
    }

    public void setMDMWorkflowProc(List<TalendFile> MDMWorkflowProc) {
        this.mdmWorkflowProc = MDMWorkflowProc;
    }

    public List<TalendFile> getMDMWorkflowProc() {
        return mdmWorkflowProc;
    }

    public List<TalendFile> getUnstableFiles() {
        List<TalendFile> files = new ArrayList<>();

        Predicate<TalendFile> filesMissing = talendFile->
                !new File(talendFile.getItemFilename()).exists() ||
                        !new File(talendFile.getPropertiesFilename()).exists() ||
                        !new File(talendFile.getScreenshotFilename()).exists();

        files.addAll(processes.stream().filter(filesMissing).collect(Collectors.toList()));
        files.addAll(routes.stream().filter(filesMissing).collect(Collectors.toList()));
        files.addAll(services.stream().filter(filesMissing).collect(Collectors.toList()));
        return files;
    }
}
