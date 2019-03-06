package ch.mno.talend.mitard.writers.dependencies;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dutoitc on 04.03.2019.
 */
public class DotBuilder {

    public static String buildDot(DependenciesData dependenciesData) {
        StringBuffer sb = new StringBuffer();
        sb.append("digraph G{\r\n");
        sb.append("graph [rankdir=LR, fontsize=10, margin=0.001];\r\n");

        // Count number of relations
        Map<String, Integer> count = new HashMap<>();
        List<Map<String, List<String>>> sources = new ArrayList<>();
        sources.add(dependenciesData.getProcessDependencies());
        sources.add(dependenciesData.getRouteDependencies());
        sources.add(dependenciesData.getServiceDependencies());
        sources.add(dependenciesData.getWorkflowDependencies());
        for (Map<String, List<String>> source: sources) {
            for (Map.Entry<String, List<String>> entry : source.entrySet()) {
                if (count.containsKey(entry.getKey())) {
                    count.put(entry.getKey(), count.get(entry.getKey())+1);
                } else {
                    count.put(entry.getKey(), 1);
                }

                for (String target : entry.getValue()) {
                    if (count.containsKey(target)) {
                        count.put(target, count.get(target)+1);
                        count.put(entry.getKey(), count.get(entry.getKey())+1);
                    } else {
                        count.put(target, 2); // Count link to avoid orphan
                        count.put(entry.getKey(), count.get(entry.getKey())+1);
                    }
                }
            }
        }

        // Boxes
        for (Map<String, List<String>> source: sources) {
            String shape="box";
            boolean rankSame=false;
            if (source== dependenciesData.getProcessDependencies()) {
                shape="box";
            }
            if (source== dependenciesData.getRouteDependencies()) {
                shape="trapezium";
                rankSame=true;
            }
            if (source== dependenciesData.getServiceDependencies()) {
                shape="ellipse";
            }
            if (source== dependenciesData.getWorkflowDependencies()) {
                shape="house";
                rankSame=true;
            }

            if (rankSame) sb.append("{ rank=same; \n");
            for (String name : source.keySet()) {
                if (count.get(name)>1)
                    sb.append("   " + name + " [shape=").append(shape).append(", style=filled, fillcolor=green, color=green, label=\"" + name + "\"];\r\n");
            }
            if (rankSame) sb.append("}\n");
        }

        // Boxes
        /*for (String name : processDependencies.keySet()) {
            if (count.get(name)>1)
            sb.append("   " + name + " [shape=box, style=filled, fillcolor=green, color=green, label=\"" + name + "\"];\r\n");
        }
        sb.append("{ rank=same; \n");
        for (String name : routeDependencies.keySet()) {
            if (count.get(name)>1)
            sb.append("   " + name + " [shape=trapezium, style=filled, fillcolor=green, color=green, label=\"" + name + "\"];\r\n");
        }
        sb.append("}\n");
        for (String name : serviceDependencies.keySet()) {
            if (count.get(name)>1)
            sb.append("   " + name + " [shape=ellipse, style=filled, fillcolor=green, color=green, label=\"" + name + "\"];\r\n");
        }
        for (String name : workflowDependencies.keySet()) {
            if (count.get(name)>1)
            sb.append("   " + name + " [shape=house, style=filled, fillcolor=green, color=green, label=\"" + name + "\"];\r\n");
        }*/

        // Isolated boxes / orpheans
        sb.append("subgraph cluster_orphans {\r\n");
        sb.append("rankdir=LR\r\n");
        for (String name : dependenciesData.getProcessDependencies().keySet()) {
            if (count.get(name)==1)
                sb.append("   " + name + " [shape=box, style=filled, fillcolor=green, color=green, label=\"" + name + "\"];\r\n");
        }
        for (String name : dependenciesData.getRouteDependencies().keySet()) {
            if (count.get(name)==1)
                sb.append("   " + name + " [shape=trapezium, style=filled, fillcolor=green, color=green, label=\"" + name + "\"];\r\n");
        }
        for (String name : dependenciesData.getServiceDependencies().keySet()) {
            if (count.get(name)==1)
                sb.append("   " + name + " [shape=ellipse, style=filled, fillcolor=green, color=green, label=\"" + name + "\"];\r\n");
        }
        for (String name : dependenciesData.getWorkflowDependencies().keySet()) {
            if (count.get(name)==1)
                sb.append("   " + name + " [shape=house, style=filled, fillcolor=green, color=green, label=\"" + name + "\"];\r\n");
        }
        sb.append("}\r\n");




        for (Map<String, List<String>> source: sources) {
            for (Map.Entry<String, List<String>> entry : source.entrySet()) {
                for (String target : entry.getValue()) {
                    if (!StringUtils.isEmpty(target)) {
                        sb.append("   " + entry.getKey() + "->" + target + "\r\n");
                    }
                }
            }
        }

        sb.append("}\n");
        return sb.toString();
    }


}
