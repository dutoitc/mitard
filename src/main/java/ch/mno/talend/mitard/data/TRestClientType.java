package ch.mno.talend.mitard.data;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class TRestClientType extends AbstractNodeType {

    private String url;
    private String path;
    private String authUsername;
    private String authPassword;

    @Override
    public String toString() {
        return "TRestClient[" + getUniqueName() + "]";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String code) {
        this.url = code;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAuthUsername() {
        return authUsername;
    }

    public void setAuthUsername(String authUsername) {
        this.authUsername = authUsername;
    }

    public String getAuthPassword() {
        return authPassword;
    }

    public void setAuthPassword(String authPassword) {
        this.authPassword = authPassword;
    }
}