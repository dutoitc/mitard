package ch.mno.talend.mitard.data;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class TESBConsumerType extends AbstractNodeType {

    private String serviceName;
    private String endpointURI;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getEndpointURI() {
        return endpointURI;
    }

    public void setEndpointURI(String endpointURI) {
        this.endpointURI = endpointURI;
    }

    @Override
    public String toString() {
        return "tESBConsumer["+getUniqueName()+", "+endpointURI+"]";
    }
}
