package ch.mno.talend.mitard.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xsicdt on 07/08/15.
 */
public class WorkflowType {

	public List<String> services = new ArrayList<>();

	public void addService(String service) {
		services.add(service);
	}

	public List<String> getServices() {
		return services;
	}

}
