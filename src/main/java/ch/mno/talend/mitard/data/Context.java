package ch.mno.talend.mitard.data;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    Properties projectProperties = new Properties();

    public Context(InputStream is) {
        properties = new Properties();
        if (is==null) {
            throw new RuntimeException("Canot load null properties file. Check that the specified file exists.");
        }
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load properties: " + e.getMessage());
        }

        // Load project properties
        if (properties.getProperty("properties")!=null) {
            for (String path : properties.getProperty("properties").split(",")) {
                if (path.isEmpty()) continue;
                String location = properties.getProperty("talendWorkspacePath") + File.separatorChar + path;
                try (FileInputStream fis = new FileInputStream(location)) {
                    assert fis != null;
                    for (String line : IOUtils.toString(fis).split("\n")) {
                        String[] spl = line.split(";");
                        if (spl.length == 2) {
                            projectProperties.put(spl[0], spl[1]);
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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


    public String getProjectProperties(String key) {
        return projectProperties.getProperty(key);
    }
}
