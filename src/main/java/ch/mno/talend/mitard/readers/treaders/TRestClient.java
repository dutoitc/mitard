package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TRestClientType;

public class TRestClient extends AbstractTReader {

    TRestClientType obj;

    public TRestClient(String componentName) {
        super(new TRestClientType(), componentName);
        obj = (TRestClientType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
        switch (name) {
            case "URL":
                obj.setUrl(value);
                break;
            case "PATH":
                obj.setPath(value);
                break;
            case "AUTH_USERNAME":
                obj.setAuthUsername(value);
                break;
            case "AUTH_PASSWORD":
                obj.setAuthPassword(value);
                break;
        }
    }
}