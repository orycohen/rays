package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * This class represents a spot-light source of light.
 * It has h position in the 3D space and a direction.
 */
public class SpotLight extends PointLight {

	/**
	 * This field represents the direction of of the SpotLight;
	 */
	private Vector _direction;
	
//***************************Constructor**************************//
	/**
	 * Constructor for a new instance of the SpotLight class.
	 * This object will represents a spot-light source of light.
	 * This spot-light has a position, and direction.
	 * @param color The color that represents the intensity of this object.
	 * @param position The position of the point light in the 3D space
	 * @param direction The direction of the light that is represented
	 * by this spot-light.
	 * @param radius The light's radius
	 * @param Kc
	 * @param Kl
	 * @param Kq
	 */
	public SpotLight(Color color, Point3D position, Vector direction,double radius, double Kc, double Kl, double Kq) {
		super(color, position, radius, Kc, Kl, Kq);
		_direction = new Vector(direction).normalizedVector();
	}

//*****************************Getter*****************************//
	
	/**
	 * Getter for the direction of this spot-light source of light.
	 * It simply returns the direction of the light that is represented 
	 * by this SpotLight object.
	 * @return The direction of the light that is represented by this SpotLight object.
	 */
	public Vector getDirection() {
		return new Vector(_direction);
	}
	
	/**
	 * This overrides the getIntensity function that is implemented 
	 * by the PointLight class that this SpotLight inherits.
	 * It calculates the intensity of the light from
	 * this spot-light in a given point in the 3D space.
	 */
	@Override
	public Color getIntensity(Point3D point) {
		double SpotLightFactor = getL(point).normalizedVector().dotProduct(_direction);
		
		//If the dot product result is negative it means that there is no light from this 
		//spot-light in this given point.
		return super.getIntensity(point).scale(SpotLightFactor < 0 ? 0 : SpotLightFactor);
	}
}
