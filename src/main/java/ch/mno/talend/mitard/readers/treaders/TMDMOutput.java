package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TMDMOutputType;
import ch.mno.talend.mitard.data.TMDMViewSearchType;

public class TMDMOutput extends AbstractTReader {

    TMDMOutputType obj;

    public TMDMOutput(String componentName) {
        super(new TMDMOutputType(), componentName);
        obj = (TMDMOutputType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
        switch (name) {
            case "PATH":
                obj.addPath(value);
                break;
            case "WITHREPORT":
                obj.setWithReport("true".equals(value));
                break;
        }
    }
}