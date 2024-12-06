package entities;

public class Account
{
	private String username;
	private String password;
	private AccessLevel permission;
	private AccountManager manager;

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public AccessLevel getPermissions() {
		return permission;
	}

	public void setUsername(String newName) {
		this.manager.updateKey(newName, username);
		this.username = newName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPermission(AccessLevel permission) {
		this.permission = permission;
	}

	Account(String username, String password, AccessLevel permission, AccountManager manager){
		this.username = username;
		this.password = password;
		this.permission = permission;
		this.manager = manager;
	}

	public enum AccessLevel {
		ADMIN, PROFESSIONAL;
	}
}
