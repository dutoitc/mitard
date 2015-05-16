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

    public AbstractJsonNode(String path, String name, String version, String purpose, String description, Date creationDate, Date modificationDate) {
        this.path = path;
        this.name = name;
        this.version = version;
        this.purpose = purpose;
        this.description = description;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
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
}
