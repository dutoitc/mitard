package ch.mno.talend.mitard.readers;

import ch.mno.talend.mitard.data.AbstractNodeType;
import ch.mno.talend.mitard.data.ConnectionType;
import ch.mno.talend.mitard.data.ProcessType;
import ch.mno.talend.mitard.readers.treaders.AbstractTReader;
import ch.mno.talend.mitard.readers.treaders.TReaderFactory;
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

        if (localName.equals("node")) {
            readNode(localName, attributes);
        } else if (localName.equals("connection")) {
            process.addConnection(new ConnectionType(getAttribute(attributes, "source"), getAttribute(attributes, "target"), getAttribute(attributes, "connectorName")));
        } else if (localName.equals("elementParameter")) {
            if ("ON_STATCATCHER_FLAG".equals(attributes.getValue("name")) && "true".equals(attributes.getValue("value"))) {
                process.setUseStatCatcher();
            }
        }

        if (reader != null) {
            reader.startElement(localName, attributes);
        }
    }

    private void readNode(String localName, Attributes attributes) {
        String componentName = getAttribute(attributes, "componentName");
        reader = TReaderFactory.build(componentName);
        if (reader==null) {
            if (componentName.startsWith("c") || componentName.startsWith("t")) {
                LOG.warn("Missing read for " + componentName);
            } else {
                // Application components
                LOG.trace("Missing read for " + componentName);
            }
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
    }


    private String getAttribute(Attributes attributes, String name) {
        for (int i = 0; i < attributes.getLength(); i++) {
            if (attributes.getLocalName(i).equals(name)) {
                return attributes.getValue(i);
            }
        }
        return null;
    }


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
