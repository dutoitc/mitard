package ch.mno.talend.mitard.data;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class TOracleCommitType extends AbstractNodeType {

    private boolean close;

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    @Override
    public String toString() {
        return "TOracleCommit[" + getUniqueName() + "]";
    }
}
