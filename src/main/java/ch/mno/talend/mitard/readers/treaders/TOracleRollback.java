package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TOracleRollbackType;

public class TOracleRollback extends AbstractTReader {

    TOracleRollbackType obj;

    public TOracleRollback(String componentName) {
        super(new TOracleRollbackType(), componentName);
        obj = (TOracleRollbackType) super.getNode();
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