package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TSoapType;

public class TSoap extends AbstractTReader {

    TSoapType obj;

    public TSoap(String componentName) {
        super(new TSoapType(), componentName);
        obj = (TSoapType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
        switch (name) {
            case "ENDPOINT":
                obj.setEndpoint(value);
                break;
            case "ACTION":
                obj.setAction(value);
                break;
        }
    }
}