package ch.mno.talend.mitard.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class ProcessType {

    private List<AbstractNodeType> nodeList = new ArrayList<>();
    private Map<String, List<String>> connections = new HashMap<>();
    private Map<String, List<String>> connectionsParent = new HashMap<>();

    public void addNode(AbstractNodeType node) {
        nodeList.add(node);
    }

    public List<AbstractNodeType> getNodeList() {
        return nodeList;
    }

    public void addConnection(String source, String target) {
        if (connections.containsKey(source)) {
            connections.get(source).add(target);
        } else {
            List<String> arr = new ArrayList<>();
            arr.add(target);
            connections.put(source, arr);
        }
        if (connectionsParent.containsKey(target)) {
            connectionsParent.get(target).add(source);
        } else {
            List<String> arr = new ArrayList<>();
            arr.add(source);
            connectionsParent.put(target, arr);
        }
    }

    public List<String> getConnections(String name) {
        return connections.get(name);
    }

    public Map<String, List<String>> getConnections() {
        return connections;
    }

    public boolean isConnectedToPrejob(String componentName) {
        List<String> parents = new ArrayList<>();
        if (connectionsParent.get(componentName)!=null){
            parents.addAll(connectionsParent.get(componentName));
        }
        String parent = componentName;
        while (!parents.isEmpty()) {
            parent = parents.remove(0);
            if (connectionsParent.get(parent)!=null) {
                if (connectionsParent.get(parent)!=null) {
                    parents.addAll(connectionsParent.get(parent));
                }
            }
        }
        return parent.startsWith("tPrejob");
    }
}
