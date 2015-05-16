package ch.mno.talend.mitard.helpers;

import java.io.File;
import java.io.FileFilter;

public class DirectoryFilter implements FileFilter {
    public DirectoryFilter() {
    }

    public boolean accept(File pathname) {
        return pathname.isDirectory() && !pathname.getName().startsWith("/") && !pathname.getName().startsWith(".");
    }
}
