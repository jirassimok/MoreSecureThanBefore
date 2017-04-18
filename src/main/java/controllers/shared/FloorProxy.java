package controllers.shared;

import javafx.scene.image.Image;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by tbasl_000 on 4/8/2017.
 */
public class FloorProxy implements FloorImage
{

	private String building;
	private int floorNum;
	private Floor floor;

	public FloorProxy(String building, int floor) {
		this.building = building;
		this.floorNum = floor;
	}

	/** checks to see if the floor has been loaded, then returns the image attached to that
	 *  floor.
	 *
	 * @return The image of the floor we are asking for
	 */
	public Image display() {
		if(this.floor == null){
			this.floor = new Floor(this.building, this.floorNum);
		}
		return floor.display();
	}

	public Image displayThumb() {
		if(this.floor == null){
			this.floor = new Floor(this.building, this.floorNum);
		}
		return floor.displayThumb();
	}
	// create floorProxies for every floor
	private static FloorProxy building1Floor1 = new FloorProxy("Building1",1);
	private static FloorProxy building1Floor2 = new FloorProxy("Building1",2);
	private static FloorProxy building1Floor3 = new FloorProxy("Building1",3);
	private static FloorProxy building1Floor4 = new FloorProxy("Building1",4);
	private static FloorProxy building1Floor5 = new FloorProxy("Building1",5);
	private static FloorProxy building1Floor6 = new FloorProxy("Building1",6);
	private static FloorProxy building1Floor7 = new FloorProxy("Building1",7);
	private static FloorProxy building2Floor1 = new FloorProxy("Building2", 1);
	private static FloorProxy building2Floor2 = new FloorProxy("Building2", 2);
	private static FloorProxy building2Floor3 = new FloorProxy("Building2", 3);
	private static FloorProxy building2Floor4 = new FloorProxy("Building2", 4);
	private static FloorProxy outsideFloor = new FloorProxy("Outside", 1);

	private static LinkedList<FloorProxy> building1Maps = new LinkedList<>(
			Arrays.asList(building1Floor1, building1Floor2, building1Floor3, building1Floor4,
					building1Floor5, building1Floor6, building1Floor7));

	private static LinkedList<FloorProxy> building2Maps = new LinkedList<>(
			Arrays.asList(building2Floor1, building2Floor2, building2Floor3, building2Floor4));

	private static LinkedList<FloorProxy> outsideMaps = new LinkedList<>(
			Arrays.asList(outsideFloor));

	private static HashMap<String, LinkedList<FloorProxy>> floorMaps = new HashMap<>();

	public static HashMap<String, LinkedList<FloorProxy>> getFloorMaps() {
		floorMaps.put("Building1", building1Maps);
		floorMaps.put("Building2", building2Maps);
		floorMaps.put("Outside", outsideMaps);
		return floorMaps;
	}
}
