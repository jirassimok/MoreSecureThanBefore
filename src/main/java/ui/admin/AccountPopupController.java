package ui.admin;

import entities.Account;
import entities.Account.AccessLevel;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.Callback;

import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;

import static main.ApplicationController.getAccountManager;


public class AccountPopupController
		implements Initializable
{
	private static final int MIN_PASSWORD_LENGTH = 8;
	private static final String NEW_USER_NAME = "newuser";
	private static final int RANDOM_PASSWORD_BYTES = 100;
	private static final SecureRandom PASSWORD_RANDOM = new SecureRandom();

	@FXML private Button doneBtn;
	@FXML private TableView<Account> accountTableView;
	@FXML private TableColumn<Account, String> usernameCol;
	@FXML private TableColumn<Account, char[]> passwordCol;
	@FXML private TableColumn<Account, AccessLevel> permissionsCol;
	@FXML private Label errorField;

	Account selectedAccount;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.accountTableView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> {});

		accountTableView.getItems().setAll(getAccountManager().getAccounts().values());

		Callback<TableColumn<Account, String>, TableCell<Account, String>> cellFactory = p -> new EditingCell();

		permissionsCol.setCellValueFactory(cdf -> new SimpleObjectProperty<>(cdf.getValue().getPermissions()));

		passwordCol.setCellValueFactory(cdf -> new SimpleObjectProperty<>(new char[0]));

		usernameCol.setCellValueFactory(cdf -> new SimpleStringProperty(cdf.getValue().getUsername()));

		usernameCol.setCellFactory(cellFactory);
		passwordCol.setCellFactory(column -> new PasswordCell());
		permissionsCol.setCellFactory(column -> new ComboBoxTableCell<>(AccessLevel.values()));

		usernameCol.setOnEditCommit(t -> {
			if(getAccountManager().getAccounts().values().stream()
					.noneMatch(a -> a != t.getRowValue() && a.getUsername().equals(t.getNewValue()))) {
				hideError("Username already taken");
				t.getRowValue().setUsername(t.getNewValue());
			}else{
				displayError("Username already taken");
			}
		});

		passwordCol.setOnEditCommit(event -> {
			String errMsg = String.format("Password must be at least %s characters", MIN_PASSWORD_LENGTH);
			char[] password = event.getNewValue();
			if (password.length >= MIN_PASSWORD_LENGTH) {
				hideError(errMsg);
				try {
					event.getRowValue().changePassword(password);
				} catch (GeneralSecurityException e) {
					e.printStackTrace();
					displayError("Error processing password change; see logs for details.");
					return;
				} finally {
					Arrays.fill(password, '-');
				}
			} else {
				Arrays.fill(password, '-');
				displayError(errMsg);
			}
		});

		permissionsCol.setOnEditCommit(edit -> edit.getRowValue().setPermission(edit.getNewValue()));

		this.accountTableView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> selectedAccount = newValue);
	}

	/**
	 * Display an error about passwords. Limit the length of the message,
	 * as the display field has fixed width.
	 */
	private void displayError(String message) {
		errorField.setText(message);
		errorField.setVisible(true);
	}

	private void hideError(String message) {
		if (message.equals(errorField.getText())) {
			errorField.setVisible(false);
		}
	}

	@FXML
	public void onAddAccountBtnClicked(){
		List<Account> items = accountTableView.getItems();
		int index;
		if (getAccountManager().getAccounts().containsKey(NEW_USER_NAME)) {
			Account newAccount = getAccountManager().getAccounts().get(NEW_USER_NAME);
			index = items.indexOf(newAccount);
			displayError("Rename the last new user first.");
		} else {
			Account newAccount;
			try {
				newAccount = getAccountManager().addNewAccount(
						"newuser", AccessLevel.PROFESSIONAL, randomPassword());
				displayError("User created; please change name and password.");
			} catch (GeneralSecurityException e) {
				e.printStackTrace();
				displayError("Error creating account; see logs for details.");
				return;
			}
			items.add(newAccount);
			index = items.size() - 1;
		}
		Platform.runLater(() -> {
			accountTableView.getSelectionModel().select(index);
			accountTableView.getFocusModel().focus(index);
			accountTableView.scrollTo(index);
		});
	}

	@FXML
	public void onRemoveAccountBtnClicked(){
		getAccountManager().deleteAccount(selectedAccount.getUsername());
		accountTableView.getItems().remove(selectedAccount);
	}

	@FXML
	public void ondoneBtnClick(){
		getAccountManager().deleteAccount(NEW_USER_NAME);
		doneBtn.getScene().getWindow().hide();
	}

	private char[] randomPassword() {
		byte[] bytes = new byte[RANDOM_PASSWORD_BYTES];
		PASSWORD_RANDOM.nextBytes(bytes);

		// Java offers no clean way to directly Base64-encode a byte[] in a char[],
		// but it does specify the encoding of the bytes, so we can do it manually.
		byte[] encoded = Base64.getEncoder().encode(bytes);
		Arrays.fill(bytes, (byte) 0);
		char[] password = new char[encoded.length];
		for (int i = 0; i < password.length; ++i) {
			// We know base64 encodes into a limited subset of ISO-8859-1 (Latin-1),
			// so we can do this cast safely.
			password[i] = (char) encoded[i];
		}
		Arrays.fill(encoded, (byte) 0);
		return password;
	}
}
