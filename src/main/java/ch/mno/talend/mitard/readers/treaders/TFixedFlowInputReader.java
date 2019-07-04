package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TFixedFlowInputType;

public class TFixedFlowInputReader extends AbstractTReader {

    TFixedFlowInputType obj;

    public TFixedFlowInputReader(String componentName) {
        super(new TFixedFlowInputType(), componentName);
        obj = (TFixedFlowInputType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
        switch (name) {
            case "VALUE":
            case "SCHEMA_COLUMN":
                obj.addText(value);
                break;
        }
    }
}