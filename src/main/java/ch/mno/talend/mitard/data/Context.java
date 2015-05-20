package ch.mno.talend.mitard.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by dutoitc on 13.05.2015.
 */
public class Context {

    Properties properties = new Properties();

    public Context(InputStream is) {
        properties = new Properties();
        if (is==null) {
            throw new RuntimeException("Canot load null properties file.");
        }
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load properties: " + e.getMessage());
        }
    }

    private String getProperty(String name) {
        String property = properties.getProperty(name);
        if (property==null) {
            throw new RuntimeException("Missing context.properties' property \""+name+"\"");
        }
        return property;
    }

    public String getWorkspacePath() {
        return getProperty("talendWorkspacePath");}

    public String getProductionPath() {
        return getProperty("productionPath");
    }

    public List<String> getBLACKLIST() {
        return Arrays.asList(getProperty("blacklist").split(","));
    }

    public String getDotPath() {
        return getProperty("dotPath");
    }

    public String getJiraUrl() {
        return getProperty("jiraUrl");
    }

    public String getJiraPrefix() {
        return getProperty("jiraPrefix");
    }
}
