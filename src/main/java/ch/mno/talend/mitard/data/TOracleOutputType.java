package ch.mno.talend.mitard.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class TOracleOutputType extends AbstractNodeType {


    private String dbTable;
    private List<String> schemaColumns = new ArrayList<>();

    @Override
    public String toString() {
        return "TOracleOutput["+getUniqueName()+"]";
    }

    public void setDbTable(String dbTable) {
        this.dbTable = dbTable;
    }

    public String getDbTable() {
        return dbTable;
    }

    public void addSchemaColumn(String value) {
        schemaColumns.add(value);
    }

    public String getText() {
        return "Table="+dbTable+",schemaColumns="+ StringUtils.join(schemaColumns);
    }
}