package ch.mno.talend.mitard.readers;

import ch.mno.talend.mitard.data.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Note: seems to be impossible to work with XSD for Talend file (namespace not correct ?), so a parse is necessary.
 */
public class ProcessReader extends DefaultHandler {


    public static Logger LOG = LoggerFactory.getLogger(ProcessReader.class);

    private ProcessType process;
    private String path = "";
    private AbstractTReader reader;

    public void startDocument() throws SAXException {
        process = new ProcessType();
    }

    public void endDocument() throws SAXException {

    }

    public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
            throws SAXException {
        int p = localName.indexOf(":");
        path = path + "/" + localName.substring(p + 1);
//        System.out.println("Start element " + path);

        if (localName.equals("node")) {
            String componentName = getAttribute(attributes, "componentName");
            switch (componentName) {
                case "tRESTRequest":
                    reader = new TRestRequestReader(componentName);
                    break;
                case "tESBProviderRequest":
                    reader = new TESBProviderRequestReader(componentName);
                    break;
                case "tESBConsumer":
                    reader = new TESBConsumerReader(componentName);
                    break;
                case "tRunJob":
                    reader = new TRunJobReader(componentName);
                    break;
                case "cTalendJob":
                    reader = new CTalendJobReader(componentName);
                    break;
                case "tOracleCommit":
                    reader = new TOracleCommitReader(componentName);
                    break;
                case "tMDMCommit":
                    reader = new TMDMCommitReader(componentName);
                    break;
                case "tJava":
                    reader = new TJavaReader(componentName);
                    break;
                case "tJavaRow":
                    reader = new TJavaRowReader(componentName);
                    break;
                case "tJavaFlex":
                    reader = new TJavaFlexReader(componentName);
                    break;
                case "tBonitaInstantiateProcess":
                    reader = new TBonitaInstanciateProcessReader(componentName);
                    break;
                case "tDie":
                    reader = new TDieReader(componentName);
                    break;
                case "tFixedFlowInput":
                    reader = new TFixedFlowInputReader(componentName);
                    break;
                case "tOracleInput":
                    reader = new TOracleInputReader(componentName);
                    break;
                case "tOracleOutput":
                    reader = new TOracleOutputReader(componentName);
                    break;
                case "tMDMConnection":
                    reader = new TMDMConnectionReader(componentName);
                    break;
                case "tREST":
                    reader = new TRest(componentName);
                    break;
                case "tRESTClient":
                    reader = new TRestClient(componentName);
                    break;
                case "tHttpRequest":
                    reader = new THTTPRequest(componentName);
                    break;
                case "tSOAP":
                    reader = new TSoap(componentName);
                    break;
                case "tOracleConnection":
                    reader = new TOracleConnection(componentName);
                    break;
                case "tOracleRollback":
                    reader = new TOracleRollback(componentName);
                    break;
                case "tPrejob":
                    reader = new TPrejob(componentName);
                    break;
                case "tOracleClose":
                    reader = new TOracleClose(componentName);
                    break;
                case "tMDMClose":
                    reader = new TMDMClose(componentName);
                    break;
                case "tMDMRollback":
                    reader = new TMDMRollback(componentName);
                    break;
                case "tFileOutputJSON":
                case "tFileInputPositional":
                case "tLogRow":
                case "tXMLMap":
                case "tFlowToIterate":
                case "tMysqlInput":
                case "tRowGenerator":
                case "tMysqlOutput":
                case "tESBProviderResponse":
                case "tRESTResponse":
                case "tMDMInput":
                case "tPostjob":
                case "tMDMOutput":
                case "tMDMInputNullOptional":
                case "tFileInputRaw":
                case "tHashInput":
                case "tRouteOutput":
                case "tMomOutput":
                case "tFileOutputXML":
                case "tESBProviderFault":
                case "tMap":
                case "tHashOutput":
                case "tExtractXMLField":
                case "tSetGlobalVar":
                case "tConvertType":
                case "tLibraryLoad":
                case "tAssertCatcher":
                case "tLogCatcher":
                case "tFileOutputMSXML":
                case "tNormalize":
                case "RCEntLog4jLogger":
                case "tUniqRow":
                case "tUnite":
                case "tAggregateRow":
                case "tBufferInput":
                case "tFilterRow":
                case "tOracleRow":
                case "tBufferOutput":
                case "tLogXML":
                case "tXMLInsert":
                case "tRouteInput":
                case "tWarn":
                case "tStatCatcher":
                case "tAssert":
                case "tMomRollback":
                case "tReplicate":
                case "tWriteXMLField":
                case "tCreateTable":
                case "tExtractJSONFields":
                case "tSendMail":
                case "tFilterColumns":
                case "tFlowMeterCatcher":
                case "tFileCopy":
                case "tSleep":
                case "tMomConnection":
                case "tFileDelete":
                case "tFileInputXML":
                case "tCreateTemporaryFile":
                case "tMDMViewSearch":
                case "tOracleTableList":
                case "tMDMTriggerInput":
                case "tParallelize":
                case "tContextLoad":
                case "tLoop":
                case "tMDMDelete":
                case "tFileOutputDelimited":
                case "tSortRow":
                case "tFileInputDelimited":
                case "tFileList":
                case "tXSDValidator":
                case "cJMS":
                case "cMessageRouter":
                case "cMessagingEndpoint":
                case "cLog":
                case "cConfig":
                case "cSetHeader":
                case "cFile":
                case "cBeanRegister":
                case "cConvertBodyTo":
                case "cOnException":
                case "cMQConnectionFactory":
                case "cJavaDSLProcessor":
                case "cProcessor":
                case "tChronometerStart":
                case "tChronometerStop":
                case "tFileOutputExcel":
                case "tFileInputExcel":
                case "tJoin":
                case "tWriteJSONField":
                case "tSynonymSearch":
                case "tMultiPatternCheck":
                case "tSampleRow":
                case "tMDMBulkLoad":
                case "tSplitRow":
                case "tAdvancedFileOutputXML":
                case "tDenormalize":
                case "tMomCommit":
                case "cCXFRS":
                case "cSetBody":
                case "cHttp":
                case "tFileOutputRaw":
                case "tFileInputFullRow":
                case "tFileExist":
                case "tSynonymOutput":
                case "tASsert":
                case "tMDMReceive":
                case "tSchemaComplianceCheck":
                case "tInfiniteLoop":
                case "cREST": // Note: URL could be read and correlated with service
                    reader = new TNodeReader(componentName);
                    break;
                default:
                    if (componentName.startsWith("c") || componentName.startsWith("t")) {
                        LOG.warn("Missing read for " + componentName);
                    } else {
                        // Application components
                        LOG.trace("Missing read for " + componentName);
                    }
            }
        } else if (localName.equals("connection")) {
            process.addConnection(getAttribute(attributes, "source"), getAttribute(attributes, "target"));
        }

        if (reader != null) {
            reader.startElement(localName, attributes);
        }
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        // Keep lastPath, current full path
        int p = path.lastIndexOf("/");
        String lastPath = path.substring(p + 1);
        path = path.substring(0, p);

        if (lastPath.equals("node") && reader != null) {
            process.addNode(reader.getNode());
            reader = null;
        }
        //buffer = "";
    }


    public void characters(char ch[], int start, int length)
            throws SAXException {
       // buffer += new String(ch, start, length);
    }


    private String getAttribute(Attributes attributes, String name) {
        for (int i = 0; i < attributes.getLength(); i++) {
            if (attributes.getLocalName(i).equals(name)) {
                return attributes.getValue(i);
            }
        }
        return null;
    }


    // ========================================================================================

    private abstract class AbstractTReader {

        AbstractNodeType obj;

        public AbstractTReader(AbstractNodeType obj, String componentName) {
            this.obj = obj;
            obj.setComponentName(componentName);
        }

        public void startElement(String localName, Attributes attributes) {
            if (localName.equals("elementParameter")) {
                String name = getAttribute(attributes, "name");
                String value = getAttribute(attributes, "value");

                if ("UNIQUE_NAME".equals(name)) {
                    obj.setUniqueName(value);
                } else if ("ACTIVE".equals(name)) {
                    obj.setActive("true".equals(value));
                } else if ("USE_EXISTING_CONNECTION".equals(name)) {
                    obj.setUseExistingConnection("true".equals(value));
                } else {
                    handleElement(name, value);
                }
            }
            if (localName.equals("elementValue")) {
                String name = getAttribute(attributes, "elementRef");
                String value = getAttribute(attributes, "value");
                handleElement(name, value);
            }
        }

        protected abstract void handleElement(String name, String value);

        private AbstractNodeType getNode() {
            return obj;
        }
    }


    // ========================================================================================

    private class TNodeReader extends AbstractTReader {

        TNodeType obj;

        public TNodeReader(String componentName) {
            super(new TNodeType(), componentName);
            obj = (TNodeType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
        }


        public void startElement(String localName, Attributes attributes) {
            super.startElement(localName, attributes);
            if ("NODE".equals(localName.toUpperCase())) {
                String x = attributes.getValue("posX");
                if (StringUtils.isNotBlank(x)) obj.setX(Integer.parseInt(x));
                String y = attributes.getValue("posY");
                if (StringUtils.isNotBlank(y)) obj.setY(Integer.parseInt(y));
            } else if ("ELEMENTPARAMETER".equals(localName.toUpperCase())) {
                obj.addElementParameter(attributes.getValue("field"), attributes.getValue("name"), attributes.getValue("value"));
            }
        }

    }


    // ========================================================================================

    private class TRestRequestReader extends AbstractTReader {

        TRestRequestType obj;

        public TRestRequestReader(String componentName) {
            super(new TRestRequestType(), componentName);
            obj = (TRestRequestType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
            switch (name) {
                case "REST_ENDPOINT":
                    if (value.charAt(0) == '"') value = value.substring(1, value.length() - 1);
                    obj.setEndpointURI(value);
                    break;
                default:
            }
        }
    }
    // ========================================================================================

    private class TESBConsumerReader extends AbstractTReader {

        TESBConsumerType obj;

        public TESBConsumerReader(String componentName) {
            super(new TESBConsumerType(), componentName);
            obj = (TESBConsumerType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
            switch (name) {
                case "ESB_ENDPOINT":
                    if (value.charAt(0) == '"') value = value.substring(1, value.length() - 1);
                    obj.setEndpointURI(value);
                    break;
                case "SERVICE_NAME":
                    obj.setServiceName(value);
                default:
            }
        }
    }

    // ========================================================================================

    private class CTalendJobReader extends AbstractTReader {

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

    // ========================================================================================

    private class TRunJobReader extends AbstractTReader {

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

    // ========================================================================================

    private class TOracleCommitReader extends AbstractTReader {

        TOracleCommitType obj;

        public TOracleCommitReader(String componentName) {
            super(new TOracleCommitType(), componentName);
            obj = (TOracleCommitType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
            switch (name) {
                case "CLOSE":
                    obj.setClose("true".equals(value));
                    break;
                case "CONNECTION":
                    obj.setConnection(value);
                    break;
            }
        }
    }
    // ========================================================================================

    private class TMDMCommitReader extends AbstractTReader {

        TMDMCommitType obj;

        public TMDMCommitReader(String componentName) {
            super(new TMDMCommitType(), componentName);
            obj = (TMDMCommitType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
            switch (name) {
                case "CLOSE":
                    obj.setClose("true".equals(value));
                    break;
                case "CONNECTION":
                    obj.setConnection(value);
                    break;
            }
        }
    }

    // ========================================================================================

    private class TMDMConnectionReader extends AbstractTReader {

        TMDMConnectionType obj;

        public TMDMConnectionReader(String componentName) {
            super(new TMDMConnectionType(), componentName);
            obj = (TMDMConnectionType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
            switch (name) {
                case "AUTO_COMMIT":
                    obj.setAutoCommit("true".equals(value));
                    break;
            }
        }
    }

    // ========================================================================================

    private class TRest extends AbstractTReader {

        TRestType obj;

        public TRest(String componentName) {
            super(new TRestType(), componentName);
            obj = (TRestType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
            switch (name) {
                case "URL":
                    obj.setUrl(value);
                    break;
            }
        }
    }
    // ========================================================================================

    private class TRestClient extends AbstractTReader {

        TRestClientType obj;

        public TRestClient(String componentName) {
            super(new TRestClientType(), componentName);
            obj = (TRestClientType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
            switch (name) {
                case "URL":
                    obj.setUrl(value);
                    break;
                case "PATH":
                    obj.setPath(value);
                    break;
            }
        }
    }
    // ========================================================================================

    private class THTTPRequest extends AbstractTReader {

        THttpRequestType obj;

        public THTTPRequest(String componentName) {
            super(new THttpRequestType(), componentName);
            obj = (THttpRequestType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
            switch (name) {
                case "URI":
                    obj.setUri(value);
                    break;
            }
        }
    }
    // ========================================================================================

    private class TOracleConnection extends AbstractTReader {

        TOracleConnectionType obj;

        public TOracleConnection(String componentName) {
            super(new TOracleConnectionType(), componentName);
            obj = (TOracleConnectionType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
            switch (name) {
                case "AUTO_COMMIT":
                    obj.setAutoCommit("TRUE".equalsIgnoreCase(value));
                    break;
            }
        }
    }
    // ========================================================================================

    private class TOracleRollback extends AbstractTReader {

        TOracleRollbackType obj;

        public TOracleRollback(String componentName) {
            super(new TOracleRollbackType(), componentName);
            obj = (TOracleRollbackType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
            switch (name) {
                case "CLOSE":
                    obj.setClose("true".equalsIgnoreCase(value));
                    break;
                case "CONNECTION":
                    obj.setConnection(value);
                    break;
            }
        }
    }
    // ========================================================================================

    private class TMDMRollback extends AbstractTReader {

        TMDMRollbackType obj;

        public TMDMRollback(String componentName) {
            super(new TMDMRollbackType(), componentName);
            obj = (TMDMRollbackType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
            switch (name) {
                case "CLOSE":
                    obj.setClose("true".equalsIgnoreCase(value));
                    break;
                case "CONNECTION":
                    obj.setConnection(value);
                    break;
            }
        }
    }
    // ========================================================================================

    private class TPrejob extends AbstractTReader {

        TPrejobType obj;

        public TPrejob(String componentName) {
            super(new TPrejobType(), componentName);
            obj = (TPrejobType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
        }
    }
    // ========================================================================================

    private class TOracleClose extends AbstractTReader {

        TOracleCloseType obj;

        public TOracleClose(String componentName) {
            super(new TOracleCloseType(), componentName);
            obj = (TOracleCloseType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
            switch (name) {
                case "CONNECTION":
                    obj.setConnection(value);
                    break;
            }
        }
    }
    // ========================================================================================

    private class TMDMClose extends AbstractTReader {

        TMDMCloseType obj;

        public TMDMClose(String componentName) {
            super(new TMDMCloseType(), componentName);
            obj = (TMDMCloseType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
            switch (name) {
                case "CONNECTION":
                    obj.setConnection(value);
                    break;
            }
        }
    }
    // ========================================================================================

    private class TSoap extends AbstractTReader {

        TSoapType obj;

        public TSoap(String componentName) {
            super(new TSoapType(), componentName);
            obj = (TSoapType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
            switch (name) {
                case "ENDPOINT":
                    obj.setEndpoint(value);
                    break;
                case "ACTION":
                    obj.setAction(value);
                    break;
            }
        }
    }
    // ========================================================================================

    private class TJavaReader extends AbstractTReader {

        TJavaType obj;

        public TJavaReader(String componentName) {
            super(new TJavaType(), componentName);
            obj = (TJavaType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
            switch (name) {
                case "CODE":
                    obj.setCode(value);
                    break;
            }
        }
    }
    // ========================================================================================

    private class TJavaRowReader extends AbstractTReader {

        TJavaRowType obj;

        public TJavaRowReader(String componentName) {
            super(new TJavaRowType(), componentName);
            obj = (TJavaRowType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
            switch (name) {
                case "CODE":
                    obj.setCode(value);
                    break;
            }
        }
    }


    // ========================================================================================

    private class TBonitaInstanciateProcessReader extends AbstractTReader {

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
    // ========================================================================================

    private class TJavaFlexReader extends AbstractTReader {

        TJavaFlexType obj;

        public TJavaFlexReader(String componentName) {
            super(new TJavaFlexType(), componentName);
            obj = (TJavaFlexType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
            switch (name) {
                case "CODE_START":
                    obj.setCodeStart(value);
                    break;
                case "CODE_MAIN":
                    obj.setCodeMain(value);
                    break;
                case "CODE_END":
                    obj.setCodeEnd(value);
                    break;
            }
        }
    }
    // ========================================================================================

    private class TDieReader extends AbstractTReader {

        TDieType obj;

        public TDieReader(String componentName) {
            super(new TDieType(), componentName);
            obj = (TDieType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
            switch (name) {
            case "MESSAGE":
                obj.setMessage(value);
                break;
            }
        }
    }

    // ========================================================================================

    private class TOracleInputReader extends AbstractTReader {

        TOracleInputType obj;

        public TOracleInputReader(String componentName) {
            super(new TOracleInputType(), componentName);
            obj = (TOracleInputType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
            switch (name) {
            case "DBTABLE":
                obj.setDbTable(value);
                break;
            case "QUERY":
                obj.setQuery(value);
                break;
            }
        }
    }

    // ========================================================================================

    private class TOracleOutputReader extends AbstractTReader {

        TOracleOutputType obj;

        public TOracleOutputReader(String componentName) {
            super(new TOracleOutputType(), componentName);
            obj = (TOracleOutputType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
            switch (name) {
            case "DBTABLE":
                obj.setDbTable(value);
                break;
            case "SCHEMA_COLUMN":
                obj.addSchemaColumn(value);
                break;
            }
        }
    }

    // ========================================================================================

    private class TFixedFlowInputReader extends AbstractTReader {

        TFixedFlowInputType obj;

        public TFixedFlowInputReader(String componentName) {
            super(new TFixedFlowInputType(), componentName);
            obj = (TFixedFlowInputType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
            switch (name) {
            case "VALUE":
            case "SCHEMA_COLUMN":
                obj.addText(value);
                break;
            }
        }
    }

    // ========================================================================================

    private class TESBProviderRequestReader extends AbstractTReader {

        TESBConsumerType obj;

        public TESBProviderRequestReader(String componentName) {
            super(new TESBConsumerType(), componentName);
            obj = (TESBConsumerType) super.getNode();
        }

        @Override
        protected void handleElement(String name, String value) {
            switch (name) {
                case "ENDPOINT_URI":
                    if (value.charAt(0) == '"') value = value.substring(1, value.length() - 1);
                    obj.setEndpointURI(value);
                    break;
                case "SERVICE_NAME":
                    obj.setServiceName(value);
                default:
            }
        }
    }


    // ========================================================================================


    public static ProcessType read(String file) throws IOException, SAXException, ParserConfigurationException {
        return read(new FileInputStream(file));
    }

    public static ProcessType read(InputStream xml) throws ParserConfigurationException, SAXException, IOException {
        if (xml==null) {
            throw new IOException("InputStream cannot be null");
        }

        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser saxParser = spf.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        ProcessReader reader = new ProcessReader();
        xmlReader.setContentHandler(reader);
        //InputSource is = new InputSource(xml);

        InputSource is = new InputSource();
        is.setCharacterStream(new InputStreamReader(xml, "UTF-8"));
        xmlReader.parse(is);
        return reader.process;
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
//        ProcessType process = read(new FileInputStream("C:\\projets\\talend-mitard\\mitard\\src\\main\\test\\resources\\ESBTUTORIALPROJECT\\process\\RESTService_0.3.item"));
        ProcessType process = read(new FileInputStream("C:\\projets\\talend-mitard\\mitard\\src\\main\\test\\resources\\ESBTUTORIALPROJECT\\process\\GreetingServiceConsumer_0.1.item"));
        for (AbstractNodeType node : process.getNodeList()) {
            System.out.println(node.toString());
        }
    }

}
