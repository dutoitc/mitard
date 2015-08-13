package ch.mno.talend.mitard.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class TNodeType extends AbstractNodeType {

    private int x, y;
    private Map<String, String> fieldValues = new HashMap<>();


    @Override
    public String toString() {
        return "(node)["+getUniqueName()+"]";
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void addElementParameter(String field, String name, String value) {
        fieldValues.put(name, value);
    }

    public String getValue(String name) {
        return fieldValues.get(name);
    }

}
