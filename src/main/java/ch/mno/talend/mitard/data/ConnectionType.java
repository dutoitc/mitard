package ch.mno.talend.mitard.data;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class ConnectionType {

    public enum CONNECTOR_NAME { FLOW, COMPONENT_OK, RUN_IF, SUBJOB_OK, FILTER, ITERATE, SUBJOB_ERROR,
        REJECT, FAULT, OUTPUT, OUTPUT_1, OUTPUT_2, DUPLICATE, UNIQUE, SYNCHRONIZE, PARALLELIZE,  WRONG_CALLS,
        ROW_PATTERN_OK, ROW_PATTERN_KO, RESPONSE, COMPONENT_ERROR,
        ROUTE, ROUTE_WHEN, ROUTE_OTHER,
        OTHER}


    // <connection connectorName="FLOW" label="shit22" lineStyle="0" metaname="shit22" offsetLabelX="0" offsetLabelY="0" source="tMap_11" target="tFilterRow_66">...
    // <connection connectorName="COMPONENT_OK" label="OnComponentOk" lineStyle="3" metaname="tFilterRow_66" offsetLabelX="0" offsetLabelY="0" source="tFilterRow_66" target="tOracleRow_1">

    private String source;
    private String target;
    private CONNECTOR_NAME connectorName;

    public ConnectionType(String source, String target, String connectorName) {
        this.source = source;
        this.target = target;
        try {
            this.connectorName = CONNECTOR_NAME.valueOf(connectorName);
        } catch (Exception e) {
            System.err.println("Unsupported connector name: " + connectorName);
            this.connectorName = CONNECTOR_NAME.OTHER;
        }
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public CONNECTOR_NAME getConnectorName() {
        return connectorName;
    }

}