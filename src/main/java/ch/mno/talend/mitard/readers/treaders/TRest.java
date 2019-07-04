package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TRestType;

public class TRest extends AbstractTReader {

    TRestType obj;

    public TRest(String componentName) {
        super(new TRestType(), componentName);
        obj = (TRestType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
        switch (name) {
            case "URL":
                obj.setUrl(value);
                break;
        }
    }
}