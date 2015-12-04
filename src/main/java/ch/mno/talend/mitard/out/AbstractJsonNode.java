package ch.mno.talend.mitard.out;

import java.util.Date;

/**
 * Created by dutoitc on 14.05.2015.
 */
public class AbstractJsonNode {

    private String path;
    private String name;
    private String version;
    private String purpose;
    private String description;
    private Date creationDate;
    private Date modificationDate;

    private String authorId, authorLogin, authorFirstname, authorLastname;

    public AbstractJsonNode(String path, String name, String version, String purpose, String description, Date creationDate, Date modificationDate, String authorId, String authorLogin, String authorFirstname, String authorLastname) {
        this.path = path;
        this.name = name;
        this.version = version;
        this.purpose = purpose;
        this.description = description;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.authorId = authorId;
        this.authorLogin = authorLogin;
        this.authorFirstname = authorFirstname;
        this.authorLastname = authorLastname;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorLogin() {
        return authorLogin;
    }

    public void setAuthorLogin(String authorLogin) {
        this.authorLogin = authorLogin;
    }

    public String getAuthorFirstname() {
        return authorFirstname;
    }

    public void setAuthorFirstname(String authorFirstname) {
        this.authorFirstname = authorFirstname;
    }

    public String getAuthorLastname() {
        return authorLastname;
    }

    public void setAuthorLastname(String authorLastname) {
        this.authorLastname = authorLastname;
    }
}
