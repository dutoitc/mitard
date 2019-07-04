package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TDieType;

public class TDieReader extends AbstractTReader {

    TDieType obj;

    public TDieReader(String componentName) {
        super(new TDieType(), componentName);
        obj = (TDieType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
        switch (name) {
            case "MESSAGE":
                obj.setMessage(value);
                break;
        }
    }
}
