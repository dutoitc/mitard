package ch.mno.talend.mitard.readers;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;

import ch.mno.talend.mitard.data.TalendProjectType;
import ch.mno.talend.mitard.data.WorkflowType;

/**
 * Read workflow proc file
 */
public class WorkflowReader {

	public static WorkflowType read(InputStream stream) throws IOException {
		String data = IOUtils.toString(stream, "UTF-8");

		WorkflowType workflow = new WorkflowType();

		Matcher matcher = Pattern.compile("key=.setEndpointAddress. value=\"(.*?)\"").matcher(data);
		while (matcher.find()) {
			String endpoint = matcher.group(1);
			try {
				endpoint = new String(Hex.decodeHex(endpoint.toCharArray()));
				if (endpoint.endsWith("\"}")) endpoint = endpoint.substring(0, endpoint.length()-2);
				endpoint = endpoint.substring(endpoint.lastIndexOf('/')+1); // Last part of URL = service name
			}
			catch (DecoderException e) {
				e.printStackTrace();
			}
			workflow.addService(endpoint);
		}

		return workflow;
	}



}
