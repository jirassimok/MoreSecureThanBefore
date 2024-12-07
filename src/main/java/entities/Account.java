package entities;

import hashing.HashProtocol;
import hashing.PasswordHasher;

import java.security.GeneralSecurityException;

public class Account
{
	private AccountManager manager;

	private String username;
	private AccessLevel permission;
	private HashProtocol passwordProtocol;
	private String passHash;
	private String salt;

	public String getUsername() {
		return username;
	}

	public AccessLevel getPermissions() {
		return permission;
	}

	public HashProtocol getPasswordProtocol() {
		return passwordProtocol;
	}

	public String getPassHash() {
		return passHash;
	}

	public String getSalt() {
		return salt;
	}

	/* **** Setters **** */

	public void setUsername(String newName) {
		this.manager.updateKey(newName, username);
		this.username = newName;
	}

	public void setPermission(AccessLevel permission) {
		this.permission = permission;
	}

	/* **** Password Access **** */

	public void changePassword(char[] newPassword) throws GeneralSecurityException {
		String salt = PasswordHasher.generateSalt();
		this.passHash = PasswordHasher.hashPassword(newPassword, salt);
		this.passwordProtocol = PasswordHasher.DEFAULT_PROTOCOL;
		this.salt = salt;
	}

	Account(AccountManager manager, String username, AccessLevel permission,
	        HashProtocol protocol, String passHash, String salt) {
		this.manager = manager;
		this.username = username;
		this.passHash = passHash;
		this.salt = salt;
		this.passwordProtocol = protocol;
		this.permission = permission;
	}

	/**
	 * Constructor for a new account.
	 *
	 * <p>Clears the password array.
	 */
	Account(AccountManager manager, String username, AccessLevel permission, char[] password)
			throws GeneralSecurityException {
		this.manager = manager;
		this.username = username;
		this.permission = permission;
		this.changePassword(password);
	}

	public enum AccessLevel {
		ADMIN, PROFESSIONAL;
	}
}
