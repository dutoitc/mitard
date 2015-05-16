package ch.mno.talend.mitard;


import ch.mno.talend.mitard.data.Context;
import ch.mno.talend.mitard.data.TalendFiles;
import ch.mno.talend.mitard.helpers.TalendFileHelper;
import ch.mno.talend.mitard.writers.*;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, JAXBException, ParserConfigurationException, SAXException {
        Context context = new Context();

        TalendFiles talendFiles = TalendFileHelper.findLatestVersions(context.getWorkspacePath());

        // Init production path
        File productionDir = new File(context.getProductionPath());
        productionDir.delete();
        productionDir.mkdir();

        // Dependencies
        new DependenciesWriter(context).write(talendFiles);

        // JSON
        new ProcessesWriter(context).write(talendFiles);
        new RoutesWriter(context).write(talendFiles);
        new ServicesWriter(context).write(talendFiles);
        new StatisticsWriter(context).write(talendFiles);
        new ViolationsWriter(context).write(talendFiles);
    }



}
