package entities;

import hashing.HashProtocol;
import entities.Account.AccessLevel;
import hashing.PasswordHasher;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import static hashing.PasswordHasher.hashPassword;

/**
 * In-memory account storage, comparable to {@link Directory}.
 */
public class AccountManager {
	private Map<String, Account> accounts = new HashMap<>();
	private boolean loggedIn;

	private static final String DUMMY_SALT = PasswordHasher.generateSalt();


 	/* **************** Account Checking Methods **************** */

	public boolean isProfessional() {
		return loggedIn;
	}

	// Maybe this should be a static method on ApplicationController,
	// if I'm not separating Session into its own class.
	public boolean canViewRestricted() {
		return loggedIn;
	}


	/* **************** Access Modification Methods **************** */

	public void logOut() {
		this.loggedIn = false;
	}


	/* **************** Account Editing Methods **************** */

	public void updateKey(String newName, String oldName) {
		Account tempAccount = accounts.get(oldName);
		accounts.remove(oldName);
		accounts.put(newName, tempAccount);
	}

	public Map<String, Account> getAccounts() {
		return accounts;
	}

	public Account loadAccount(String user, AccessLevel permission,
	                           HashProtocol protocol, String passHash, String salt) {
		Account newAccount = new Account(this, user, permission, protocol, passHash, salt);
		accounts.put(user, newAccount);
		return newAccount;
	}

	public Account addNewAccount(String user, AccessLevel permission, char[] password)
			throws GeneralSecurityException {
		Account newAccount = new Account(this, user, permission, password);
		accounts.put(user, newAccount);
		return newAccount;
	}

	public void deleteAccount(String user) {
		accounts.remove(user);
	}

	/**
	 * Try to log in. If a given username and password form a valid log-in ID,
	 * set the logged-in state appropriately and return the login type.
	 *
	 * @param username        The username to test
	 * @param password        The password to test
	 * @return The login result.
	 */
	public LoginStatus tryLogin(String username, char[] password) {
		Account account = accounts.get(username);
		if (account == null) {
			// Hash the password anyway so the user can't see why we failed
			// (improves timing similarity and clears the password array).
			try {
				hashPassword(password, DUMMY_SALT);
			} catch (GeneralSecurityException e) {}
			return LoginStatus.FAILURE;
		}

		String passHash;
		try {
			passHash = hashPassword(account.getPasswordProtocol(), password, account.getSalt());
		} catch (GeneralSecurityException e) {
			return LoginStatus.FAILURE;
		}

		// Safe because the empty string is not a valid password
		if (account.getPassHash().equals(passHash)) {
			switch (account.getPermissions()) {
				case ADMIN:
					return LoginStatus.ADMIN;
				case PROFESSIONAL:
					this.loggedIn = true;
					return LoginStatus.PROFESSIONAL;
				default:
					return LoginStatus.FAILURE;
			}
		} else {
			return LoginStatus.FAILURE;
		}
	}

	public enum LoginStatus {
		ADMIN, PROFESSIONAL, FAILURE;
	}
}
