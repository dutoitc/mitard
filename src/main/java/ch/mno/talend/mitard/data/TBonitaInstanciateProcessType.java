package ch.mno.talend.mitard.data;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class TBonitaInstanciateProcessType extends AbstractNodeType {

    private String processName;
    private String processVersion;

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

    public void setProcessVersion(String processVersion) {
        this.processVersion = processVersion;
    }

    public String getProcessVersion() {
        return processVersion;
    }
}