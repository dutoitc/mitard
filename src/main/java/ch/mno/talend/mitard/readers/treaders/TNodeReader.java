package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TNodeType;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;

public class TNodeReader extends AbstractTReader {

    TNodeType obj;

    public TNodeReader(String componentName) {
        super(new TNodeType(), componentName);
        obj = (TNodeType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
    }


    public void startElement(String localName, Attributes attributes) {
        super.startElement(localName, attributes);
        if ("NODE".equals(localName.toUpperCase())) {
            String x = attributes.getValue("posX");
            if (StringUtils.isNotBlank(x)) obj.setX(Integer.parseInt(x));
            String y = attributes.getValue("posY");
            if (StringUtils.isNotBlank(y)) obj.setY(Integer.parseInt(y));
        } else if ("ELEMENTPARAMETER".equals(localName.toUpperCase())) {
            obj.addElementParameter(attributes.getValue("field"), attributes.getValue("name"), attributes.getValue("value"));
        }
    }

}