package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TOracleInputType;

public class TOracleInputReader extends AbstractTReader {

    TOracleInputType obj;

    public TOracleInputReader(String componentName) {
        super(new TOracleInputType(), componentName);
        obj = (TOracleInputType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
        switch (name) {
            case "DBTABLE":
                obj.setDbTable(value);
                break;
            case "QUERY":
                obj.setQuery(value);
                break;
            case "SPECIFY_DATASOURCE_ALIAS":
                obj.setSpecifyDatasourceAlias("TRUE".equalsIgnoreCase(value));
                break;
        }
    }
}