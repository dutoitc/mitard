package ch.mno.talend.mitard.writers.dependencies;

import ch.mno.talend.mitard.data.TalendFile;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Build DOT, grouping elements by path and name by regexp
 * Created by dutoitc on 04.03.2019.
 */
// TODO: support path regex, or entry point regex (need two pass to include downlink dependencies)
public class CustomDotBuilder {

    public static final String GROUP_VARIOUS = "Various";
    private StringBuilder sbRelations = new StringBuilder();
    private Map<String, StringBuilder> sbGroups = new HashMap<>();
    private List<Pair<Pattern, String>> groups = new ArrayList<>();
    private Set<String> declaredObjects = new HashSet<>();
    private String dotName;


    /**
     *
     * @param dependenciesData
     * @param customBuildDefinition name;regex1;group1;regex2;group2;...;regexN;groupN
     */
    public CustomDotBuilder(DependenciesData dependenciesData, String customBuildDefinition) {
        String[] spl = customBuildDefinition.split(";");
        if ((spl.length-1)%2!=0) throw new RuntimeException("Wrong custom dot definition: " + customBuildDefinition);
        dotName = spl[0];

        // Init groups by pattern
        for (int i=1; i<spl.length; i+=2) {
            groups.add(new ImmutablePair<>(Pattern.compile(spl[i]), spl[i+1]));
        }

        for (int step=0; step<=1; step++) {
            addCode(dependenciesData, "house", dependenciesData.getWorkflowDependencies(), step);
            addCode(dependenciesData, "trapezium", dependenciesData.getRouteDependencies(), step);
            addCode(dependenciesData, "ellipse", dependenciesData.getServiceDependencies(), step);
            addCode(dependenciesData, "box", dependenciesData.getProcessDependencies(), step);
        }
    }

    /** Step0= objects; step1 = dependencies (need objects) */
    private void addCode(DependenciesData dependenciesData, String type, Map<String, List<String>> dependencies, int step) {
        for (Map.Entry<String, List<String>> entry: dependencies.entrySet()) {
            String code = buildObjectCode(type, entry.getKey());
            String nameForGroup = findNameForGroup(dependenciesData, entry.getKey());
            String group = findGroup(nameForGroup);

            // On ne déclare pas les objets sans groupes. Ils seront crées au step1 par la relation x->y
            if (step==0 && !GROUP_VARIOUS.equals(group)) {
                declaredObjects.add(entry.getKey());
                addObjectToGroup(nameForGroup, group, code);
            }

            if (step==1 && !GROUP_VARIOUS.equals(group)) {
                for (String target : entry.getValue()) {
                    if (StringUtils.isEmpty(target)) continue;
                    if (!declaredObjects.contains(target)) {
                        String codeTarget = buildObjectCode(type, entry.getKey()); // Supose same type TODO: find type from name S_, P_... ?
                        String nameForGroupTarget = findNameForGroup(dependenciesData, target);
                        String groupTarget = findGroup(nameForGroupTarget);
                        declaredObjects.add(target);
                        addObjectToGroup(nameForGroupTarget, groupTarget, codeTarget);
                    }
                    sbRelations.append("   " + entry.getKey() + "->" + target + "\r\n");
                }
            }
        }
    }

    private String findNameForGroup(DependenciesData dependenciesData, String name) {
        TalendFile talendFile = dependenciesData.findTalendFile(name);
        String nameForGroup = name;

        if (talendFile==null) {
            talendFile = dependenciesData.guessTalendFileLatestVersion(name);
        }

        if (talendFile!=null) {
            nameForGroup = talendFile.getPath()+"."+nameForGroup;
        }
        return nameForGroup;
    }


    /** Find dot group matching regex */
    private String findGroup(String name) {
        for (Pair<Pattern, String> x: groups) {
            if (x.getKey().matcher(name).matches()) {
                return x.getValue();
            }
        }
        return GROUP_VARIOUS;
    }

    /** Add object code to matching group */
    private void addObjectToGroup(String name, String group, String content) {
        if (group==null) group="External";
        if (!sbGroups.containsKey(group)) {
            sbGroups.put(group, new StringBuilder());
        }
        sbGroups.get(group).append(content);
    }


    private String buildObjectCode(String shape, String name) {
        StringBuilder sb = new StringBuilder();
        sb.append(name + " [shape=").append(shape).append(", style=filled, fillcolor=green, color=green, label=\"" + name + "\"];\r\n");
        return sb.toString();
    }


    public String getDot() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph G{\r\n");
        //sb.append("graph [rankdir=LR, fontsize=10, margin=0.001];\r\n");
        sb.append("graph [rankdir=LR];\r\n");


        for (Map.Entry<String, StringBuilder> entry: sbGroups.entrySet()) {
			if (GROUP_VARIOUS.equals(entry.getKey())) continue; // Special group not to be displayed
            sb.append("subgraph cluster_").append(entry.getKey()).append("{ rankdir=LR\r\n").append(entry.getValue()).append("}\r\n");

        }

        sb.append("\r\n");
        sb.append(sbRelations.toString());

        sb.append("}\r\n");
        return sb.toString();
    }


}
