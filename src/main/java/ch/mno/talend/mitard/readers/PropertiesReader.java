package ch.mno.talend.mitard.readers;

import ch.mno.talend.mitard.data.PropertiesType;
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
public class PropertiesReader extends DefaultHandler {

    private PropertiesType properties;
    private String path = "";

    public void startDocument() throws SAXException {
        properties = new PropertiesType();
    }

    public void endDocument() throws SAXException {

    }

    public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
            throws SAXException {
        int p = localName.indexOf(":");
        path = path + "/" + localName.substring(p + 1);
//        System.out.println("Start element " + path);

        if (localName.equals("Property")) {
            properties.setPurpose(getAttribute(attributes, "purpose"));
            properties.setDescription(getAttribute(attributes, "description"));
            properties.setVersion(getAttribute(attributes, "version"));
        }

    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        // Keep lastPath, current full path
        int p = path.lastIndexOf("/");
        String lastPath = path.substring(p + 1);
        path = path.substring(0, p);
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


    // ========================================================================================


    public static PropertiesType reader(InputStream xml) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser saxParser = spf.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        PropertiesReader reader = new PropertiesReader();
        xmlReader.setContentHandler(reader);
        //InputSource is = new InputSource(xml);

        InputSource is = new InputSource();
        is.setCharacterStream(new InputStreamReader(xml, "UTF-8"));
        xmlReader.parse(is);
        return reader.properties;
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
//        ProcessType process = read(new FileInputStream("C:\\projets\\talend-mitard\\mitard\\src\\main\\test\\resources\\ESBTUTORIALPROJECT\\process\\RESTService_0.3.item"));
        PropertiesType properties = reader(new FileInputStream("/home/xsicdt/isoft/Talend-5.6.1/studio/workspaceDE/a/RCENT/process/F_Publication/C_Annonces/DiffusionAnnonceSubjob_0.1.properties"));
        System.out.println(properties.getDescription());
    }

}
