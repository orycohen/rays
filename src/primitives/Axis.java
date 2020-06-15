package primitives;

/**
 * Class for representing a new axis.
 * Helpful when there is need to put a texture on a Geometry.
 * @author oricohen
 */
public class Axis {
	
	/**
	 * The origin point of the axis. 
	 * Can be any point in the 3D space.
	 */
	private Point3D _origin; 
	
	/**
	 * 
	 */
	public static Axis zero = new Axis(Point3D.zero, new Vector(1,0,0), new Vector(0,1,0), new Vector(0,0,1));
	
	/**
	 * The vector that is orthogonal to the plane spanned the other two vectors, namely the X vector.
	 */
	private Vector _xVector; 
	
	
	/**
	 * The vector that is orthogonal to the plane spanned the other two vectors, namely the Y vector.
	 */
	private Vector _yVector; 
	
	
	/**
	 * The vector that is orthogonal to the plane spanned the other two vectors, namely the Z vector.
	 */
	private Vector _zVector;
	
	/**
	 * A constructor to build a new axis in the 3D space.
	 * <b>Note</b> that this constructor assumes that the given 3 vectors are orthogonal.
	 * @param origin The origin point of the axis. It can be any point in the 3D space.
	 * @param xVector The vector that should be orthogonal to the plane spanned the other two vectors, namely the X vector.
	 * @param yVector The vector that should be orthogonal to the plane spanned the other two vectors, namely the Y vector.
	 * @param zVector The vector that should be orthogonal to the plane spanned the other two vectors, namely the Z vector.
	 */
	public Axis(Point3D origin, Vector xVector, Vector yVector, Vector zVector) {
		_origin = new Point3D(origin);
		_xVector = new Vector(xVector).normalizedVector();
		_yVector = new Vector(yVector).normalizedVector();
		_zVector = new Vector(zVector).normalizedVector();
	}
	
	public Axis(Point3D origin) {
		_origin = new Point3D(origin);
		_xVector = new Vector(1,0,0);
		_yVector = new Vector(0,1,0);
		_zVector = new Vector(0,0,1);
	}
	
	/**
	 * A copy constructor for deep copying an existing axis object.
	 * @param other An existing axis object.
	 */
	public Axis(Axis other) {
		_origin = new Point3D(other._origin);
		_xVector = new Vector(other._xVector);
		_yVector = new Vector(other._yVector);
		_zVector = new Vector(other._zVector);
	}
	
	/**
	 * Getter for the X vector of the axis.
	 * @return A new instance of the vector object.
	 */
	public Vector getXVector() {return new Vector(_xVector);}
	
	/**
	 * Getter for the Y vector of the axis.
	 * @return A new instance of the vector object.
	 */
	public Vector getYVector() {return new Vector(_yVector);}
	
	/**
	 * Getter for the Z vector of the axis.
	 * @return A new instance of the vector object.
	 */
	public Vector getZVector() {return new Vector(_zVector);}
	
	/**
	 * Getter for the origin of the axis.
	 * @return A new instance of the origin object.
	 */
	public Point3D getOrigin() {return new Point3D(_origin);}
	
	/**
	 * Given any point in the 3D space, this function returns its projection on the X vector of the axis.
	 * @param point A point in the 3D space.
	 * @return The projection of the given point on the X vector.
	 */
	public double getXProjection(Point3D point) {return new Vector(point.subtract(_origin)).dotProduct(_xVector);}
	
	/**
	 * Given any point in the 3D space, this function returns its projection on the Y vector of the axis.
	 * @param point A point in the 3D space.
	 * @return The projection of the given point on the Y vector.
	 */
	public double getYProjection(Point3D point) {return new Vector(point.subtract(_origin)).dotProduct(_yVector);}
	
	/**
	 * Given any point in the 3D space, this function returns its projection on the Z vector of the axis.
	 * @param point A point in the 3D space.
	 * @return The projection of the given point on the Z vector.
	 */
	public double getZProjection(Point3D point) {return new Vector(point.subtract(_origin)).dotProduct(_zVector);}
	
	/**
	 * Method to reset the origin of the axis represented by this object, 
	 * which actually moves the axis represented by this object
	 * @param origin The new origin to be assigned to this axis' origin.
	 */
	public void setOrigin(Point3D origin) {_origin = new Point3D(origin);} 
}
