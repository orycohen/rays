package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * Interface for a light source.
 * This interface includes operation for getting the 
 * intensity of the light in a given point, and operation 
 * for getting the vector from the light source to the 
 * given point. 
 */
public interface LightSource {
	
	/**
	 * Operation for calculating the intensity of the light source
	 * in a given point in the 3D space. 
	 * @param point The point in which we calculate the intensity. 
	 * @return A color that represents the intensity of the source light in the given point.
	 */
	public Color getIntensity(Point3D point);
	
	/**
	 * Operation for getting the vector from the light source to 
	 * the given point where we calculate the intensity.
	 * @param point The point in which we want to calculate the intensity.
	 * @return A new normalized vector from the light source to the given point where we calculate the intensity.
	 */
	public Vector getL(Point3D point);
}
