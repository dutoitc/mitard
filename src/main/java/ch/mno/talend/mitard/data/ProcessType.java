package ch.mno.talend.mitard.data;

import java.util.*;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class ProcessType {

    private List<AbstractNodeType> nodeList = new ArrayList<>();
    private Map<String, List<ConnectionType>> connections = new HashMap<>();
    private Map<String, List<ConnectionType>> connectionsParent = new HashMap<>();
    private Set<String> contextNames = new HashSet<>();
    private boolean usesStatCatcher;

    public void addNode(AbstractNodeType node) {
        nodeList.add(node);
    }

    public List<AbstractNodeType> getNodeList() {
        return nodeList;
    }


    public void addConnection(ConnectionType ct) {
        if (connections.containsKey(ct.getSource())) {
            connections.get(ct.getSource()).add(ct);
        } else {
            List<ConnectionType> arr = new ArrayList<>();
            arr.add(ct);
            connections.put(ct.getSource(), arr);
        }
        if (connectionsParent.containsKey(ct.getTarget())) {
            connectionsParent.get(ct.getTarget()).add(ct);
        } else {
            List<ConnectionType> arr = new ArrayList<>();
            arr.add(ct);
            connectionsParent.put(ct.getTarget(), arr);
        }
    }


    public List<ConnectionType> getConnections(String name) {
        List<ConnectionType> conn = connections.get(name);
        if (conn==null) {
            return new ArrayList<>();
        }
        return conn;
    }

    public Map<String, List<ConnectionType>> getConnections() {
        return connections;
    }

    public boolean isConnectedToPrejob(String componentName) {
        List<ConnectionType> childrenOfPrejob = connections.get("tPrejob_1");
        if (childrenOfPrejob==null) return false;
        while (!childrenOfPrejob.isEmpty()) {
            String child = childrenOfPrejob.remove(0).getTarget();
            if (child.equals(componentName)) return true;
            List<ConnectionType> c = connections.get(child);
            if (c!=null) {
                childrenOfPrejob.addAll(c);
            }
        }
        return false;
    }

    public void setUseStatCatcher() {
        usesStatCatcher = true;
    }

    public boolean isUsesStatCatcher() {
        return usesStatCatcher;
    }

    public void addContextName(String name) {
        contextNames.add(name);
    }

    public Set<String> getContextNames() {
        return contextNames;
    }

}
