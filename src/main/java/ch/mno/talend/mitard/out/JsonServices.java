package ch.mno.talend.mitard.out;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dutoitc on 12.05.2015.
 */
public class JsonServices {


    private List<JsonService> services = new ArrayList<>();


    public void addService(String path, String name, String version, String purpose, String description, Date creationDate, Date modificationDate, List<String> screenshots) {
        services.add(new JsonService(path, name, version, purpose, description, creationDate, modificationDate, screenshots));
    }

    public List<JsonService> getServices() {
        return services;
    }


    private class JsonService extends AbstractJsonNode {


        private final List<String> screenshots;

        public JsonService(String path, String name, String version, String purpose, String description, Date creationDate, Date modificationDate, List<String> screenshots) {
            super(path, name, version, purpose, description, creationDate, modificationDate);
            this.screenshots = screenshots;
        }

        public List<String> getScreenshots() {
            return screenshots;
        }
    }

}
