package entities;

import main.ApplicationController;

public class Account
{
	public String username;
	public String password;
	public AccessLevel permission;

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
		ApplicationController.getAccountManager().updateKey(newName, username);
		this.username = newName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPermission(AccessLevel permission) {
		this.permission = permission;
	}

	Account(String username, String password, AccessLevel permission){
		this.username = username;
		this.password = password;
		this.permission = permission;
	}

	public enum AccessLevel {
		ADMIN, PROFESSIONAL;
	}
}
