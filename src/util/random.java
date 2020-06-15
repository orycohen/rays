package util;

import java.util.Random;

import primitives.Ray;
import primitives.Vector;

/**
 * Helper for getting random rays and random vectors.
 */
public class random {

	private static Random rand = new Random();
	
	/**
	 * With given ray, radius and length, the function generates
	 * a new random ray that starts from when the given ray starts 
	 * and the direction is bounded inside the cone that is made of the 
	 * given ray, radius and length.
	 * @param ray The ray part of the cone.
	 * @param radius The radius of the base of the cone.
	 * @param length The length of the cone.
	 * @return A new randomly generated ray that is bounded inside the 
	 * cone that consists of the given ray, radius and length.
	 */
	public static Ray genRandRay(Ray ray, double radius, double length) {
		Vector movement = getRandVec(radius);
		Vector direction = ray.getDirection().scale(length).add(movement);
		return new Ray(ray.getPoint(), direction);
	}

	/**
	 * Generate random vector in length between 0 to 
	 * the given radius.
	 * @param radius The maximum length of the vector.
	 * @return New random vector in length between 0 to 
	 * the given radius.
	 */
	public static Vector getRandVec(double radius) {
		return genRandUnitVec().scale(radius * rand.nextDouble());
	}
	
	/**
	 * Generate random unit vector.
	 * @return A new random unit vector.
	 */
	public static Vector genRandUnitVec() {
		return new Vector
				(rand.nextDouble() - 0.5, 
				 rand.nextDouble() - 0.5,
				 rand.nextDouble() - 0.5).normalizedVector();
	}

}
