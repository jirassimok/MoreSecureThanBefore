package controllers.shared;

import javafx.scene.image.Image;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class Floor implements FloorImage
{
	// Attributes of the floor class

	private int floorNum;
	private String path;
	private String thumbPath;
	private String buildingName;

	// Linked list containing the string names of the different floor images.
	private LinkedList<String> building1FloorImages = new LinkedList<>(Arrays.asList(
			"/1_thefirstfloor.png", "/2_thesecondfloor.png", "/3_thethirdfloor.png",
			"/4_thefourthfloor.png", "/5_thefifthfloor.png", "/6_thesixthfloor.png",
			"/7_theseventhfloor.png"));

	private LinkedList<String> building2FloorImages = new LinkedList<>(Arrays.asList(
			"/building21.png", "/building22.png", "/building23.png", "/building24.png"));

	private LinkedList<String> outsideFloorImages = new LinkedList<>(Arrays.asList(
			"/outsidearea.png"));

	private HashMap<String, LinkedList<String>> floorImages = new HashMap<>();

	private HashMap<String, LinkedList<String>> getFlImg(){
		floorImages.put("Building1", building1FloorImages);
		floorImages.put("Building2", building2FloorImages);
		floorImages.put("Outside", outsideFloorImages);
		return floorImages;
	}

	// Linked list containing the string names of the different floor images.
	private LinkedList<String> building1Thumbnails = new LinkedList<>();

	//private LinkedList<String> building2Thumbnals = new

	private static HashMap<String, LinkedList<String>> ALL_THUMBS = new HashMap<>();
	static {
		LinkedList<String> building1Thumbs = new LinkedList<>();
		building1Thumbs.add("/t_building1_1.png");
		building1Thumbs.add("/t_building1_2.png");
		building1Thumbs.add("/t_building1_3.png");
		building1Thumbs.add("/t_building1_4.png");
		building1Thumbs.add("/t_building1_5.png");
		building1Thumbs.add("/t_building1_6.png");
		building1Thumbs.add("/t_building1_7.png");
		ALL_THUMBS.put("BUILDING1", building1Thumbs);

		LinkedList<String> building2Thumbs = new LinkedList<>();
		building2Thumbs.add("/t_building21.png");
		building2Thumbs.add("/t_building22.png");
		building2Thumbs.add("/t_building23.png");
		building2Thumbs.add("/t_building24.png");
		ALL_THUMBS.put("BUILDING2", building2Thumbs);

		LinkedList<String> outsideThumbs = new LinkedList<>();
		outsideThumbs.add("/t_outsidearea.png");
		ALL_THUMBS.put("OUTSIDE", outsideThumbs);
	}

	// constructor for the floor class
	public Floor(String building, int floorNum) {
		this.floorNum = floorNum;
		this.path = getFlImg().get(building).get(floorNum - 1);
		this.thumbPath = ALL_THUMBS.get(building.toUpperCase()).get(floorNum - 1);
	}

	/** takes the String name of the path attribute and loads the image attached to that path
	 *
	 * @return an image of the specified path.
	 * TODO: The image doesn't load sometimes. Having it load in the background prevents it from crashing
	 */
	public Image display() {
		Image ret;
		ret = new Image(path, true);

		return ret;
	}

	public Image displayThumb() {
		Image ret;
		ret = new Image(thumbPath, true);

		return ret;
	}

	@Override
	public String getName() {
		return this.buildingName;
	}

	@Override
	public int getNumber() {
		return this.floorNum;
	}


}
