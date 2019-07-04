package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TBonitaInstanciateProcessType;

public class TBonitaInstanciateProcessReader extends AbstractTReader {

    TBonitaInstanciateProcessType obj;

    public TBonitaInstanciateProcessReader(String componentName) {
        super(new TBonitaInstanciateProcessType(), componentName);
        obj = (TBonitaInstanciateProcessType) super.getNode();
    }

    @Override
    protected void handleElement(String name, String value) {
        switch (name) {
            case "PROCESS_NAME":
                obj.setProcessName(value);
                break;
            case "PROCESS_VERSION":
                obj.setProcessVersion(value);
                break;
        }
    }
}