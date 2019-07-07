package ch.mno.talend.mitard.readers;

import ch.mno.talend.mitard.data.WorkflowType;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Read workflow proc file
 */
public class WorkflowReader {

	public static WorkflowType read(InputStream stream) throws IOException {
		String data = IOUtils.toString(stream, "UTF-8");

		WorkflowType workflow = new WorkflowType();

		Set<String> services = new HashSet<>();
		Matcher matcher = Pattern.compile("key=.setEndpointAddress. value=\"(.*?)\"").matcher(data);
		while (matcher.find()) {
			String endpoint = matcher.group(1);
			try {
				endpoint = new String(Hex.decodeHex(endpoint.toCharArray()));
				if (endpoint.endsWith("\"}")) endpoint = endpoint.substring(0, endpoint.length()-2);
				endpoint = endpoint.substring(endpoint.lastIndexOf('/')+1); // Last part of URL = service name
				services.add(endpoint);
			}
			catch (DecoderException e) {
				e.printStackTrace();
			}
		}

		//matcher = Pattern.compile("<parameters xmi:type=\"connectorconfiguration:ConnectorParameter\".*?service\\/(.*?Operation)", Pattern.DOTALL + Pattern.MULTILINE).matcher(data);
		matcher = Pattern.compile("service\\/([a-zA-Z0-9_\\-]+?Operation)", Pattern.DOTALL + Pattern.MULTILINE).matcher(data);
		while (matcher.find()) {
			String endpoint = matcher.group(1);
			services.add(endpoint);
		}
		services.forEach(service -> workflow.addService(service));

		return workflow;
	}

}