package ch.mno.talend.mitard.out;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dutoitc on 15.05.2015.
 */
public class JsonFileViolations {

    /** Route, service or process name */
    private String name;

    private List<JsonViolationEnum> generalViolations = new ArrayList<>();
    private Map<String, List<JsonViolationEnum>> componentViolations = new HashMap<>();


    public JsonFileViolations(String name) {
        this.name = name;
    }

    public void addGeneralViolation(JsonViolationEnum violation) {
        generalViolations.add(violation);
    }

    public void addComponentViolation(String componentName, JsonViolationEnum violation) {
        if (componentViolations.containsKey(componentName)) {
            componentViolations.get(componentName).add(violation);
        } else {
            List<JsonViolationEnum> list = new ArrayList<>();
            list.add(violation);
            componentViolations.put(componentName, list);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JsonViolationEnum> getGeneralViolations() {
        return generalViolations;
    }

    public void setGeneralViolations(List<JsonViolationEnum> generalViolations) {
        this.generalViolations = generalViolations;
    }

    public Map<String, List<JsonViolationEnum>> getComponentViolations() {
        return componentViolations;
    }

    public void setComponentViolations(Map<String, List<JsonViolationEnum>> componentViolations) {
        this.componentViolations = componentViolations;
    }

    public boolean hasViolations() {
        return !generalViolations.isEmpty() || !componentViolations.isEmpty();
    }
}
