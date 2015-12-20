package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.Context;
import ch.mno.talend.mitard.data.TalendFiles;
import ch.mno.talend.mitard.out.JSonWriter;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by dutoitc on 13.05.2015.
 */
public abstract class AbstractWriter {

    private final Context context;

    public AbstractWriter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public abstract void write(TalendFiles talendFiles);

    /** Write obj as JSON in productionPath/data/filename */
    protected void writeJson(String filename, Object obj) throws IOException {
        String pathname = context.getProductionPath() + File.separatorChar + "data" + File.separatorChar + filename;
        (new JSonWriter()).writeJSon(new File(pathname), obj);
    }

    /** Check if the given name is in the context blacklist */
    protected boolean isBlacklisted(String name) {
        for (String item: context.getBLACKLIST()) {
            if (Pattern.compile(item, Pattern.CASE_INSENSITIVE).matcher(name).find()) {
                return true;
            }
        }
        return false;
    }

}
