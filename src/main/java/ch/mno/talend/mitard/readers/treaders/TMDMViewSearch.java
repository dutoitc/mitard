package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TMDMRollbackType;
import ch.mno.talend.mitard.data.TMDMViewSearchType;

public class TMDMViewSearch extends AbstractTReader {

    TMDMViewSearchType obj;

    public TMDMViewSearch(String componentName) {
        super(new TMDMViewSearchType(), componentName);
        obj = (TMDMViewSearchType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
        switch (name) {
            case "XPATH":
                obj.addOperation(value);
        }
    }
}