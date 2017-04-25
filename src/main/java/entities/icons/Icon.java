package entities.icons;

import java.util.function.Consumer;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class Icon
		extends Group
{
	private Node symbol;
	private Label label;
	private ImageView image;
	private double labelOffsetX = 0;
	private double labelOffsetY = 0;

	Icon(ImageView image, Label label) {
		super(image, label);
		this.image = image;
		this.label = label;
	}

	public Icon(Node symbol, Label label) {
		super(symbol, label);
		this.symbol = symbol;
		this.label = label;
	}

	Icon(Node symbol) {
		this(symbol, null);
	}

	public void relocate(double x, double y) {
		this.symbol.setLayoutX(x);
		this.symbol.setLayoutY(y);
	}

	public Node getSymbol() {
		return this.symbol;
	}

	public Label getLabel() {
		return this.label;
	}


	void setSymbol(Node symbol) {
		this.symbol = symbol;
	}

	void setLabel(Label label) {
		this.label = label;
	}

	public void moveTo(double x, double y) {
		this.image.setLayoutX(x);
		this.image.setLayoutY(y);
		this.label.setLayoutX(x + this.labelOffsetX);
		this.label.setLayoutY(y + this.labelOffsetY);
	}

	public void setLabelOffset(double x, double y) {
		this.labelOffsetX = x;
		this.labelOffsetY = y;
		this.label.setLayoutX(this.label.getLayoutX() + this.labelOffsetX);
		this.label.setLayoutY(this.label.getLayoutY() + this.labelOffsetY);
	}

	/**
	 * Apply the given consumer function to this icon's symbol, if present
	 *
	 * @param consumer A function that may take a single javafx Node as its only argument
	 */
	public void applyToSymbol(Consumer<? super Node> consumer) {
		if (this.symbol != null) {
			consumer.accept(this.symbol);
		}
	}

	/**
	 * Apply the given consumer function to this icon's label, if present
	 *
	 * @param consumer A function that may take a single javafx Node as its only argument
	 */
	public void applyToLabel(Consumer<? super Node> consumer) {
		if (this.symbol != null) {
			consumer.accept(this.symbol);
		}
	}
}
