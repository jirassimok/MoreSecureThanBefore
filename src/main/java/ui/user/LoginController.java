package ui.user;


import entities.AccountManager;
import entities.Directory;
import javafx.application.Platform;
import javafx.concurrent.Task;
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

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class LoginController implements Initializable{
	// TODO: Make this configurable
	private static final long LOGIN_RETRY_DELAY = TimeUnit.MILLISECONDS.toNanos(3000);

	@FXML private Label errorLbl;
	@FXML private Button cancelBtn;
	@FXML private TextField usernameField;
	@FXML private PasswordField passwordField;
	@FXML private Button loginBtn;
	@FXML private BorderPane parentBorderPane;

	private Directory directory = ApplicationController.getDirectory();
	private AccountManager accountManager = ApplicationController.getAccountManager();
	private TimeoutTimer timer = TimeoutTimer.getTimeoutTimer();

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
	}

	private void lockControls() {
		cancelBtn.setDisable(true);
		loginBtn.setDisable(true);
		usernameField.setEditable(false);
		passwordField.setEditable(false);
	}

	private void restartForm() {
		passwordField.clear();
		cancelBtn.setDisable(false);
		loginBtn.setDisable(false);
		usernameField.setEditable(true);
		passwordField.setEditable(true);
		usernameField.selectAll();
	}

	/**
	 * Task representing login computation.
	 *
	 * <p>Internally handles success and failure.
	 */
	private class LoginTask extends Task<Optional<Parent>> {
		/**
		 * Returns the new root FXML element on success, or an empty Optional on failure.
		 */
		@Override
		protected Optional<Parent> call() throws IOException, InterruptedException {
			long startTime = System.nanoTime();
			switch (accountManager.tryLogin(usernameField.getText(), getPasswordAsArray())) {
				case ADMIN:
					return Optional.of(loadFXML("/AdminUI.fxml"));
				case PROFESSIONAL:
					return Optional.of(loadFXML("/UserDestination.fxml"));
				default:
					// Wait before allowing another login attempt
					long nanos = System.nanoTime() - startTime;
					try {
						TimeUnit.NANOSECONDS.sleep(LOGIN_RETRY_DELAY - nanos);
					} catch (InterruptedException e) {
						if (this.isCancelled()) {
							restartForm();
						} else {
							throw e;
						}
					}
					return Optional.empty();
			}
		}

		@Override
		protected void succeeded() {
			super.succeeded();
			Optional<Parent> newRoot = getValue();
			if (newRoot.isPresent()) {
				changeScene(newRoot.get());
			} else {
				errorLbl.setText("Incorrect Username or Password");
				restartForm();
				usernameField.requestFocus();
			}
		}

		@Override
		protected void failed() {
			super.failed();
			errorLbl.setText("Error logging in; see logs.");
			restartForm();
		}

		/** Load the page for the logged-in user. */
		private Parent loadFXML(String resourcePath) throws IOException {
			try {
				return FXMLLoader.load(this.getClass().getResource(resourcePath));
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

	@FXML
	public void loginBtnClicked() {
		this.lockControls();
		Task<?> loginTask = new LoginTask();
		Thread thread = new Thread(loginTask);
		thread.setDaemon(true);
		thread.start();
	}

	/**
	 * Get the password from the PasswordField as a char[], trying not to make
	 * it into a string.
	 */
	private char[] getPasswordAsArray() {
		CharSequence content = passwordField.getCharacters();
		char[] password = new char[content.length()];

		// OpenJFX stores TextField/PasswordField data in a StringBuilder, and OpenJDK's
		// StringBuilder stores its contents in a char[]. So we can use that to access the
		// char array almost directly.
		if (content instanceof StringBuilder) {
			StringBuilder buffer = (StringBuilder) content;

			// Copy the StringBuilder's internal data into our own array.
			buffer.getChars(0, buffer.length(), password, 0);

			// Blank the StringBuilder's internal buffer by overwriting it with .replace
			// and a string of null bytes.
			buffer.replace(0, buffer.length(), new String(new char[buffer.length()]));
			// If we use buffer.delete here, the TextField will get confused and crash
			// when we clear it below.
		} else {
			// If we didn't get a StringBuilder, we're not using the OpenJFX PasswordField,
			// and we don't know what's inside 'content', but we can copy the data byte by
			// byte from it and hope that it's using an array internally and that it's
			// cleared along with the password field.
			//
			// To do better than this, we'd probably need to reimplement TextField from
			// scratch ourselves.
			for (int i = 0; i < password.length; ++i) {
				password[i] = content.charAt(i);
			}
		}
		passwordField.clear();
		return password;
	}

	@FXML
	public void enterPressed(KeyEvent e){
		if ( e.getCode() == KeyCode.ENTER){
			this.loginBtnClicked();
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

	public void changeScene(Parent newRoot) {
		parentBorderPane.getScene().setRoot(newRoot);
	}

	// place inside controller
	public void resetState() {
		accountManager.logOut();
		parentBorderPane.getScene().setRoot(directory.getCaretaker().getState().getRoot());
	}
}
