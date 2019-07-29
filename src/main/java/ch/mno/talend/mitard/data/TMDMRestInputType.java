package ch.mno.talend.mitard.data;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class TMDMRestInputType extends AbstractNodeType {


    private String query;

    @Override
    public String toString() {
        return "TMDMRestInputType[" + getUniqueName() + "]";
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
