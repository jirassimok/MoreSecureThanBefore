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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApplicationController extends Application
{

	private static Directory directory;
	private static AccountManager accountManager;
	private static IconController iconController;
	private static Stage stage;
	private static Set<Runnable> closeCallbacks;

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

	public static boolean registerCloseCallback(Runnable callback) {
		return closeCallbacks.add(callback);
	}

	public static boolean deregisterCloseCallback(Runnable callback) {
		return closeCallbacks.remove(callback);
	}

	@Override
	public void init() throws Exception {
		closeCallbacks = new HashSet<>();

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
		List<Exception> exceptions = new ArrayList<>();
		for (Runnable callback : closeCallbacks) {
			try {
				callback.run();
			} catch (RuntimeException e) {
				exceptions.add(e);
			}
		}
		if (exceptions.size() == 1) {
			throw exceptions.get(0);
		} else if (!exceptions.isEmpty()) {
			Exception e = new RuntimeException("Multiple exceptions occurred while stopping");
			for (Exception x : exceptions) {
				e.addSuppressed(x);
			}
			throw e;
		}
	}

	/** This is called by JavaFX and starts up the application UI user panel*/
	@Override
	public void start(Stage primaryStage) throws Exception {
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

