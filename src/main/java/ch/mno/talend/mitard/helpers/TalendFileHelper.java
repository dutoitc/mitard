package ch.mno.talend.mitard.helpers;

import ch.mno.talend.mitard.data.TalendFile;
import ch.mno.talend.mitard.data.TalendFiles;
import ch.mno.talend.mitard.readers.TalendProjectReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TalendFileHelper {

    private static final Logger LOG = LoggerFactory.getLogger(TalendFileHelper.class);

    public TalendFileHelper() {
    }

    /** Find latest process, routes, services versions in workspace path subfolders */
    public static TalendFiles findLatestVersions(String talendWorkspacePath) throws IOException {
        TalendFiles talendFiles = new TalendFiles();
        talendFiles.setProcesses(findLatestVersionsInternal(new File(talendWorkspacePath + File.separatorChar+"process")));
        talendFiles.setRoutes(findLatestVersionsInternal(new File(talendWorkspacePath + File.separatorChar + "routes")));
        talendFiles.setServices(findLatestVersionsInternal(new File(talendWorkspacePath + File.separatorChar + "services")));
        talendFiles.setMDMWorkflowProc(findLatestVersionsInternal(new File(talendWorkspacePath + File.separatorChar + "MDM" + File.separatorChar + "workflow")));
        talendFiles.setProject(TalendProjectReader.read(new FileInputStream(talendWorkspacePath + "/talend.project")));
        return talendFiles;
    }

    private static List<TalendFile> findLatestVersionsInternal(File folder) {
        if (!folder.exists()) return new ArrayList<TalendFile>();

        try {
            HashMap data = new HashMap();

            // Folders
            Files.list(folder.toPath())
                    .filter(pathname -> pathname.toFile().isDirectory() && !pathname.toFile().getName().startsWith("/") && !pathname.toFile().getName().startsWith("."))
                    .forEach(file -> {
                        // Keep subfiles recursively if first or greater version
                        findLatestVersionsInternal(file.toFile())
                                .stream()
                                .filter(subfile -> !data.containsKey(subfile.getName()) || ((TalendFile) data.get(subfile.getName())).isVersionLowerThan(subfile))
                                .forEach(subfile -> data.put(subfile.getName(), subfile));
                    });

            // Files
            Pattern patFilename = Pattern.compile("(.*)_(\\d+\\.\\d+)\\.item");
            Files.list(folder.toPath())
                    .filter(path -> path.toFile().isFile() && path.toFile().getName().endsWith(".item"))
                    .forEach(file -> {
                        String filename = file.toFile().getName();       // XXX_123.456.item
                        Matcher matcher = patFilename.matcher(filename);
                        if (matcher.matches()) {
                            String processName = matcher.group(1);
                            String version = matcher.group(2);
                            TalendFile talendFile = new TalendFile(folder.getAbsolutePath(), processName, version);
                            if (!data.containsKey(talendFile.getName()) ||  ((TalendFile) data.get(talendFile.getName())).isVersionLowerThan(talendFile)) {
                                data.put(talendFile.getName(), talendFile);
                            }
                        } else {
                            LOG.warn("Cannot find processName or version in [" + filename + "]; Skipping.");
                        }
                    });

            return new ArrayList(data.values());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
