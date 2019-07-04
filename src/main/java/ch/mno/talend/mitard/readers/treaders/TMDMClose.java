package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TMDMCloseType;

public class TMDMClose extends AbstractTReader {

    TMDMCloseType obj;

    public TMDMClose(String componentName) {
        super(new TMDMCloseType(), componentName);
        obj = (TMDMCloseType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
        switch (name) {
            case "CONNECTION":
                obj.setConnection(value);
                break;
        }
    }
}