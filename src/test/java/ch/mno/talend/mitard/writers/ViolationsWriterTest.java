package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.Context;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

/**
 * Created by xsicdt on 03/04/18.
 */
public class ViolationsWriterTest {

    @Test
    public void testX() {
        Context context = Mockito.mock(Context.class);
        Mockito.when(context.getBLACKLIST()).thenReturn(Arrays.asList(".*TEMPLATE.*,.*test_.*,.*Copy_of.*,.*MOCK.*,.*Old,.*Copy,.*_old,H_Tests.*,K_Int.*,Y_NoMoreUsed.*,Z_.*,.*LLA_.*,WriteJournalEven*,GestionErreu*,.*tmp.*,Copy.*,Backup.*,Tmp.*,LLA.*,.*test.*,.*DEADLOCK.*".split(",")));
        Assert.assertTrue(new ViolationsWriter(context).isBlacklisted("Y_NoMoreUsed/E_Traitement/H_REE REEControlerExistenceDependences"));

    }

}
