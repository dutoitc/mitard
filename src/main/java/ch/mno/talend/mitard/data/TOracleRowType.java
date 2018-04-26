package ch.mno.talend.mitard.data;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class TOracleRowType extends AbstractNodeType {

    private String query;
    private boolean specifyDatasourceAlias;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "TOracleRowType[" + getUniqueName() + "]";
    }

    public void setSpecifyDatasourceAlias(boolean specifyDatasourceAlias) {
        this.specifyDatasourceAlias = specifyDatasourceAlias;
    }

    public boolean isSpecifyDatasourceAlias() {
        return specifyDatasourceAlias;
    }
}
