package ch.mno.talend.mitard.out;

import ch.mno.talend.mitard.data.TalendUserType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dutoitc on 12.05.2015.
 */
public class JsonWorkflows {


    private List<JsonWorkflow> workflows = new ArrayList<>();


    public void addWorkflow(String path, String name, String version, Date creationDate, Date modificationDate, TalendUserType author, List<String> services) {
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
        workflows.add(new JsonWorkflow(path, name, version, creationDate, modificationDate, authorId, authorLogin, authorFirstName, authorLastName, services));
    }

    public List<JsonWorkflow> getWorkflows() {
        return workflows;
    }


    private class JsonWorkflow extends AbstractJsonNode {


        private final List<String> services;

        public JsonWorkflow(String path, String name, String version, Date creationDate, Date modificationDate,
                            String authorId, String authorLogin, String authorFirstname, String authorLastname, List<String> services) {
            super(path, name, version, "", "", creationDate, modificationDate, authorId, authorLogin, authorFirstname, authorLastname);
            this.services = services;
        }

        public List<String> getServices() {
            return services;
        }

    }

}