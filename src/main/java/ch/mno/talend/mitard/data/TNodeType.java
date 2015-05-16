package ch.mno.talend.mitard.data;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class TNodeType extends AbstractNodeType {


    @Override
    public String toString() {
        return "(node)["+getUniqueName()+"]";
    }
}
