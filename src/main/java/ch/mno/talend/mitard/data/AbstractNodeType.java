package ch.mno.talend.mitard.data;

import java.util.Date;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class AbstractNodeType {


    private String uniqueName;
    private String componentName;
    private String purpose;
    private String description;
    private Date creationDate;
    private Date modificationDate;
    private boolean active=true;
    private boolean useExistingConnection;

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }


    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentName() {
        return componentName;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setUseExistingConnection(boolean useExistingConnection) {
        this.useExistingConnection = useExistingConnection;
    }

    public boolean isUseExistingConnection() {
        return useExistingConnection;
    }
}
