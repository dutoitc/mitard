package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TESBConsumerType;

public class TESBProviderRequestReader extends AbstractTReader {

    TESBConsumerType obj;

    public TESBProviderRequestReader(String componentName) {
        super(new TESBConsumerType(), componentName);
        obj = (TESBConsumerType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
        switch (name) {
            case "ENDPOINT_URI":
                if (value.charAt(0) == '"') value = value.substring(1, value.length() - 1);
                obj.setEndpointURI(value);
                break;
            case "SERVICE_NAME":
                obj.setServiceName(value);
                break;
            default:
        }
    }
}
