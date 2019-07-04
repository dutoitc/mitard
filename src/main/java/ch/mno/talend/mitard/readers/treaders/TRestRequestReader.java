package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TRestRequestType;

public class TRestRequestReader extends AbstractTReader {

        TRestRequestType obj;

        public TRestRequestReader(String componentName) {
            super(new TRestRequestType(), componentName);
            obj = (TRestRequestType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
            switch (name) {
                case "REST_ENDPOINT":
                    if (value.charAt(0) == '"') value = value.substring(1, value.length() - 1);
                    obj.setEndpointURI(value);
                    break;
                default:
            }
        }
    }