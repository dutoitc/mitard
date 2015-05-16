package ch.mno.talend.mitard.data;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class TRestRequestType extends AbstractNodeType {

    private String endpointURI;

    public String getEndpointURI() {
        return endpointURI;
    }

    public void setEndpointURI(String endpointURI) {
        this.endpointURI = endpointURI;
    }

    @Override
    public String toString() {
        return "tREST["+getUniqueName()+", "+endpointURI+"]";
    }
}
