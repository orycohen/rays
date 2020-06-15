package elements;

import primitives.Color;

/**
 * Abstract class that every light source should inherits.
 */
public abstract class Light {
	
	/**
	 * The color of the light
	 */
	protected Color _color;

	/**
	 * A protected constructor.
	 * This constructor constructs the _color field of the
	 * Light class.
	 * @param color the color that represents the intensity of this object.
	 */
	protected Light(Color color) {
		_color = new Color(color);
	}

	/**
	 * Getter for the intensity of the color/light that is represented by
	 * this object.
	 * @return A new Color that represents the intensity of this object.
	 */
	public Color getIntensity() {
		return new Color(_color);
	}
	
}
