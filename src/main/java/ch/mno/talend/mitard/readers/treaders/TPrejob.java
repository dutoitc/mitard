package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TPrejobType;

public class TPrejob extends AbstractTReader {

    TPrejobType obj;

    public TPrejob(String componentName) {
        super(new TPrejobType(), componentName);
        obj = (TPrejobType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
    }
}