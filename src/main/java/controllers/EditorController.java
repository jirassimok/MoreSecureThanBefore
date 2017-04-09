package controllers;


import entities.Directory;
import entities.Professional;
import entities.Room;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import entities.Node;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import main.ApplicationController;
import main.DatabaseException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringJoiner;

public class EditorController extends MapDisplayController implements Initializable
{
	@FXML
	private Button addRoomBtn;
	@FXML
	private Button logoutBtn;
	@FXML
	private TextField nameField;
	@FXML
	private TextField descriptField;
	@FXML
	private TextField xCoordField;
	@FXML
	private TextField yCoordField;
	@FXML
	private ImageView imageViewMap;
	@FXML
	private Pane contentPane;
	@FXML
	private Button modifyRoomBtn;
	@FXML
	private Button cancelBtn;
	@FXML
	private Button deleteRoomBtn;
	@FXML
	private Button confirmBtn;
	@FXML
	private ChoiceBox<Professional> proChoiceBox;
	@FXML
	private Label roomTextLbl;
	@FXML
	private Button addCustomProBtn;
	@FXML
	private Button deleteProfBtn;
	@FXML
	protected Pane linePane;
	@FXML
	protected Pane nodePane;
	@FXML
	public AnchorPane contentAnchor = new AnchorPane();

	final double SCALE_DELTA = 1.1; //The rate to scale
	private double clickedX, clickedY; //Where we clicked on the anchorPane
	private boolean beingDragged; //Protects the imageView for being dragged

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Load
		this.setPanes(linePane, nodePane); //Set the panes
		this.directory = ApplicationController.getDirectory(); //Grab the database controller from main and use it to populate our directory
		this.map4 = new Image("/4_thefourthfloor.png"); //TODO: load this from the directory
		this.imageViewMap.setImage(this.map4); //Load background

		//Init
		this.populateChoiceBox(); //populate box for professionals
		this.proList = new ArrayList<>(); //TODO: OBSOLETE, should be in directory
		for (Professional pro: this.directory.getProfessionals()) {
			this.proList.add(pro);
		}
		this.selectChoiceBox();
		this.kiosk = null;
		for (Room r : this.directory.getRooms()) {
			if (r.getName().equalsIgnoreCase("YOU ARE HERE")) {
				this.kiosk = r;
			}
		}

		this.displayNodes(); //draws the nodes from the directory
		this.redrawLines();  //deletes all the lines then draws them again from the directory

		//Lets us click through items
		this.imageViewMap.setPickOnBounds(true);
		this.contentAnchor.setPickOnBounds(false);
		this.topPane.setPickOnBounds(false);

		this.installPaneLisenters();

	}

	@FXML
	private void logoutBtnClicked() {
		try {
			Parent userUI = (BorderPane) FXMLLoader.load(this.getClass().getResource("/FinalUI.fxml"));
			this.botPane.getScene().setRoot(userUI);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void addProfToRoom() {
		// TODO: Change use of instanceof to good coding standards
//		if (this.selectedProf == null || this.selectedNode == null || !(this.selectedNode instanceof Room)) {
//			return;
//		} else {
//			this.selectedProf.addLocation((Room)this.selectedNode);
//			this.roomList = "";
//			for (Room r: this.selectedProf.getLocations())
//				this.roomList += r.getName() + ", ";
//			this.roomTextLbl.setText(this.roomList);
//		}
	}

	@FXML
	public void delProfFromRoom() {
//		if (this.selectedNode == null) {
//			return;
//		} else {
//			this.selectedProf.getLocations().forEach(room -> {
//				if(room.equals(this.selectedNode)) {
//					this.selectedProf.removeLocation(room);
//				}
//			});
//
//			this.roomList = "";
//			for (Room r: this.selectedProf.getLocations())
//				this.roomList += r.getName() + ", ";
//			this.roomTextLbl.setText(this.roomList);
//		}
	}

	@FXML
	public void refreshBtnClicked() {
		//TODO
//		this.populateChoiceBox();
//
//		for (Professional pro: this.directory.getProfessionals()) {
//			this.proList.add(pro);
//
//		}
	}

	@FXML
	public void addCustomProBtnPressed() throws IOException {
		//TODO
//		FXMLLoader loader = new FXMLLoader();
//		loader.setLocation(this.getClass().getResource("/AddProUI.fxml"));
//		this.addProController = loader.getController();
//		//this.addProController.setEditorController(this);
//		Scene addProScene = new Scene(loader.load());
//		Stage addProStage = new Stage();
//		addProStage.setScene(addProScene);
//
//		addProStage.showAndWait();
	}

	@FXML
	public void deleteProfBtnClicked () {
		//TODO
//		this.directory.removeProfessional(this.selectedProf);
//	//	this.refreshBtnClicked();
	}


	@FXML
	public void confirmBtnPressed() {
		//TODO
//		this.directory.getRooms().forEach(room -> {
//			System.out.println("Attempting to save room: " + room.getName() + " to database...");
//		});
//
//		try {
//
//
//			ApplicationController.dbc.destructiveSaveDirectory(this.directory);
//		} catch (DatabaseException e) {
//			System.err.println("\n\nDATABASE DAMAGED\n\n");
//			e.printStackTrace();
//			System.err.println("\n\nDATABASE DAMAGED\n\n");
//		}
	}

	@FXML
	public void addRoomBtnClicked() {
		this.addRoom(this.readX(), this.readY(), this.nameField.getText(), this.descriptField.getText());
	}

	@FXML
	public void modifyRoomBtnClicked() {
		if(this.selectedNode == null) return;
		//TODO: Change use of instanceof to good coding standards
		if(this.selectedNode.containsRoom()) {
			this.updateSelectedRoom(this.readX(), this.readY(), this.nameField.getText(), this.descriptField.getText());
		} else {
			this.updateSelectedNode(this.readX(), this.readY());
		}
	}

	@FXML
	public void deleteRoomBtnClicked() {
		this.deleteSelectedNode();
	}


	public void selectChoiceBox(){
		//TODO
//		this.proChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
//			@Override
//			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//				//proTextLbl.setText(proList.get(newValue.intValue() - 1).toString());
//
//				if(proList.size() != 0 && newValue.intValue() >= 0) {
//					EditorController.this.selectedProf = proList.get(newValue.intValue());
//				}
//
//				// Build a string listing the names of the professional's rooms
//				StringJoiner roomList = new StringJoiner(", ");
//				selectedProf.getLocations().forEach(room -> roomList.add(room.getName()));
//
//				roomTextLbl.setText(roomList.toString());
//			}
//		});
	}

	public void populateChoiceBox() {
		this.proChoiceBox.setItems(FXCollections.observableArrayList(this.directory.getProfessionals()));
	}


	private void addRoom(double x, double y, String name, String description) { //TODO
		//		Room newRoom = new Room(x-this.RECTANGLE_WIDTH/2, y-this.RECTANGLE_HEIGHT/2, name, description);
		//		this.directory.addRoom(newRoom);
		//		this.paintRoomOnLocation(newRoom);
	}

	private double readX() {
		return Double.parseDouble(this.xCoordField.getText());
	}

	private double readY() {
		return Double.parseDouble(this.yCoordField.getText());
	}

	private void addNode(double x, double y) {
		Node newNode = this.directory.newNode(x, y);
		this.paintNode(newNode);
		newNode.getShape().setOnMouseClicked((MouseEvent e) -> {
			onShapeClick(e, newNode);
		});
		newNode.getShape().setOnMouseDragged((MouseEvent e) -> {
			onShapeDrag(e, newNode);
		});
		newNode.getShape().setOnMouseReleased((MouseEvent e) -> {
			onShapeReleased(e, newNode);
		});
		newNode.getShape().setOnMousePressed((MouseEvent e) -> {
			this.primaryPressed = e.isPrimaryButtonDown();
			this.secondaryPressed = e.isSecondaryButtonDown();
		});
	}

	private void updateSelectedRoom(double x, double y, String name, String description) {
//		this.selectedNode.moveTo(x, y);
//		((Room) this.selectedNode).setName(name);
//		((Room) this.selectedNode).setDescription(description);
//		Rectangle selectedRectangle = (Rectangle) this.selectedShape;
//		selectedRectangle.setX(x);
//		selectedRectangle.setY(y);
	}

	private void updateSelectedNode(double x, double y) { //TODO
		this.selectedNode.moveTo(x, y);

		Circle selectedCircle = (Circle) this.selectedShape;
		selectedCircle.setCenterX(x);
		selectedCircle.setCenterY(y);
	}

	private void deleteSelectedNode() { //TODO
		if(this.selectedNode == null) return;


		this.selectedNode.disconnectAll();
		this.directory.removeNodeOrRoom(this.selectedNode);
		this.selectedNode = null;
		// now garbage collector has to do its work

		this.contentPane.getChildren().remove(this.selectedShape);
		this.selectedShape = null;

		this.redrawLines();

	}

	public void redrawLines() {
		this.botPane.getChildren().clear();
		this.directory.getNodes().forEach(node -> {
				node.getNeighbors().forEach(Neighbor -> {
					this.paintLine(node,Neighbor);
				});
		});
	}


	public void setFields(double x, double y) {
		this.xCoordField.setText(x+"");
		this.yCoordField.setText(y+"");
	}



	///////////////////////
	/////EVENT HANDLERS////
	///////////////////////

	public void installPaneLisenters(){
		botPane.setOnMouseClicked(e -> {
			this.setFields(e.getX(), e.getY());
			//Create node on double click
			if(e.getClickCount() == 2) {
				this.addNode(e.getX(), e.getY());
			}

			//TODO change only this shape
//			if(this.selectedShape != null) {
//				if(this.selectedNode.getRoom() != null) {
//					if (this.selectedNode.getRoom().getName().equalsIgnoreCase(this.KIOSK_NAME)) {
//						this.selectedShape.setFill(this.KIOSK_COLOR);
//					} else {
//						this.selectedShape.setFill(this.DEFAULT_SHAPE_COLOR);
//					}
//				}
//			}
			this.displayNodes();

			this.selectedNode = null;
			this.selectedShape = null;
		});

		contentAnchor.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override public void handle(ScrollEvent event) {
				event.consume();
				if (event.getDeltaY() == 0) {
					return;
				}
				double scaleFactor =
						(event.getDeltaY() > 0)
								? SCALE_DELTA
								: 1/SCALE_DELTA;
				contentAnchor.setScaleX(contentAnchor.getScaleX() * scaleFactor);
				contentAnchor.setScaleY(contentAnchor.getScaleY() * scaleFactor);
			}
		});

		contentAnchor.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				clickedX = event.getX();
				clickedY = event.getY();
			}
		});

		contentAnchor.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if(!beingDragged) {
					contentAnchor.setTranslateX(contentAnchor.getTranslateX() + event.getX() - clickedX);
					contentAnchor.setTranslateY(contentAnchor.getTranslateY() + event.getY() - clickedY);
				}
				event.consume();
			}
		});
	}

	public void onShapeClick(MouseEvent e, Node n) {
		// update text fields
		this.setFields(n.getX(), n.getY());

		// check if you single click
		// so, then you are selecting a node
		if(e.getClickCount() == 1 && this.primaryPressed) {
//			if(this.selectedShape != null) {
//
//				//TODO change only this shape
//				if(this.selectedShape != null) {
//					if(this.selectedNode.getRoom() != null) {
//						if (this.selectedNode.getRoom().getName().equalsIgnoreCase(this.KIOSK_NAME)) {
//							this.selectedShape.setFill(this.KIOSK_COLOR);
//						} else {
//							this.selectedShape.setFill(this.DEFAULT_SHAPE_COLOR);
//						}
//					}
//				}
//
////				this.displayNodes();
//
//			} else {
//				this.selectedShape.setFill(this.DEFAULT_SHAPE_COLOR);
//			}

			this.selectedShape = (Shape) e.getSource();
			this.selectedNode = n;
			this.selectedShape.setFill(this.SELECTED_SHAPE_COLOR);
		} else if(this.selectedNode != null && !this.selectedNode.equals(n) && this.secondaryPressed) {
			// ^ checks if there has been a node selected,
			// checks if the node selected is not the node we are clicking on
			// and checks if the button pressed is the right mouse button (secondary)

			// finally check if they are connected or not
			// if they are connected, remove the connection
			// if they are not connected, add a connection
			this.selectedNode.connectOrDisconnect(n);
			this.redrawLines();
		}
	}

	// This is going to allow us to drag a node!!!
	public void onShapeDrag(MouseEvent e, Node n) {
		beingDragged = true;
		if(this.selectedNode != null && this.selectedNode.equals(n)) {
			if(this.primaryPressed) {
				this.selectedShape = (Shape) e.getSource();
				this.updateSelectedNode(e.getX(), e.getY());
				this.setFields(this.selectedNode.getX(), this.selectedNode.getY());
				this.redrawLines();
			} else if(this.secondaryPressed) {
				// right click drag on the selected node
			}
		}
	}

	public void onShapeReleased(MouseEvent e, Node n) {
		this.releasedX = e.getX();
		this.releasedY = e.getY();

		// if the releasedX or Y is negative we want to remove the node

		if(this.releasedX < 0 || this.releasedY < 0) {
			this.deleteSelectedNode();
		}
	}



}
