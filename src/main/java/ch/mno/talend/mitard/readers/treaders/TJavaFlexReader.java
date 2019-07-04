package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TJavaFlexType;

public class TJavaFlexReader extends AbstractTReader {

    TJavaFlexType obj;

    public TJavaFlexReader(String componentName) {
        super(new TJavaFlexType(), componentName);
        obj = (TJavaFlexType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
        switch (name) {
            case "CODE_START":
                obj.setCodeStart(value);
                break;
            case "CODE_MAIN":
                obj.setCodeMain(value);
                break;
            case "CODE_END":
                obj.setCodeEnd(value);
                break;
        }
    }
}