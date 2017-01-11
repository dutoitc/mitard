package ch.mno.talend.mitard.data;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class TMDMConnectionType extends AbstractNodeType {

    private boolean autoCommit;

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    @Override
    public String toString() {
        return "TMDMConnectionType[" + getUniqueName() + "]";
    }
}
