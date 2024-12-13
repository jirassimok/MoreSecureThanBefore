package main;

//import controllers.SMSController;
import entities.AccountManager;
import icons.IconController;
import javafx.application.Application;
import entities.Directory;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import database.DatabaseWrapper;
import database.DatabaseException;
import memento.TimeoutTimer;

public class ApplicationController extends Application
{

	private static Directory directory;
	private static AccountManager accountManager;
	private static IconController iconController;
	private static Stage stage;

	public static Stage getStage() {
		return ApplicationController.stage;
	}

	public static AccountManager getAccountManager() {
		return ApplicationController.accountManager;
	}

	public static Directory getDirectory() {
		return ApplicationController.directory; // returns the single copy
	}

	public static void setDirectory(Directory newDirectory) {
		ApplicationController.directory = newDirectory;
	}

	public static IconController getIconController() {
		return ApplicationController.iconController;
	}

	@Override
	public void init() throws Exception {
		if (ApplicationController.directory != null
				|| ApplicationController.accountManager != null
				|| ApplicationController.iconController != null) {
			throw new IllegalStateException("Application already initialized");
		}

		try {
			DatabaseWrapper.getInstance().init();
		} catch (DatabaseException e) {
			System.out.println("ERROR IN DATABASE INITIALIZATION:\n" + e.getMessage());
			Platform.exit();
		}

		try {
			ApplicationController.directory = DatabaseWrapper.getInstance().getDirectory();
			ApplicationController.accountManager = DatabaseWrapper.getInstance().getAccountManager();
		} catch (DatabaseException e) {
			System.out.println("ERROR LOADING DATABASE:\n" + e.getMessage());
			Platform.exit();
		}
		ApplicationController.iconController = new IconController(directory, accountManager);
	}

	@Override
	public void stop() throws Exception {
		DatabaseWrapper.getInstance().close();
		TimeoutTimer.getTimeoutTimer().cancelTimer();
	}

	/** This is called by JavaFX and starts up the application UI user panel*/
	@Override
	public void start(Stage primaryStage) throws Exception {
		if (ApplicationController.stage != null) {
			throw new IllegalStateException("Application already has a stage");
		}
		ApplicationController.stage = primaryStage;
		Parent root = (Pane) FXMLLoader.load(this.getClass().getResource("/Welcome.fxml"));
		primaryStage.setTitle("FFSM Navigator");
		primaryStage.getIcons().add(new Image("/bwhIcon.png"));
		Scene user = new Scene(root, 1300, 800);
		primaryStage.setMinWidth(1180);
		primaryStage.setMinHeight(722);
		primaryStage.setScene(user);
		primaryStage.show();

	}
}

