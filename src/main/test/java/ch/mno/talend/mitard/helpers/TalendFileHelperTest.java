package ch.mno.talend.mitard.helpers;

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
        TalendFiles versions = new TalendFileHelper().findLatestVersions(new File(getClass().getResource("/ESBTUTORIALPROJECT/process").getFile()).getAbsolutePath());
        Assert.assertEquals(6, versions.getProcesses().size());
    }
}
