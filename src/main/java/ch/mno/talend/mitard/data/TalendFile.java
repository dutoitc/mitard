package ch.mno.talend.mitard.data;

import java.io.File;

/** A Talend file (in fact, a component, with .item, .properties, .screenshot, .proc?) */
public class TalendFile {
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

    public String getScreenshotFilename() {
        return this.path + File.separatorChar + this.name + "_" + this.version + ".screenshot";
    }

    public String getProcFilename() {
        return this.path + File.separatorChar + this.name + "_" + this.version + ".proc";
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
        int p = Math.max(path.indexOf("routes"), Math.max(path.indexOf("services"), path.indexOf("process")));
        p = Math.max(path.indexOf("/", p), path.indexOf('\\', p));
        return path.substring(p+1);
    }

    public boolean existThreeFiles() {
        // Note: services have .wsdl but no .screenshot
        return new File(getItemFilename()).exists() && new File(getPropertiesFilename()).exists() && (this.path.contains("services") || new File(getScreenshotFilename()).exists());
    }
}
