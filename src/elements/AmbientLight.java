package elements;

import primitives.Color;

/**
 * A class for ambient light for the scene.
 * Represents a fixed-intensity and fixed-color light 
 * source that affects all objects in the scene equally.
 */
public class AmbientLight extends Light{
	
	/**
	 * Represents how much of the intensity we want.
	 * For example, _Ka = 1 would be brighter than _Ka = 0.3.
	 */
	private double _Ka; 
	
	/**
	 * Constructor for the ambient light
	 * @param color The color of the background ambient light.
	 * @param ka The intensity of the background ambient light.
	 */
	public AmbientLight(Color intensity, double ka) {
		super(intensity);
		_Ka = ka;
	}
	
	/**
	 * Copy constructor for a deep copy of the given object
	 * @param other The object that is being copied
	 */
	public AmbientLight(AmbientLight other) {
		super(other._color);
		_Ka = other._Ka;	
	}

	/**
	 * Function that calculates and returns the intensity.
	 * @return The intensity of the ambient light.
	 */
	public Color getIntensity() {
		return _color.scale(_Ka);
	}
}
