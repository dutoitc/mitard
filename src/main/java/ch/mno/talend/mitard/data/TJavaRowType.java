package ch.mno.talend.mitard.data;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class TJavaRowType extends AbstractNodeType {

    private String code;

    @Override
    public String toString() {
        return "TJavaRow["+getUniqueName()+"]";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}