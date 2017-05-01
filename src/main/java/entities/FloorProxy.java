package entities;

import javafx.scene.image.Image;

import java.util.*;

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

	@Override
	public String getName() {
		return this.building;
	}

	@Override
	public int getNumber() {
		return this.floorNum;
	}
	// create floorProxies for every floor

	private static LinkedList<FloorProxy> FLOORS = new LinkedList<>(Arrays.asList(
				new FloorProxy("Building1",1),
				new FloorProxy("Building1",2),
				new FloorProxy("Building1",3),
				new FloorProxy("Building1",4),
				new FloorProxy("Building1",5),
				new FloorProxy("Building1",6),
				new FloorProxy("Building1",7),
				new FloorProxy("Building2", 1),
				new FloorProxy("Building2", 2),
				new FloorProxy("Building2", 3),
				new FloorProxy("Building2", 4),
				new FloorProxy("Outside", 1)));

	/**
	 * Get a floor by building and number
	 */
	public static FloorProxy getFloor(String building, int floorNum) {
		return FLOORS.stream()
				.filter(floor -> (floor.getNumber() == floorNum) && (floor.getName().compareToIgnoreCase(building) == 0))
				.findAny().orElse(null);
		//return EVERYTHING.get(building).get(floorNum-1);
	}

	public static List<FloorProxy> getFloors() {
		return new ArrayList<>(FloorProxy.FLOORS);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getName());
		sb.append(" floor ");
		sb.append(this.getNumber());
		return sb.toString();
	}

}
