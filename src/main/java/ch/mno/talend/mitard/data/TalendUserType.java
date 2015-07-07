package ch.mno.talend.mitard.data;

/**
 * Talend user, as read in talend.properties
 */
public class TalendUserType {

	private String id, login, firstName, lastName;

	public TalendUserType(String id, String login, String firstName, String lastName) {
		this.id = id;
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
