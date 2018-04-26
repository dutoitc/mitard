package ch.mno.talend.mitard.data;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class TOracleConnectionType extends AbstractNodeType {

    private boolean autoCommit;
    private String dbName;
    private boolean specifyDatasourceAlias;

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    @Override
    public String toString() {
        return "TOracleConnectionType[" + getUniqueName() + "]";
    }

    public void setSpecifyDatasourceAlias(boolean specifyDatasourceAlias) {
        this.specifyDatasourceAlias = specifyDatasourceAlias;
    }

    public boolean isSpecifyDatasourceAlias() {
        return specifyDatasourceAlias;
    }
}
