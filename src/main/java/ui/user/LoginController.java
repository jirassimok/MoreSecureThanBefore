package ui.user;


import entities.AccountManager;
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
	// This is a field for reference equality purposes
	private Runnable timerCancelCallback = retryTimer::cancel;

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
		ApplicationController.registerCloseCallback(timerCancelCallback);
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

	/**
	 * Start a timer that waits before unlocking the controls.
	 */
	private void startRetryTimer() {
		this.retryTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				passwordField.clear();
				unlockControls();
				usernameField.selectAll();
			}
		}, LOGIN_RETRY_DELAY);
	}

	@FXML
	public void loginBtnClicked() throws IOException {
		this.lockControls();
		switch (accountManager.tryLogin(this.usernameField.getText(), this.passwordField.getText())) {
			case ADMIN:
				// directory.logIn(); // Admins start viewing the user screen
				changeScene(FXMLLoader.load(this.getClass().getResource("/AdminUI.fxml")));
				break;
			case PROFESSIONAL:
				changeScene(FXMLLoader.load(this.getClass().getResource("/UserDestination.fxml")));
				break;
			default:
				this.errorLbl.setText("Incorrect Username or Password");
				this.usernameField.requestFocus();
				this.startRetryTimer();
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

	/**
	 * Close the retry timer. After this is called, the timer will no longer work;
	 * use it only when leaving the page or closing the application.
	 */
	private void closeRetryTimer() {
		timerCancelCallback.run();
		ApplicationController.deregisterCloseCallback(timerCancelCallback);
	}

	public void changeScene(Parent newRoot) {
		closeRetryTimer();
		parentBorderPane.getScene().setRoot(newRoot);
	}

	// place inside controller
	public void resetState() {
		accountManager.logOut();
		closeRetryTimer();
		parentBorderPane.getScene().setRoot(directory.getCaretaker().getState().getRoot());
	}
}
