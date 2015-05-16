package ch.mno.talend.mitard.out;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dutoitc on 12.05.2015.
 */
public class JsonProcesses {

    private List<JsonProcess> processes = new ArrayList<>();

    public void addProcess(String name, String version, String purpose, String description, Date creationDate, Date modificationDate, List<String> screenshots) {
        processes.add(new JsonProcess(name, version, purpose, description, creationDate, modificationDate, screenshots));
    }

    public List<JsonProcess> getProcesses() {
        return processes;
    }

    private class JsonProcess extends AbstractJsonNode {

        private final List<String> screenshots;

        public JsonProcess(String name, String version, String purpose, String description, Date creationDate, Date modificationDate, List<String> screenshots) {
            super(name, version, purpose, description, creationDate, modificationDate);
            this.screenshots = screenshots;
        }

        public List<String> getScreenshots() {
            return screenshots;
        }

    }

}
