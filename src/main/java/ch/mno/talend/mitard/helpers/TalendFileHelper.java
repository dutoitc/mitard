package ch.mno.talend.mitard.helpers;

import ch.mno.talend.mitard.data.TalendFile;
import ch.mno.talend.mitard.data.TalendFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TalendFileHelper {
    public TalendFileHelper() {
    }

    public static TalendFiles findLatestVersions(String talendWorkspacePath) {
        TalendFiles talendFiles = new TalendFiles();
        talendFiles.setProcesses(findLatestVersionsInternal(new File(talendWorkspacePath + "\\process")));
        talendFiles.setRoutes(findLatestVersionsInternal(new File(talendWorkspacePath + "\\routes")));
        talendFiles.setServices(findLatestVersionsInternal(new File(talendWorkspacePath + "\\services")));

        return talendFiles;

    }

    private static List<TalendFile> findLatestVersionsInternal(File folder) {
        HashMap data = new HashMap();
        File[] var3 = folder.listFiles(new DirectoryFilter());
        int var4 = var3.length;

        int var5;
        File file;
        for(var5 = 0; var5 < var4; ++var5) {
            file = var3[var5];
            List name = findLatestVersionsInternal(file);
            Iterator p = name.iterator();

            while(p.hasNext()) {
                TalendFile p2 = (TalendFile)p.next();
                if(!data.containsKey(p2.getName()) || ((TalendFile)data.get(p2.getName())).isVersionLowerThan(p2)) {
                    data.put(p2.getName(), p2);
                }
            }
        }

        var3 = folder.listFiles(new FileItemFilter());
        var4 = var3.length;

        for(var5 = 0; var5 < var4; ++var5) {
            file = var3[var5];
            String var11 = file.getName();
            int var12 = var11.lastIndexOf("_");
            int var13 = var11.lastIndexOf(".");
            TalendFile talendFile = new TalendFile(folder.getAbsolutePath(), var11.substring(0, var12), var11.substring(var12 + 1, var13));
            data.put(talendFile.getName(), talendFile);
        }

        return new ArrayList(data.values());
    }
}
