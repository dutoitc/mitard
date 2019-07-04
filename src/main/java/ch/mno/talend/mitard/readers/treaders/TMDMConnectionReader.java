package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TMDMConnectionType;

public class TMDMConnectionReader extends AbstractTReader {

    TMDMConnectionType obj;

    public TMDMConnectionReader(String componentName) {
        super(new TMDMConnectionType(), componentName);
        obj = (TMDMConnectionType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
        switch (name) {
            case "AUTO_COMMIT":
                obj.setAutoCommit("true".equals(value));
                break;
        }
    }
}