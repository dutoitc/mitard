package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.Context;
import ch.mno.talend.mitard.out.JSonWriter;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by dutoitc on 13.05.2015.
 */
public class AbstractWriter {

    private final Context context;

    public AbstractWriter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    protected void writeJson(String filename, Object obj) throws IOException {
        (new JSonWriter()).writeJSon(new File(context.getProductionPath()+File.separatorChar+"data"+File.separatorChar+filename), obj);
    }

    protected boolean isBlacklisted(String name) {
        for (String item: context.getBLACKLIST()) {
            if (Pattern.compile(item, Pattern.CASE_INSENSITIVE).matcher(name).find()) {
                return true;
            }
        }
        return false;
    }

}
