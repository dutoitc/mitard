package ch.mno.talend.mitard.data;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dutoitc on 13.05.2015.
 */
public class Context {
    private String workspacePath="C:\\projets\\talend-mitard\\mitard\\sample\\RCENT";
    //String workspacePath = "C:\\projets\\talend-mitard\\mitard\\src\\main\\test\\resources\\ESBTUTORIALPROJECT";

//    private String productionPath = "C:\\projets\\talend-mitard\\mitard\\produced";
    private String productionPath = "C:\\projets\\talend-mitard\\mitard\\src\\main\\resources\\template\\data";
    private List<String> BLACKLIST = Arrays.asList(".*TEST*", ".*TEMPLATE.*", ".*test_.*", ".*Copy_of.*", ".*MOCK.*", ".*Old", ".*Copy", ".*_old");
    private String dotPath = "C:\\Program Files (x86)\\Graphviz2.38\\bin";
    private String jiraUrl = "http://todo/jira/";
    private String jiraPrefix = "SIREF-";

    public String getWorkspacePath() {
        return workspacePath;
    }

    public String getProductionPath() {
        return productionPath;
    }

    public List<String> getBLACKLIST() {
        return BLACKLIST;
    }

    public String getDotPath() {
        return dotPath;
    }

    public String getJiraUrl() {
        return jiraUrl;
    }

    public String getJiraPrefix() {
        return jiraPrefix;
    }
}
