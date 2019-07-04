package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.THttpRequestType;

public class THTTPRequest extends AbstractTReader {

    THttpRequestType obj;

    public THTTPRequest(String componentName) {
        super(new THttpRequestType(), componentName);
        obj = (THttpRequestType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
        switch (name) {
            case "URI":
                obj.setUri(value);
                break;
        }
    }
}