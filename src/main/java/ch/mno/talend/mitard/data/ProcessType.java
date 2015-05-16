package ch.mno.talend.mitard.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class ProcessType {

    private List<AbstractNodeType> nodeList = new ArrayList<>();

    public void addNode(AbstractNodeType node) {
        nodeList.add(node);
    }

    public List<AbstractNodeType> getNodeList() {
        return nodeList;
    }

}
