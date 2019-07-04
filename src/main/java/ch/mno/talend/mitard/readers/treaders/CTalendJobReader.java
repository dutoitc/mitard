package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.CTalendJobType;

public class CTalendJobReader extends AbstractTReader {

    CTalendJobType obj;

    public CTalendJobReader(String componentName) {
        super(new CTalendJobType(), componentName);
        obj = (CTalendJobType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
        switch (name) {
            case "SELECTED_JOB_NAME":
                obj.setProcessName(value);
                break;
            case "SELECTED_JOB_NAME:PROCESS_TYPE_VERSION":
                obj.setProcessVersion(value);
                break;
        }
    }
}