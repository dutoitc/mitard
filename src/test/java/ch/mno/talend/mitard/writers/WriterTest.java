package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.Context;
import ch.mno.talend.mitard.data.TalendFiles;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;

/**
 * Created by dutoitc on 06.03.2019.
 */
public class WriterTest {

    @Test
    public void testX() {
        String contextString = "blacklist=.*TEMPLATE.*,.*test_.*,.*Copy_of.*,.*MOCK.*,.*Old";
        Context context = new Context(new ByteArrayInputStream(contextString.getBytes()));
        AbstractWriter dw = new AbstractWriter(context) {
            @Override
            public void write(TalendFiles talendFiles) {

            }
        };

        Assert.assertTrue(dw.isBlacklisted("Copy_of_BuildOrganisationLocationType_v3"));
        Assert.assertTrue(dw.isBlacklisted("My_Copy_of_BuildOrganisationLocationType_v3"));
        Assert.assertTrue(dw.isBlacklisted("Servicemock"));
        Assert.assertTrue(dw.isBlacklisted("jobOld"));
        Assert.assertFalse(dw.isBlacklisted("oldJob"));

    }

}
