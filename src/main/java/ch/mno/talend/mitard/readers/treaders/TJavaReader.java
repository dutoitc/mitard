package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TJavaType;

public class TJavaReader extends AbstractTReader {

    TJavaType obj;

    public TJavaReader(String componentName) {
        super(new TJavaType(), componentName);
        obj = (TJavaType) super.getNode();
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