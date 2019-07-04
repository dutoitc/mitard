package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TMDMCommitType;

public class TMDMCommitReader extends AbstractTReader {

    TMDMCommitType obj;

    public TMDMCommitReader(String componentName) {
        super(new TMDMCommitType(), componentName);
        obj = (TMDMCommitType) super.getNode();
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