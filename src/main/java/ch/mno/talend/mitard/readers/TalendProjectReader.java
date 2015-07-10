package ch.mno.talend.mitard.readers;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import ch.mno.talend.mitard.data.TalendProjectType;

/**
 * Read talend.project file
 */
public class TalendProjectReader {

	public static TalendProjectType read(InputStream talendProjectStream) throws IOException {
		String data = IOUtils.toString(talendProjectStream, "UTF-8");

		TalendProjectType project = new TalendProjectType();

		Matcher matcher = Pattern.compile("<TalendProperties:User xmi:id=\"(.*?)\" login=\"(.*?)\" firstName=\"(.*?)\" lastName=\"(.*?)\"").matcher(data);
		while (matcher.find()) {
			project.addUser(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4));
		}

		return project;
	}

}
