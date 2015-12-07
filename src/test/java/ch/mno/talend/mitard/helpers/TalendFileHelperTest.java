package ch.mno.talend.mitard.helpers;

import ch.mno.talend.mitard.data.TalendFile;
import ch.mno.talend.mitard.data.TalendFiles;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Created by dutoitc on 05.05.2015.
 */
public class TalendFileHelperTest {

    @Test
    public void testX() {
        TalendFiles versions = new TalendFileHelper().findLatestVersions(new File(getClass().getResource("/ESBTUTORIALPROJECT").getFile()).getAbsolutePath());
        Assert.assertEquals(6, versions.getProcesses().size());

        TalendFile process = null;
        for (TalendFile file: versions.getProcesses()) {
            if (file.getName().equals("RESTService")) {
                process = file;
                break;
            }
        }
        Assert.assertNotNull(process);
        Assert.assertTrue("Wrong name: " + process.getItemFilename(), process.getItemFilename().endsWith("RESTService_0.3.item"));
        Assert.assertEquals("RESTService", process.getName());
        Assert.assertEquals("0.3", process.getVersion());

    }
}
