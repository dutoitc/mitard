package ch.mno.talend.mitard.data;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** A Talend file (in fact, a component, with .item, .properties, .screenshot, .proc?) */
public class TalendFile {


    public static final Pattern PAT_TALENDPROPERTIES_PROPERTY = Pattern.compile("TalendProperties:Property.*?\\ id=\"(.*?)\"");


    private String path;
    private String name;
    private String version;

    public TalendFile(String path, String name, String version) {
        this.path = path;
        this.name = name;
        this.version = version;
    }

    public String getItemFilename() {
        return this.path + File.separatorChar + this.name + "_" + this.version + ".item";
    }

    public String getPropertiesFilename() {
        return this.path + File.separatorChar + this.name + "_" + this.version + ".properties";
    }

    public String getWSDLFilename() {
        return this.path + File.separatorChar + this.name + "_" + this.version + ".wsdl";
    }

    public String getScreenshotFilename() {
        return this.path + File.separatorChar + this.name + "_" + this.version + ".screenshot";
    }

    public String getProcFilename() {
        String filename = this.path + File.separatorChar + this.name + "_" + this.version + ".proc";
        if (!new File(filename).exists()) {
            // Talend 6.4 replaces _proc by -proc (bug?)
            filename = this.path + File.separatorChar + this.name + "-" + this.version + ".proc";
        }
        return filename;
    }

    /** Talend 6.4 replaces _proc by -proc (bug?) */
   /* public String getProcFilenameTalend6() {
        return this.path + File.separatorChar + this.name + "-" + this.version + ".proc";
    }*/

    /** Path and name */
    public String getProcessFullPath() {
        return getPath() + "/" + this.name + " " + this.version;
    }

    public String getName() {
        return this.name;
    }

    public boolean isVersionLowerThan(TalendFile file) {
       // return Float.parseFloat(this.version) < Float.parseFloat(file.version);
        return versionIsLower(this.version, file.version);
    }
    private boolean versionIsLower(String v1, String v2) {
        String[] spl1 =v1.split("\\.");
        String[] spl2 =v2.split("\\.");
        for (int i=0; i<Math.min(spl1.length, spl2.length); i++) {
            int n1 = Integer.parseInt(spl1[i]);
            int n2 = Integer.parseInt(spl2[i]);
            if (n1<n2) return true;
            if (n1>n2) return false;
        }
        return true;
    }

    public String getVersion() {
        return this.version;
    }

    /**
     *
     * @return path after routes/, services/, process/
     */
    public String getPath() {
        if (path.contains("workflow")) {
            return "/"; // Workflow are stored as root (no nfolder)
        }
        int p = Math.max(path.indexOf("routes"), Math.max(path.indexOf("services"), path.indexOf("process")));
        p = Math.max(path.indexOf("/", p), path.indexOf('\\', p));
        return path.substring(p+1);
    }

    public boolean existThreeFiles() {
        // Note: services have .wsdl but no .screenshot
        return new File(getItemFilename()).exists() && new File(getPropertiesFilename()).exists() && (this.path.contains("services") || new File(getScreenshotFilename()).exists());
    }

    public String readPropertiesFileId() throws IOException {
        String properties = IOUtils.toString(new FileInputStream(getPropertiesFilename()));
        Matcher matcherItem = PAT_TALENDPROPERTIES_PROPERTY.matcher(properties);
        if (!matcherItem.find()) throw new RuntimeException("Format error reading process of " + getName());
        return matcherItem.group(1);
    }

    public String readServiceWSDLAddress() throws IOException {
        String wsdl = IOUtils.toString(new FileInputStream(getWSDLFilename()));
        Matcher matcherItem = Pattern.compile("soap:address location=\"(.*?)\"").matcher(wsdl);
        if (!matcherItem.find()) throw new RuntimeException("Format error reading process of " + getName());
        return matcherItem.group(1);
    }


}