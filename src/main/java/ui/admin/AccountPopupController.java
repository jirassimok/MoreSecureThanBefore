package ui.admin;

import entities.Account;
import entities.Account.AccessLevel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

import static main.ApplicationController.getAccountManager;


public class AccountPopupController
		implements Initializable
{
	@FXML private Button doneBtn;
	@FXML private TableView<Account> accountTableView;
	@FXML private TableColumn<Account, String> usernameCol;
	@FXML private TableColumn<Account, String> passwordCol;
	@FXML private TableColumn<Account, AccessLevel> permissionsCol;
	@FXML private Label existsError;

	Account selectedAccount;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.accountTableView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> {});

		accountTableView.getItems().setAll(getAccountManager().getAccounts().values());

		Callback<TableColumn<Account, String>, TableCell<Account, String>> cellFactory = p -> new EditingCell();

		permissionsCol.setCellValueFactory(cdf -> new SimpleObjectProperty<>(cdf.getValue().getPermissions()));

		passwordCol.setCellValueFactory(cdf -> new SimpleStringProperty("*****"));

		usernameCol.setCellValueFactory(cdf -> new SimpleStringProperty(cdf.getValue().getUsername()));

		usernameCol.setCellFactory(cellFactory);
		passwordCol.setCellFactory(cellFactory);
		permissionsCol.setCellFactory(column -> new ComboBoxTableCell<>(AccessLevel.values()));

		usernameCol.setOnEditCommit(t -> {
			if(getAccountManager().getAccounts().values().stream()
					.noneMatch(a -> a != t.getRowValue() && a.getUsername().equals(t.getNewValue()))) {
				existsError.setVisible(false);
				t.getRowValue().setUsername(t.getNewValue());
			}else{
				existsError.setVisible(true);
			}
		});

		passwordCol.setOnEditCommit(t -> t.getRowValue().setPassword(t.getNewValue()));

		permissionsCol.setOnEditCommit(edit -> edit.getRowValue().setPermission(edit.getNewValue()));

		this.accountTableView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> selectedAccount = newValue);
	}

	@FXML
	public void onAddAccountBtnClicked(){
		Account newAccount = getAccountManager().addAccount("newuser","newpassword", AccessLevel.PROFESSIONAL);
		accountTableView.getItems().add(newAccount);
	}

	@FXML
	public void onRemoveAccountBtnClicked(){
		getAccountManager().deleteAccount(selectedAccount.getUsername());
		accountTableView.getItems().remove(selectedAccount);
	}

	@FXML
	public void ondoneBtnClick(){
		getAccountManager().deleteAccount("newuser");
		doneBtn.getScene().getWindow().hide();
	}

}
