package ch.mno.talend.mitard.data;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class TDieType extends AbstractNodeType {

    private String message;

    @Override
    public String toString() {
        return "TDie["+getUniqueName()+"]";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}