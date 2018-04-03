package ch.mno.talend.mitard.data;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class TMDMRollbackType extends AbstractNodeType {

    private String connection;
    private boolean close;

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    @Override
    public String toString() {
        return "TMDMRollbackType[" + getUniqueName() + "]";
    }

}
