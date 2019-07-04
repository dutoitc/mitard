package ch.mno.talend.mitard.readers.treaders;

import ch.mno.talend.mitard.data.TRunJobType;

public class TRunJobReader extends AbstractTReader {

        TRunJobType obj;

        public TRunJobReader(String componentName) {
            super(new TRunJobType(), componentName);
            obj = (TRunJobType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
            switch (name) {
                case "PROCESS":
                    obj.setProcessName(value);
                    break;
                case "PROCESS:PROCESS_TYPE_VERSION":
                    obj.setProcessVersion(value);
                    break;
                case "PROPAGATE_CHILD_RESULT":
                    obj.setPropagateChildResult(value);
                    break;
            }
        }
    }