package ch.mno.talend.mitard.data;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class TRestType extends AbstractNodeType {

    private String url;

    @Override
    public String toString() {
        return "TRest["+getUniqueName()+"]";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String code) {
        this.url = code;
    }
}