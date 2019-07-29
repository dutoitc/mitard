package ch.mno.talend.mitard.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class TMDMInputType extends AbstractNodeType {


    private String concept;

    @Override
    public String toString() {
        return "TMDMInputType[" + getUniqueName() + "]";
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getConcept() {
        return concept;
    }
}
