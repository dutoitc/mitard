package ch.mno.talend.mitard.data;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class TBonitaInstanciateProcessType extends AbstractNodeType {

    private String processName;

    @Override
    public String toString() {
        return "TBonitaInstanciateProcessType["+getUniqueName()+"]";
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }
}