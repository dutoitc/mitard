package ch.mno.talend.mitard.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xsicdt on 29/06/15.
 */
public class TalendProjectType {

	private List<TalendUserType> users = new ArrayList<>();

	public TalendUserType getUserById(String id) {
		for (TalendUserType user: users) {
			if (user.getId().equals(id)) return user;
		}
		return null;
	}

	public List<TalendUserType> getUsers() {
		return users;
	}

	public void setUsers(List<TalendUserType> users) {
		this.users = users;
	}

	public void addUser(String id, String login, String firstName, String lastName) {
		users.add(new TalendUserType(id, login, firstName, lastName));
	}
}
