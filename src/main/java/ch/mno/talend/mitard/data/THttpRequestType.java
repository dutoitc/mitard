package ch.mno.talend.mitard.data;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class THttpRequestType extends AbstractNodeType {

    private String uri;

    @Override
    public String toString() {
        return "THttpRequest[" + getUniqueName() + "]";
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}