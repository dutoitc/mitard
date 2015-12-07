package ch.mno.talend.mitard.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Talend project informations.
 */
public class TalendProjectType {

	private Map<String, TalendUserType> users = new HashMap<>();

	public TalendUserType getUserById(String id) {
		return users.get(id);
	}

	public void addUser(String id, String login, String firstName, String lastName) {
		users.put(id, new TalendUserType(id, login, firstName, lastName));
	}
}
