package ch.mno.talend.mitard.writers.dependencies;

import ch.mno.talend.mitard.data.Context;

import static ch.mno.talend.mitard.writers.dependencies.DependencyKey.TYPE.PROCESS;
import static ch.mno.talend.mitard.writers.dependencies.DependencyKey.TYPE.ROUTE;
import static ch.mno.talend.mitard.writers.dependencies.DependencyKey.TYPE.SERVICE;
import static ch.mno.talend.mitard.writers.dependencies.DependencyKey.TYPE.WORKFLOW;

/**
 * Created by dutoitc on 04.03.2019.
 */
public class DependencyKey {



    enum TYPE {
        PROCESS("P"), ROUTE("R"), SERVICE("S"), WORKFLOW("B");
        TYPE(String prefix) {
            this.prefix = prefix;
        }
        private String prefix;
    }

    private TYPE type;
    private String name;
    private String version;
    private String normalized;

    public DependencyKey(TYPE type, String name, String version, Context context) {
        name = (""+name.charAt(0)).toUpperCase()+name.substring(1);
        this.type = type;
        this.name = name;
        this.version = version;
        this.normalized = type.prefix + '_' + normalize(context, name, version);
    }


    public static DependencyKey buildProcessKey(String name, String version, Context context) {
        return new DependencyKey(PROCESS, name, version, context);
    }
    public static DependencyKey buildRouteKey(String name, String version, Context context) {
        return new DependencyKey(ROUTE, name, version, context);
    }
    public static DependencyKey buildServiceKey(String name, String version, Context context) {
        return new DependencyKey(SERVICE, name, version, context);
    }
    public static DependencyKey buildWorkflowKey(String name, String version, Context context) {
        return new DependencyKey(WORKFLOW, name, version, context);
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getTypeName() {
        return type.prefix + '_' + name;
    }

    public String getNormalized() {
        return normalized;
    }

    /** Normalize name, version to [name]_[version] with replacements. '-' and '.' to' _'.*/
    private String normalize(Context context, String name, String version) {
        if (version == null) return "";

        // Convert context.xxx to the value given by xxx in context/properties file
        if (version.startsWith("context")) {
            version = context.getProjectProperties(version.substring(8));
        }
        String s = name + "_" + version;
        return s.replace("-", "_").replace(".", "_");
    }

}
