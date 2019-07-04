package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TJavaRowType;

public class TJavaRowReader extends AbstractTReader {

    TJavaRowType obj;

    public TJavaRowReader(String componentName) {
        super(new TJavaRowType(), componentName);
        obj = (TJavaRowType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
        switch (name) {
            case "CODE":
                obj.setCode(value);
                break;
        }
    }
}

