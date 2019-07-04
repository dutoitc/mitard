package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TOracleConnectionType;

public class TOracleConnection extends AbstractTReader {

    TOracleConnectionType obj;

    public TOracleConnection(String componentName) {
        super(new TOracleConnectionType(), componentName);
        obj = (TOracleConnectionType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
        switch (name) {
            case "AUTO_COMMIT":
                obj.setAutoCommit("TRUE".equalsIgnoreCase(value));
                break;
            case "SPECIFY_DATASOURCE_ALIAS":
                obj.setSpecifyDatasourceAlias("TRUE".equalsIgnoreCase(value));
                break;
            case "DBNAME":
                obj.setDbName(value);
                break;
            case "DATASOURCE_ALIAS":
                obj.setDatasourceAlias(value.replaceAll("&quot;", "'"));
                break;
            // TODO: read DBName (child)
        }
    }
}