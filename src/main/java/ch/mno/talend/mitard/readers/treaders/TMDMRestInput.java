package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TMDMRestInputType;
import ch.mno.talend.mitard.data.TMDMViewSearchType;

public class TMDMRestInput extends AbstractTReader {

    TMDMRestInputType obj;

    public TMDMRestInput(String componentName) {
        super(new TMDMRestInputType(), componentName);
        obj = (TMDMRestInputType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
        switch (name) {
            case "QUERY_TEXT":
                obj.setQuery(value);
        }
    }
}