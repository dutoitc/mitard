package ch.mno.talend.mitard.readers;

import ch.mno.talend.mitard.data.AbstractNodeType;
import ch.mno.talend.mitard.data.ConnectionType;
import ch.mno.talend.mitard.data.ProcessType;
import ch.mno.talend.mitard.data.TESBConsumerType;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

/**
 * Created by dutoitc on 04.12.2015.
 */
public class ProcessReaderTest {

    @Test
    public void test1() throws IOException, SAXException, ParserConfigurationException {
        ProcessType process = ProcessReader.read(getClass().getResourceAsStream("/ESBTUTORIALPROJECT/process/AirportConsumer_0.2.item"));

        List<ConnectionType> connections = process.getConnections("tXMLMap_1");
        Assert.assertEquals(1, connections.size());
        Assert.assertEquals("tESBConsumer_1", connections.get(0).getTarget());

        Assert.assertEquals(3, process.getConnections().size());

        Assert.assertEquals(5, process.getNodeList().size());
        AbstractNodeType node = process.getNodeList().get(2);
        Assert.assertTrue(node instanceof TESBConsumerType);

        TESBConsumerType ct = (TESBConsumerType) node;
        Assert.assertEquals("airport", ct.getServiceName());
        Assert.assertEquals("http://localhost:8200/esb/AirportService", ct.getEndpointURI());
        Assert.assertEquals("tESBConsumer_1", ct.getUniqueName());
        Assert.assertEquals("tESBConsumer", ct.getComponentName());
        Assert.assertTrue(ct.isActive());
    }

}
