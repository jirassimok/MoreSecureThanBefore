package ui.user;


import entities.AccountManager;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.stage.Window;
import entities.Account;
import entities.Directory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import javafx.scene.input.KeyEvent;
import javafx.stage.WindowEvent;
import main.ApplicationController;

import memento.TimeoutTimer;

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class LoginController implements Initializable{
	// TODO: Make this configurable
	private static final int LOGIN_RETRY_DELAY = 2000;

	@FXML private Label errorLbl;
	@FXML private Button cancelBtn;
	@FXML private TextField usernameField;
	@FXML private PasswordField passwordField;
	@FXML private Button loginBtn;
	@FXML private BorderPane parentBorderPane;

	private Directory directory = ApplicationController.getDirectory();
	private AccountManager accountManager = ApplicationController.getAccountManager();
	private TimeoutTimer timer = TimeoutTimer.getTimeoutTimer();
	private Timer retryTimer = new Timer();

	// These are fields for reference equality purposes
	private final EventHandler<WindowEvent> windowHideHandler = event -> {
		retryTimer.cancel();
	};

	private final ChangeListener<Window> windowChangeListener = (observable, oldWindow, newWindow) -> {
		if (newWindow == null) {
			retryTimer.cancel();
			oldWindow.removeEventHandler(WindowEvent.WINDOW_HIDDEN, windowHideHandler);
		} else {
			newWindow.addEventHandler(WindowEvent.WINDOW_HIDDEN, windowHideHandler);
		}
	};

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		parentBorderPane.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.ESCAPE){
				try {
					cancelBtnClicked();
				} catch (IOException ex) {
					System.err.println("IOException while cancelling login attempt");
					throw new RuntimeException(ex);
				}
			}
		});

		this.cancelBtn.setFocusTraversable(false);
		this.loginBtn.setFocusTraversable(false);
		Platform.runLater( () -> usernameField.requestFocus());

		timer.emptyTasks();
		this.initGlobalFilter();
		TimeoutTimer.getTimeoutTimer().registerTask(this::resetState);

		// Throw out the timer when the scene changes or the window closes
		parentBorderPane.sceneProperty().addListener((observeScene, oldScene, newScene) -> {
			if (newScene == null) {
				oldScene.windowProperty().removeListener(windowChangeListener);
				retryTimer.cancel();
			} else {
				newScene.windowProperty().addListener(windowChangeListener);
			}
		});
	}

	private void lockControls() {
		cancelBtn.setDisable(true);
		loginBtn.setDisable(true);
		usernameField.setEditable(false);
		passwordField.setEditable(false);
	}

	private void unlockControls() {
		cancelBtn.setDisable(false);
		loginBtn.setDisable(false);
		usernameField.setEditable(true);
		passwordField.setEditable(true);
	}

	@FXML
	public void loginBtnClicked() throws IOException {
		this.lockControls();
		LoginStatus status = checkLogin(this.usernameField.getText(), this.passwordField.getText());
		switch (status) {
			case ADMIN:
				// directory.logIn(); // Admins start viewing the user screen
				Parent adminUI = FXMLLoader.load(this.getClass().getResource("/AdminUI.fxml"));
				errorLbl.getScene().setRoot(adminUI);
				break;
			case PROFESSIONAL:
				accountManager.logIn();
				Parent destUI = FXMLLoader.load(this.getClass().getResource("/UserDestination.fxml"));
				errorLbl.getScene().setRoot(destUI);
				break;
			default:
				this.errorLbl.setText("Incorrect Username or Password");
				this.usernameField.requestFocus();
				// Wait a second before letting them try again
				this.retryTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						passwordField.clear();
						unlockControls();
						usernameField.selectAll();
					}
				}, LOGIN_RETRY_DELAY);
		}
	}

	@FXML
	public void enterPressed(KeyEvent e){
		if ( e.getCode() == KeyCode.ENTER){
			try {
				this.loginBtnClicked();
			} catch (IOException ex) {
				System.err.println("IOException during login attempt");
				throw new RuntimeException(ex);
			}
		}
	}

	@FXML
	public void enterPressed1(KeyEvent e){
		if ( e.getCode() == KeyCode.ENTER){
			this.passwordField.requestFocus();
		}
	}

	@FXML
	public void cancelBtnClicked() throws IOException {
		Parent destUI = (BorderPane) FXMLLoader.load(this.getClass().getResource("/UserDestination.fxml"));
		errorLbl.getScene().setRoot(destUI);
	}



	/**
	 * Check if a given username and password form a valid log-in ID
	 *
	 * @param username The username to test
	 * @param password The password to test
	 * @return 2 for admins, 1 for professionals, or 0 for failed logins
	 */
	public LoginStatus checkLogin(String username, String password) {
		Account thisAccount = accountManager.getAccount(username);
		if(thisAccount == null){
			return LoginStatus.FAILURE;
		}

		// Safe because the empty string is not a valid password
		if (thisAccount.getPassword().equals(password)) {
			switch (thisAccount.getPermissions()) {
				case ADMIN:
					return LoginStatus.ADMIN;
				case PROFESSIONAL:
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

	/**
	 * Initializes the global filter that will reset the timer whenever an action is performed.
	 */
	protected void initGlobalFilter() {
		this.parentBorderPane.addEventFilter(MouseEvent.ANY, e-> {
			timer.resetTimer();
		});
		this.parentBorderPane.addEventFilter(KeyEvent.ANY, e-> {
			timer.resetTimer();
		});
	}

	protected TimerTask getTimerTask() {
		return new TimerTask()
		{
			public void run() {
				resetState();
			}
		};
	}

	// place inside controller
	public void resetState() {
		parentBorderPane.getScene().setRoot(directory.getCaretaker().getState().getRoot());
		accountManager.logOut();
	}
}
