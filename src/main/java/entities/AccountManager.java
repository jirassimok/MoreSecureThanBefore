package entities;

import entities.Account.AccessLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory account storage, comparable to {@link Directory}.
 */
public class AccountManager {
	private Map<String, Account> accounts = new HashMap<>();
	private boolean loggedIn;

	public void updateKey(String newName, String oldName) {
		Account tempAccount = accounts.get(oldName);
		accounts.remove(oldName);
		accounts.put(newName, tempAccount);
	}

	public Map<String, Account> getAccounts() {
		return accounts;
	}

	public AccessLevel getPermissions(String username) {
		return accounts.get(username).getPermissions();
	}

	public Account getAccount(String username) {
		return accounts.get(username);
	}

	public Account addAccount(String user, String password, AccessLevel permission) {
		Account newAccount = new Account(user, password, permission);
		accounts.put(user, newAccount);
		return newAccount;
	}

	public void deleteAccount(String user) {
		accounts.remove(user);
	}

	public boolean isProfessional() {
		return loggedIn;
	}

	@Deprecated
	public void logIn() {
		this.loggedIn = true;
	}

	public void logOut() {
		this.loggedIn = false;
	}

	@Deprecated
	public boolean isLoggedIn() {
		return this.loggedIn;
	}
}
