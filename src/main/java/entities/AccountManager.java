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

	void updateKey(String newName, String oldName) {
		Account tempAccount = accounts.get(oldName);
		accounts.remove(oldName);
		accounts.put(newName, tempAccount);
	}

	Map<String, Account> getAccounts() {
		return accounts;
	}

	AccessLevel getPermissions(String username) {
		return accounts.get(username).getPermissions();
	}

	Account getAccount(String username) {
		return accounts.get(username);
	}

	Account addAccount(String user, String password, AccessLevel permission) {
		Account newAccount = new Account(user, password, permission);
		accounts.put(user, newAccount);
		return newAccount;
	}

	void deleteAccount(String user) {
		accounts.remove(user);
	}

	boolean isProfessional() {
		return loggedIn;
	}

	void logIn() {
		this.loggedIn = true;
	}

	void logOut() {
		this.loggedIn = false;
	}

	boolean isLoggedIn() {
		return this.loggedIn;
	}
}
