package userpanel;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.IOException;

import static java.awt.Color.black;

public class Controller
{
	@FXML
	private Button logAsAdmin;
	@FXML
	private ImageView imageViewMap = new ImageView();
	@FXML
	public VBox contentVBox;
	@FXML
	public HBox contentHBox;
	@FXML
	public AnchorPane contentAnchor;
	@FXML
	private Slider floorSlider;



	@FXML
	private void logAsAdminClicked() throws IOException{
// TODO BROKEN
//		Parent login_prompt;
//		login_prompt = FXMLLoader.load(getClass().getResource("LoginPrompt.fxml"));
//		Scene login_prompt_scene = new Scene(login_prompt);
//		app_stage.setScene(login_prompt_scene);
//		app_stage.show();

	}

	//this would be called with canLogin
//	private void loginSuccessful() throws Exception{
//
//		Parent admin_UI_parent = FXMLLoader.load(getClass().getResource("AdminUI.fxml"));
//		Scene admin_UI_scene = new Scene(admin_UI_parent);
//		Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//		app_stage.setScene(admin_UI_scene);
//		app_stage.show();
//
//
//	}

//	@FXML
//	private void changeFloor()throws Exception{
//
//		int newFloor = (int) floorSlider.getValue();
//		imageViewMap.setImage()
//	}

	@FXML
	private void mapClicked() {
		//System.out.print("Map Clicked");
		this.imageViewMap.setPickOnBounds(true);


		this.imageViewMap.setOnMouseClicked(e -> {
			System.out.println("["+e.getX()+", "+e.getY()+"]");
			//Paint something at that location
			this.paintOnLocation(e.getX(), e.getY());


		});
	}
	public VBox getContentVBox() {
		return this.contentVBox;
	}

	public void paintOnLocation(double x, double y) {
		//using absolute path for right now, relative path isnt working
		Image map4 = new Image("/4_thefourthfloor.png");

		this.imageViewMap.setImage(map4);
		Circle circ;
		circ = new Circle(x,y,10, Color.web("0x0000FF") );
		//circ.setFill(new ImagePattern(map4, x, y, 0.4, 0.4, true));

		this.contentAnchor.getChildren().add(circ);
		circ.setVisible(true);

	}


}
