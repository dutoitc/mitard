package ch.mno.talend.mitard.out;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dutoitc on 12.05.2015.
 */
public class JsonRoutes {

    private List<JsonRoute> routes = new ArrayList<>();


    public void addRoute(String name, String version, String purpose, String description, Date creationDate, Date modificationDate, List<String> screenshots) {
        routes.add(new JsonRoute(name, version, purpose, description, creationDate, modificationDate, screenshots));
    }

    public List<JsonRoute> getRoutes() {
        return routes;
    }


    private class JsonRoute extends AbstractJsonNode {


        private List<String> screenshots;

        public JsonRoute(String name, String version, String purpose, String description, Date creationDate, Date modificationDates, List<String> screenshots) {
            super(name, version, purpose, description, creationDate, modificationDates);
            this.screenshots = screenshots;
        }

        public List<String> getScreenshots() {
            return screenshots;
        }

        public void setScreenshots(List<String> screenshots) {
            this.screenshots = screenshots;
        }
    }

}
