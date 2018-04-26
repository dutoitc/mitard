package ch.mno.talend.mitard.data;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class TOracleInputType extends AbstractNodeType {


    private String dbTable;
    private String query;
    private boolean specifyDatasourceAlias;

    @Override
    public String toString() {
        return "TOracleInput["+getUniqueName()+"]";
    }

    public void setDbTable(String dbTable) {
        this.dbTable = dbTable;
    }

    public String getDbTable() {
        return dbTable;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public String getText() {
        return "Table="+dbTable+",query="+query;
    }

    public void setSpecifyDatasourceAlias(boolean specifyDatasourceAlias) {
        this.specifyDatasourceAlias = specifyDatasourceAlias;
    }

    public boolean isSpecifyDatasourceAlias() {
        return specifyDatasourceAlias;
    }
}