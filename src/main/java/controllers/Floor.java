package controllers;

import javafx.scene.image.Image;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by tbasl_000 on 4/8/2017.
 */
public class Floor implements FloorImage
{
	// attributes of the floor class
	private int floorNum;
	private String path;
	private LinkedList<String> floorImages = new LinkedList<>(Arrays.asList(
							  "/MysteryRoom.png", "/MysteryRoom.png", "/MysteryRoom.png",
							  "/4_thefourthfloor.png", "/MysteryRoom.png", "/MysteryRoom.png",
							  "/MysteryRoom.png"));

	// constructor for the floor class
	public Floor(int floorNum) {
		this.floorNum = floorNum;
		this.path = floorImages.get(floorNum - 1);
	}

	// display returns an image as detailed by the string found in path
	public Image display() {
		return new Image(path);
	}


}
