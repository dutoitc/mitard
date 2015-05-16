package ch.mno.talend.mitard.data;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class TRunJobType extends AbstractNodeType {

    private String processName;



    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    @Override
    public String toString() {
        return "TRunJob["+getUniqueName()+", "+processName+"]";
    }
}
