package ch.mno.talend.mitard.data;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by dutoitc on 05.05.2015.
 */
public class TalendFileTest {

    @Test
    public void testVersion1() {
        Assert.assertTrue(new TalendFile("path", "name", "0.1").isVersionLowerThan(new TalendFile("path", "name", "0.2")));
    }

    @Test
    public void testVersion2() {
        Assert.assertFalse(new TalendFile("path", "name", "0.2").isVersionLowerThan(new TalendFile("path", "name", "0.1")));
    }


    @Test
    public void testVersion3() {
        Assert.assertFalse(new TalendFile("path", "name", "0.11").isVersionLowerThan(new TalendFile("path", "name", "0.2")));

    }


}
