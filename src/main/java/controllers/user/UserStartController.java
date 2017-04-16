package controllers.user;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ResourceBundle;


public class UserStartController extends UserMasterController implements Initializable
{


	@FXML
	private Button changeStartBtn;


	final double SCALE_DELTA = 1.1;
	private double clickedX, clickedY;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initialize();

		//Listener for search bar
		searchBar.textProperty().addListener((ignored, old, newer) -> filterRoomList(old, newer));

	}

	@FXML
	public void backBtnClicked() throws IOException, InvocationTargetException {
		choosingStart = false;
		choosingEnd = true;
		Parent userDest = (BorderPane) FXMLLoader.load(this.getClass().getResource("/UserDestination.fxml"));
		this.getScene().setRoot(userDest);

	}

}
