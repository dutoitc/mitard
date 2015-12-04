package ch.mno.talend.mitard.out;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.mno.talend.mitard.data.TalendUserType;

/**
 * Created by dutoitc on 12.05.2015.
 */
public class JsonServices {


    private List<JsonService> services = new ArrayList<>();


    public void addService(String path, String name, String version, String purpose, String description, Date creationDate, Date modificationDate, List<String> screenshots, TalendUserType author) {
        services.add(new JsonService(path, name, version, purpose, description, creationDate, modificationDate, screenshots, author.getId(), author.getLogin(), author.getFirstName(), author.getLastName()));
    }

    public List<JsonService> getServices() {
        return services;
    }


    private class JsonService extends AbstractJsonNode {


        private final List<String> screenshots;

        public JsonService(String path, String name, String version, String purpose, String description, Date creationDate, Date modificationDate, List<String> screenshots,
                           String authorId, String authorLogin, String authorFirstname, String authorLastname) {
            super(path, name, version, purpose, description, creationDate, modificationDate, authorId, authorLogin, authorFirstname, authorLastname);
            this.screenshots = screenshots;
        }

        public List<String> getScreenshots() {
            return screenshots;
        }
    }

}
