package ch.mno.talend.mitard.data;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class TFixedFlowInputType extends AbstractNodeType {

    private String text="";

    @Override
    public String toString() {
        return "TFixedFlowInput["+getUniqueName()+"]";
    }

    public void addText(String text) {
        this.text+=text+";";
    }

    public String getText() {
        return text;
    }
}