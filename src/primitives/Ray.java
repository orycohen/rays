
package primitives;

import util.Counter;

/**
 * This class represents a ray in the 3D space.
 * @author orycohen
 */
public class Ray {
	
	/**
	 * The point from which the ray starts.
	 */
	private final Point3D _point;
	/**
	 * The direction of the ray.
	 */
	private final Vector _direction;
	
	private static Counter counter = new Counter();
	public static Counter getNewCounter() {return counter;}
	
	/**
	 * Constructor for creating a new instance of this class
	 * @param point the start of the ray.
	 * @param direction the direction of the ray.
	 */
	public Ray(Point3D point, Vector direction) {
		if (direction.vectorLength() == 0)
			throw new IllegalArgumentException
			("the direction vector cannot be the zero vector");
		_point = new Point3D(point);
		_direction = new Vector(direction).normalizedVector();
		counter.increase();
	}
	
	/**
	 * Copy constructor for a deep copy of an Ray object.
	 * @param other the object that being copied
	 */
	public Ray(Ray other) {
		this._point = new Point3D(other._point);
		this._direction = new Vector(other._direction).normalizedVector();
		counter.increase();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Ray))
			return false;
		if (this == obj)
			return true;
		Ray other = (Ray)obj;
		return (_point.equals(other._point) && 
				_direction.equals(other._direction));
	}
	
	@Override
	public String toString() {
		return String.format
			("point: " + _point + ", direction: " + _direction);
	}
	
	/**
	 * Getter for the point from which the ray starts.
	 * @return A new Point3D that represents the 
	 * point from which the ray starts.
	 */
	public Point3D getPoint() {
		return new Point3D(_point);
	}
	
	/**
	 * Getter for the direction of the ray that is 
	 * represented by this object.
	 * @return A new Vector that represents the 
	 * direction of the ray that is 
	 * represented by this object.
	 */
	public Vector getDirection() {
		return new Vector(_direction);
	}
}
