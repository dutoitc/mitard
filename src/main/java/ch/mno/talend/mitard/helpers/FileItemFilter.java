package ch.mno.talend.mitard.helpers;

import java.io.File;
import java.io.FileFilter;

public class FileItemFilter implements FileFilter {
    public FileItemFilter() {
    }

    public boolean accept(File pathname) {
        return pathname.isFile() && pathname.getName().endsWith(".item");
    }
}
