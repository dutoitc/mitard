package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TOracleCommitType;

public class TOracleCommitReader extends AbstractTReader {

    TOracleCommitType obj;

    public TOracleCommitReader(String componentName) {
        super(new TOracleCommitType(), componentName);
        obj = (TOracleCommitType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
        switch (name) {
            case "CLOSE":
                obj.setClose("true".equals(value));
                break;
            case "CONNECTION":
                obj.setConnection(value);
                break;
        }
    }
}