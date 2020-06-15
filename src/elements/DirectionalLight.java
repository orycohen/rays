package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * Class that represents a directional light, like sun for instance.
 * This directional light has no position, but only a direction.
 */
public class DirectionalLight extends Light implements LightSource {

	/**
	 * The _direction represents the direction of 
	 * this DirectionalLight object. 
	 */
	private Vector _direction;
	
	/**
	 * Constructor for a new directional light object. 
	 * @param color This parameter represents the intensity of this 
	 * Directional-Light object.
	 * @param direction This parameter represents the direction of 
	 * the light that is represented by this object.
	 */
	public DirectionalLight(Color color, Vector direction) {
		super(color);
		_direction = new Vector(direction).normalizedVector();
	}

	/**
	 * Getter for the direction of the light that 
	 * is represented by this object
	 * @return A new Vector that represents the
	 * direction of the light that 
	 * is represented by this object
	 */
	public Vector getDirection() {
		return new Vector(_direction);
	}

	/**
	 * This override function is part of the LightSource interface.
	 * It returns the intensity of this light that is represented by
	 * this object.
	 */
	@Override
	public Color getIntensity(Point3D point) {
		return this.getIntensity();
	}
	
	/**
	 * returns only the direction of the light, 
	 * there is no meaning to the position here.
	 */
	@Override
	public Vector getL(Point3D point) {
		return this.getDirection();
	}
}
