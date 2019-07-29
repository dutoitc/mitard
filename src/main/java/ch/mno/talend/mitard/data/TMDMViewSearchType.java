package ch.mno.talend.mitard.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class TMDMViewSearchType extends AbstractNodeType {

    private List<String> operations = new ArrayList<>();

    public void addOperation(String operation) {
        operations.add(operation);
    }

    public List<String> getOperations() {
        return operations;
    }

    @Override
    public String toString() {
        return "TMDMViewSearchType[" + getUniqueName() + "]";
    }

}
