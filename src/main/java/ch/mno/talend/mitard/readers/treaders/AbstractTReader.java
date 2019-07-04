package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.AbstractNodeType;
import org.xml.sax.Attributes;

public abstract class AbstractTReader {

    AbstractNodeType obj;

    public AbstractTReader(AbstractNodeType obj, String componentName) {
        this.obj = obj;
        obj.setComponentName(componentName);
    }

    public void startElement(String localName, Attributes attributes) {
        if (localName.equals("elementParameter")) {
            String name = getAttribute(attributes, "name");
            String value = getAttribute(attributes, "value");

            if ("UNIQUE_NAME".equals(name)) {
                obj.setUniqueName(value);
            } else if ("ACTIVE".equals(name)) {
                obj.setActive("true".equals(value));
            } else if ("USE_EXISTING_CONNECTION".equals(name)) {
                obj.setUseExistingConnection("true".equals(value));
            } else if ("SERVICE_ACTIVITY_MONITOR".equals(name)) {
                obj.setSam("true".equals(value));
            }
            handleElement(name, value);
        }
        if (localName.equals("elementValue")) {
            String name = getAttribute(attributes, "elementRef");
            String value = getAttribute(attributes, "value");
            if ("SERVICE_ACTIVITY_MONITOR".equals(name)) {
                obj.setSam("true".equals(value));
            }
            handleElement(name, value);
        }
    }

    protected abstract void handleElement(String name, String value);

    public AbstractNodeType getNode() {
        return obj;
    }


    private String getAttribute(Attributes attributes, String name) {
        for (int i = 0; i < attributes.getLength(); i++) {
            if (attributes.getLocalName(i).equals(name)) {
                return attributes.getValue(i);
            }
        }
        return null;
    }

}