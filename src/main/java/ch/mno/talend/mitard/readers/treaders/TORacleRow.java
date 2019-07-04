package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TOracleRowType;

public class TORacleRow extends AbstractTReader {

    TOracleRowType obj;

    public TORacleRow(String componentName) {
        super(new TOracleRowType(), componentName);
        obj = (TOracleRowType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
        switch (name) {
            case "QUERY":
                obj.setQuery(value);
                break;
            case "SPECIFY_DATASOURCE_ALIAS":
                obj.setSpecifyDatasourceAlias("TRUE".equalsIgnoreCase(value));
                break;
        }
    }
}