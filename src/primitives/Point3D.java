
package primitives;

import util.Counter;

/**
 * This class represent a point in the space.
 * */
public class Point3D extends Point2D {
	
	/**
	 * A static field for re-using the (0,0,0) point.
	 */
	public static Point3D zero = new Point3D(0,0,0);
	
	/**
	 * The third coordinate that represents the third dimension.
	 */
	private final Coordinate _z;
	
	private static Counter counter = new Counter();
	public static Counter getNewCounter() {return counter;}
	
	/**
	 * Constructor for creating a new Point3D object.
	 * This object represents a point in the space.
	 * @param x the X coordinate of the point in the space.
	 * @param y the Y coordinate of the point in the space.
	 * @param z the Z coordinate of the point in the space.
	 */
	public Point3D(double x, double y, double z) {
		super(x,y);
		_z = new Coordinate(z);
		try { counter.increase(); } catch(Exception e) {}
	}
	
	/**
	 * Copy constructor for a deep copy of a Point3D object.
	 * @param other the object that being copied
	 */
	public Point3D(Point3D other) {
		super(other);
		_z = new Coordinate(other._z);
		counter.increase();
	}
	
	/**
	 * Getter for the third coordinate's, Z coordinate, object.
	 * @return A new Coordinate that represents the Z axis 
	 * value of this point
	 */
	public Coordinate getZ() {
		return new Coordinate(_z);
	}
	
	/**checks if one point is equal to other one*/
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Point3D))
			return false;
		if (this == obj)
			return true;
		Point3D other = (Point3D)obj;
		return other == this || 
				(super.equals(other) && _z.equals(other._z));
	}
	
	/**
	 * toString override
	*/
	@Override
	public String toString() {
		return String.format("(%5.3f, %5.3f, %5.3f)",
				_x.getNumber(), _y.getNumber(), _z.getNumber());
	}
	
	/**
	 * subtracts two points in the plane and returns the result 
	 * @param other the point that is subtracted from 'this' point
	 * @return a new Point3D that is the result of the subtraction.
	 */
	public Point3D subtract(Point3D other) {
		return new Point3D
				(this._x.subtract(other._x).getNumber(),
				 this._y.subtract(other._y).getNumber(),
				 this._z.subtract(other._z).getNumber());
	}
	
	/**
	 * adds two points in the plane and returns the result
	 * @param other the point that is being added to 'this' point
	 * @return a new Point3D that is the result of the addition.
	 */
	public Point3D add(Point3D other) {
		return new Point3D
				(_x.add(other._x).getNumber(),
				 _y.add(other._y).getNumber(),
				 _z.add(other._z).getNumber());
	}
	
	/**
	 * multiplies this point with a given scalar.
	 * @param scalar The scalar that multiplies this Point3D
	 * @return a new point that is the result of the multiplication.
	 */
	public Point3D mult(double scalar) {
		return new Point3D
				//multiplication of a point with a scalar
				(this._x.scale(scalar).getNumber(),
				 this._y.scale(scalar).getNumber(),
				 this._z.scale(scalar).getNumber());
	}
	
	/**distance between two points in the space
	 * @param other another point
	 * @return the distance between this point to the other point*/
	public double distance(Point3D other) {
		
		double num1 = _x.subtract(other._x).getNumber(),
				num2 = _y.subtract(other._y).getNumber(),
				num3 = _z.subtract(other._z).getNumber();
		
		double result = Math.sqrt(
				num1 * num1 +
				num2 * num2 +
				num3 * num3);
		return result;
	}
}
