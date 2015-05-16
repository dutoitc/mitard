package ch.mno.talend.mitard.data;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class TJavaFlexType extends AbstractNodeType {

    private String codeStart;
    private String codeMain;
    private String codeEnd;

    @Override
    public String toString() {
        return "TJavaFlex["+getUniqueName()+"]";
    }

    public String getCodeStart() {
        return codeStart;
    }

    public void setCodeStart(String codeStart) {
        this.codeStart = codeStart;
    }

    public String getCodeMain() {
        return codeMain;
    }

    public void setCodeMain(String codeMain) {
        this.codeMain = codeMain;
    }

    public String getCodeEnd() {
        return codeEnd;
    }

    public void setCodeEnd(String codeEnd) {
        this.codeEnd = codeEnd;
    }

}