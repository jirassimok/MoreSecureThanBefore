package entities;

import javafx.scene.image.Image;
import main.ApplicationController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class Floor implements FloorImage
{
	// Attributes of the floor class
	private int floorNum;
	private FloorImages path;
	private String thumbPath;
	private String buildingName;


	class FloorImages {
		public String vistorImage;
		public String professionalImage;

		FloorImages(String vistorImage, String professionalImage){
			this.vistorImage = vistorImage;
			this.professionalImage = professionalImage;
		}
	}

	// Linked list containing the string names of the different floor images.
	private LinkedList<FloorImages> building1 = new LinkedList<>(Arrays.asList(
			new FloorImages("/1_thefirstfloor.png", "/Admin1.png"),
			new FloorImages("/2_thesecondfloor.png", "/Admin2.png"),
			new FloorImages("/3_thethirdfloor.png", "/Admin3.png"),
			new FloorImages("/4_thefourthfloor.png","/Admin4.png" ),
			new FloorImages("/5_thefifthfloor.png", "/Admin5.png"),
			new FloorImages("/6_thesixthfloor.png", "/Admin6.png"),
			new FloorImages("/7_theseventhfloor.png", "/Admin7.png")));

	private LinkedList<FloorImages> building2 = new LinkedList<>(Arrays.asList(
			new FloorImages("/building21.png", "/building21.png"),
			new FloorImages("/building22.png", "/building21.png"),
			new FloorImages("/building23.png", "/building21.png"),
			new FloorImages("/building24.png", "/building21.png")));

	private LinkedList<FloorImages> outline = new LinkedList<>(Arrays.asList(
			new FloorImages("/outsidearea.png", "/outsidearea.png")));

	private HashMap<String, LinkedList<FloorImages>> floorImages = new HashMap<>();

	private HashMap<String, LinkedList<FloorImages>> getFlImg(){
		floorImages.put("Building1", building1);
		floorImages.put("Building2", building2);
		floorImages.put("Outside", outline);
		return floorImages;
	}

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
		this.thumbPath = ALL_THUMBS.get(building.toUpperCase()).get(floorNum - 1);

		this.path = getFlImg().get(building).get(floorNum - 1);
	}

	/** takes the String name of the path attribute and loads the image attached to that path
	 *
	 * @return an image of the specified path.
	 */
	public Image display() {
		Image ret;
		if(ApplicationController.getDirectory().isProfessional()) {
			ret = new Image(path.professionalImage, true); // backgroound loading prevents crashing sometimes
		}else{
			ret = new Image(path.vistorImage, true); // backgroound loading prevents crashing sometimes
		}
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
