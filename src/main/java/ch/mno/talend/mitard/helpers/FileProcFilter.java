package ch.mno.talend.mitard.helpers;

import java.io.File;
import java.io.FileFilter;

public class FileProcFilter implements FileFilter {
    public FileProcFilter() {
    }

    public boolean accept(File pathname) {
        return pathname.isFile() && pathname.getName().endsWith(".proc");
    }
}
