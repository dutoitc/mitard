package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TMDMInputType;

public class TMDMInput extends AbstractTReader {

    TMDMInputType obj;

    public TMDMInput(String componentName) {
        super(new TMDMInputType(), componentName);
        obj = (TMDMInputType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
        switch (name) {
            case "CONCEPT":
                obj.setConcept(value);
        }
    }
}