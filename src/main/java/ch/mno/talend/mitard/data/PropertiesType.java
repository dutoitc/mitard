package ch.mno.talend.mitard.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class PropertiesType {

    private List<AbstractNodeType> nodeList = new ArrayList<>();
    private Map<String, List<String>> connections = new HashMap<>();

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
    }

    public List<String> getConnections(String name) {
        return connections.get(name);
    }

    public Map<String, List<String>> getConnections() {
        return connections;
    }
}
