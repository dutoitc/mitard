package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TOracleOutputType;

public class TOracleOutputReader extends AbstractTReader {

    TOracleOutputType obj;

    public TOracleOutputReader(String componentName) {
        super(new TOracleOutputType(), componentName);
        obj = (TOracleOutputType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
        switch (name) {
            case "DBTABLE":
                obj.setDbTable(value);
                break;
            case "SCHEMA_COLUMN":
                obj.addSchemaColumn(value);
                break;
            case "SPECIFY_DATASOURCE_ALIAS":
                obj.setSpecifyDatasourceAlias("TRUE".equalsIgnoreCase(value));
                break;
        }
    }
}