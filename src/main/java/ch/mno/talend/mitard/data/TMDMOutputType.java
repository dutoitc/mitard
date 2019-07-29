package ch.mno.talend.mitard.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class TMDMOutputType extends AbstractNodeType {

    private List<String> path = new ArrayList<>();
    private boolean withReport;

    public void addPath(String operation) {
        path.add(operation);
    }

    public List<String> getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "TMDMOutputType[" + getUniqueName() + "]";
    }

    public boolean isWithReport() {
        return withReport;
    }

    public void setWithReport(boolean withReport) {
        this.withReport = withReport;
    }
}
