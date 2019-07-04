package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TMDMRollbackType;

public class TMDMRollback extends AbstractTReader {

    TMDMRollbackType obj;

    public TMDMRollback(String componentName) {
        super(new TMDMRollbackType(), componentName);
        obj = (TMDMRollbackType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
        switch (name) {
            case "CLOSE":
                obj.setClose("true".equalsIgnoreCase(value));
                break;
            case "CONNECTION":
                obj.setConnection(value);
                break;
        }
    }
}