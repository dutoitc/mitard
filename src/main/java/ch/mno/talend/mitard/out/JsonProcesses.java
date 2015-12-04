package ch.mno.talend.mitard.out;

import ch.mno.talend.mitard.data.TalendUserType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dutoitc on 12.05.2015.
 */
public class JsonProcesses {

    private List<JsonProcess> processes = new ArrayList<>();

    public void addProcess(String path, String name, String version, String purpose, String description, Date creationDate, Date modificationDate,
                           List<String> screenshots, TalendUserType author) {
        String authorId="";
        String authorLogin="";
        String authorFirstName="";
        String authorLastName="";
        if (author!=null) {
            authorId = author.getId();
            authorLogin = author.getLogin();
            authorFirstName = author.getFirstName();
            authorLastName = author.getLastName();
        }
        processes.add(new JsonProcess(path, name, version, purpose, description, creationDate, modificationDate, screenshots, authorId, authorLogin, authorFirstName, authorLastName));
    }

    public List<JsonProcess> getProcesses() {
        return processes;
    }

    private class JsonProcess extends AbstractJsonNode {

        private final List<String> screenshots;

        public JsonProcess(String path, String name, String version, String purpose, String description, Date creationDate, Date modificationDate, List<String> screenshots,
                    String authorId, String authorLogin, String authorFirstname, String authorLastname) {
            super(path, name, version, purpose, description, creationDate, modificationDate, authorId, authorLogin, authorFirstname, authorLastname);
            this.screenshots = screenshots;
        }

        public List<String> getScreenshots() {
            return screenshots;
        }

    }

}