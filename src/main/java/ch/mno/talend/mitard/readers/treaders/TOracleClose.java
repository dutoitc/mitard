package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TOracleCloseType;

public class TOracleClose extends AbstractTReader {

    TOracleCloseType obj;

    public TOracleClose(String componentName) {
        super(new TOracleCloseType(), componentName);
        obj = (TOracleCloseType) super.getNode();
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