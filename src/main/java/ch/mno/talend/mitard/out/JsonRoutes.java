package ch.mno.talend.mitard.out;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.mno.talend.mitard.data.TalendUserType;

/**
 * Created by dutoitc on 12.05.2015.
 */
public class JsonRoutes {

    private List<JsonRoute> routes = new ArrayList<>();


    public void addRoute(String path, String name, String version, String purpose, String description, Date creationDate, Date modificationDate, List<String> screenshots, TalendUserType author) {
        routes.add(new JsonRoute(path, name, version, purpose, description, creationDate, modificationDate, screenshots, author.getId(), author.getLogin(), author.getFirstName(), author.getLastName()));
    }

    public List<JsonRoute> getRoutes() {
        return routes;
    }


    private class JsonRoute extends AbstractJsonNode {


        private List<String> screenshots;

        public JsonRoute(String path, String name, String version, String purpose, String description, Date creationDate, Date modificationDates, List<String> screenshots,
                         String authorId, String authorLogin, String authorFirstname, String authorLastname) {
            super(path, name, version, purpose, description, creationDate, modificationDates, authorId, authorLogin, authorFirstname, authorLastname);
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
