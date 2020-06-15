
package primitives;

import util.Counter;
import util.calcs;
/**
 * This class that represent a vector in the plane: (x,y,z).
*/
public class Vector {
	
	public static Vector zero = new Vector(0,0,0);
	
	/**
	 * the head of the vector
	 */
	private final Point3D _head;
	
	private static Counter counter = new Counter();
	public static Counter getNewCounter() {return counter;}
	
	/**
	 * Constructor that gets a point in the space and save it 
	 * as the head of the vector
	 * @param head The head of the vector
	 */
	public Vector(Point3D head) {
		_head = new Point3D(head);
		try { counter.increase(); } catch(Exception e) {}
	}
	
	/**
	 * Constructor that gets three points and builds a new Point3D 
	 * @param x The x coordinate of the vector. 
	 * @param y The y coordinate of the vector. 
	 * @param z The z coordinate of the vector. */
	public Vector(double x, double y, double z) {
		_head = new Point3D(x, y, z);
		try { counter.increase(); } catch(Exception e) {}
	}
	
	/**
	 * Copy constructor for a deep copy of an Vector.
	 * @param other the object that being copied
	 */
	public Vector(Vector other) {
		this._head = new Point3D(other._head);
		counter.increase();
	}

	/**
	 * checks if the object is equal to a second one*/
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Vector))
			return false;
		if (this == obj)
			return true;
		Vector other = (Vector)obj;
		return this._head.equals(other._head);
	}
	
	@Override
	public String toString() {
		return "" + _head;
	}
	
	/**
	 * Getter for the head of the vector that is represented by 
	 * this object.
	 * @return
	 */
	public Point3D getHead() {
		return new Point3D(_head);
	}
	
	/**
	 * The function performs a vectorial subtraction between two vectors
	 * @param other The vector that being subtracted from this vector.
	 * @return a new Vector that is the result of the subtraction.
	 */
	public Vector subtract(Vector other) {
		return new Vector(_head.subtract(other._head));
	}
	
	/**
	 * The function performs a vectorial addition between two vectors.
	 * @param other The vector that being added to this vector
	 * @return a new Vector that is the result of the addition.
	 */
	public Vector add(Vector other) {
		return new Vector(_head.add(other._head));			
	}
	
	/**
	 * The function performs a scalar multiplication with 
	 * the vector that is represented by this object.
	 * @param scalar The scalar that multiplies the vector.
	 * @return a new multiplied Vector that is the 
	 * result of the scaling operation of this function
	 */
	public Vector scale(double scalar) {
		return new Vector(_head.mult(scalar));			
	}
	
	/**
	 * The function performs a dot-product of two vectors
	 * @param other the second vector in the dot-product. the first 
	 * vector is 'this' vector.
	 * @return The result of the dot-product.
	 */
	public double dotProduct(Vector other) {
		return calcs.mult(_head.getX().getNumber() , other._head.getX().getNumber()) +
			   calcs.mult(_head.getY().getNumber() , other._head.getY().getNumber()) + 
			   calcs.mult(_head.getZ().getNumber() , other._head.getZ().getNumber());
	}
	
	/**
	 * The function performs a cross product between two vectors
	 * @param other the second vector in the cross-product
	 * @return A new vector that is the result of the cross-product 
	 * operation.
	 */
	public Vector crossProduct(Vector other) {
		
		//(a1,a2,a3) is 'this' vector and (b1,b2,b3) is 
		//the 'other' vector. 
		double a1 = _head.getX().getNumber(), 
			   a2 = _head.getY().getNumber(),
			   a3 = _head.getZ().getNumber(),
			   b1 = other._head.getX().getNumber(),
			   b2 = other._head.getY().getNumber(),
			   b3 = other._head.getZ().getNumber();
		
		//calculate the coordinates as the theorem of linear algebra
		
		double x = calcs.add(calcs.mult(a2, b3),-calcs.mult(a3, b2)),
			   y = calcs.add(calcs.mult(a3, b1),-calcs.mult(a1, b3)),
			   z = calcs.add(calcs.mult(a1, b2),-calcs.mult(a2, b1));
		return new Vector(x,y,z);
	}
	
	/**
	 * The function calculates the length of the vector that is represented 
	 * by this object and rounds the 4th digit after the dot.
	 * @return the rounded number, 4 digits,  of the length of the vector.
	 */
	public double vectorLength() {
		
		//rounding the numbers so we do not get 
		//something like 0.999999.... and get mistakes.
		double result = Math.round(_head.distance(Point3D.zero) * 10000);
		return result / 10000;
	}
	
	/**
	 * The function normalizes 'this' vector so it would be in length 1.
	 * @return the normalized vector, and null in case where 
	 * the given vector is the axis origin (0,0,0)
	 */
	public Vector normalizedVector() {
		double thisVectorLength = 
				_head.distance(Point3D.zero);
		
		//return null if the vector is (0,0,0)
		if (thisVectorLength == 0)
			return null;
		double x = _head.getX().getNumber() / thisVectorLength,
			   y = _head.getY().getNumber() / thisVectorLength,
			   z = _head.getZ().getNumber() / thisVectorLength;
				
		return new Vector(x, y, z);
	}
}
