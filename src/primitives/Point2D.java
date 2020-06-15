
package primitives;

/**
 * This class represents point in the plane.
 * @author orycohen
 */
public class Point2D {
	
	/**
	 * The first coordinate of this Point2D
	 */
	protected final Coordinate _x;
	/**
	 * The second coordinate of this Point2D
	 */
	protected final Coordinate _y;
	
	/**
	 * Constructor for creating a new Point2D object 
	 * that represents a point in the plane.
	 * @param x the X coordinate of this point in the plane
	 * @param y the Y coordinate of this point in the plane
	 */
	public Point2D(double x, double y) {
		_x = new Coordinate(x);
		_y = new Coordinate(y);
	}
	
	/**
	 * Copy constructor for a deep copy of an Point2D object.
	 * @param other the object that being copied
	 */
	public Point2D(Point2D other) {
		_x = new Coordinate(other._x);
		_y = new Coordinate(other._y);
	}
	
	/**
	 * Getter for the X axis Coordinate that is in this point.
	 * @return Anew Coordinate that represents
	 * the X axis Coordinate that is in this point.
	 */
	public Coordinate getX() {
		return new Coordinate(_x);
	}
	
	/**
	 * Getter for the Y axis Coordinate that is in this point.
	 * @return Anew Coordinate that represents
	 * the Y axis Coordinate that is in this point.
	 */
	public Coordinate getY() {
		return new Coordinate(_y);
	}
	
	/**
	 * checks if one point is equal to other one
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Point2D))
			return false;
		if (this == obj)
			return true;
		Point2D other = (Point2D)obj;
		return other == this || 
				(_x.equals(other._x) && _y.equals(other._y));
	}
	
	/**
	 * toString override
	 */
	@Override
	public String toString() {
		return "(" + _x + "," + _y + ")";
	}
}
