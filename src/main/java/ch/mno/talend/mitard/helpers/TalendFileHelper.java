package ch.mno.talend.mitard.helpers;

import ch.mno.talend.mitard.data.TalendFile;
import ch.mno.talend.mitard.data.TalendFiles;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TalendFileHelper {
    public TalendFileHelper() {
    }

    public static TalendFiles findLatestVersions(String talendWorkspacePath) {
        TalendFiles talendFiles = new TalendFiles();
        talendFiles.setProcesses(findLatestVersionsInternal(new File(talendWorkspacePath + File.separatorChar+"process")));
        talendFiles.setRoutes(findLatestVersionsInternal(new File(talendWorkspacePath +  File.separatorChar+"routes")));
        talendFiles.setServices(findLatestVersionsInternal(new File(talendWorkspacePath +  File.separatorChar+"services")));


        talendFiles.setMDMWorkflowProc(
                findLatestVersionsInternal(new File(talendWorkspacePath + File.separatorChar + "MDM" + File.separatorChar + "workflow")));

        return talendFiles;

    }

    private static List<TalendFile> findLatestVersionsInternal(File folder) {

        System.out.println(folder.getAbsolutePath());
        HashMap data = new HashMap();
        File[] files = folder.listFiles(new DirectoryFilter());

        for (File file: files) {
            List name = findLatestVersionsInternal(file);
            Iterator p = name.iterator();

            while(p.hasNext()) {
                TalendFile talendFile = (TalendFile)p.next();
                if(!data.containsKey(talendFile.getName()) || ((TalendFile)data.get(talendFile.getName())).isVersionLowerThan(talendFile)) {
                    data.put(talendFile.getName(), talendFile);
                }
            }
        }

        files = folder.listFiles(new FileItemFilter());

        for (File file: files) {
            String name = file.getName();
            int var12 = name.lastIndexOf("_");
            int var13 = name.lastIndexOf(".");
            TalendFile talendFile = new TalendFile(folder.getAbsolutePath(), name.substring(0, var12), name.substring(var12 + 1, var13));
//            data.put(talendFile.getName(), talendFile);
            if(!data.containsKey(talendFile.getName()) || ((TalendFile)data.get(talendFile.getName())).isVersionLowerThan(talendFile)) {
                data.put(talendFile.getName(), talendFile);
            }
        }

        return new ArrayList(data.values());
    }
}
