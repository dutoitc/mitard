package ch.mno.talend.mitard;

import ch.mno.talend.mitard.data.ProcessType;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;

public class XSDValidator {

    @Test
    public void testProcess1() throws URISyntaxException, JAXBException, IOException {
        System.out.println(new File(".").getAbsolutePath());
        validateXMLSchema("/ESBTUTORIALPROJECT/process/RESTService_0.3.item");
        validateUnmarshaller("/ESBTUTORIALPROJECT/process/RESTService_0.3.item");
    }


    public static void validateXMLSchema(String xmlPath) throws URISyntaxException, JAXBException, IOException {
        String xml = IOUtils.toString(XSDValidator.class.getResourceAsStream(xmlPath));
        //xml = xml.replace("<talendfile.*?>", "<ProcessType>");

        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            URL xsdResource = XSDValidator.class.getResource("/talend-process.xsd");
            Assert.assertNotNull(xsdResource);
            Schema schema = factory.newSchema(new File(xsdResource.toURI()));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xml)));
            //new StreamSource(xml));
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Exception: " + e.getMessage());
        } catch (SAXException e1) {
            Assert.fail("SAX Exception: " + e1.getMessage());
        }
        Assert.assertTrue(true);
    }

    public static void validateUnmarshaller(String xmlPath) throws JAXBException, IOException {
        String s = IOUtils.toString(XSDValidator.class.getResourceAsStream(xmlPath));
        s=s.replace("talendfile:", "");

        JAXBContext jc = JAXBContext.newInstance(ProcessType.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        ProcessType process = (ProcessType) unmarshaller.unmarshal(new StringReader(s));
        Assert.assertNotNull(process);
    }
}