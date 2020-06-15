
package geometries;

import java.util.ArrayList;

import primitives.*;

/**
 * Represent a triangle in the 3D space 
 * that extends the Geometry abstract class.
 * @author orycohen
 */
public class Triangle extends Plane {

	//These are the three points that represents the triangle.	
	private final Point3D _p1;
	private final Point3D _p2;
	private final Point3D _p3;
	
	/**
	 * Inner field for the intersection method.
	 */
	private final double _area;
	
	/**
	 * constructor for a new triangle object.
	 * @param p1 the first point of the triangle
	 * @param p2 the second point of the triangle
	 * @param p3 the third point of the triangle
	 * @param material Represents what kind of material this geometry is in terms of diffusion, specularity and shininess.
	 * @param emissionImp The IGetEmission implementor
	 */
	public Triangle(Point3D p1, Point3D p2, Point3D p3, Material material, IGetEmission emissionImp){
		super(p1,p2,p3, material, emissionImp);
		_p1 = new Point3D(p1);
		_p2 = new Point3D(p2);
		_p3 = new Point3D(p3);
		_area = new Vector(_p2.subtract(_p1)).
				crossProduct(new Vector(_p3.subtract(_p1))).vectorLength() / 2;
		_box = this.createBoundingBox();
	}
	
	/**
	 * Copy constructor for a deep copy of an Triangle object
	 * @param other the object that is being copied
	 */
	public Triangle(Triangle other) {
		super(other);
		this._p1 = new Point3D(other._p1);
		this._p2 = new Point3D(other._p2);
		this._p3 = new Point3D(other._p3);
		this._area = other._area;
		this._box = new BBox(other._box);
	}

	@Override
	public boolean equals(Object obj) {
		//return false if this object is not a triangle
		if (obj == null || !(obj instanceof Triangle))
			return false;
		if (this == obj)
			return true;
		Triangle other = (Triangle)obj;
		
		return ((_p1.equals(other._p1) &&
				_p2.equals(other._p2) &&
				_p3.equals(other._p3)) || 
				(_p1.equals(other._p2) &&
				_p2.equals(other._p3) &&
				_p3.equals(other._p1)) ||
				(_p1.equals(other._p3) &&
			    _p2.equals(other._p1) &&
				_p3.equals(other._p2)) ||
				(_p1.equals(other._p2) &&
				_p2.equals(other._p1) &&
				_p3.equals(other._p3)) ||
				(_p1.equals(other._p1) &&
				_p2.equals(other._p3) &&
				_p3.equals(other._p2)) || 
				(_p1.equals(other._p3) &&
				_p2.equals(other._p2) &&
				_p3.equals(other._p1)));
	}
	
	@Override
	public String toString() {
		return String.format
				("" + _p1 + ", " + _p2 + ", " + _p3);
	}
	
	/**
	 * Getter for the first vertex of the triangle
	 * @return The first vertex of the triangle
	 */
	public Point3D getP1() {
		return new Point3D(_p1);
	}
	
	/**
	 * Getter for the second vertex of the triangle
	 * @return The second vertex of the triangle
	 */
	public Point3D getP2() {
		return new Point3D(_p2);
	}
	
	/**
	 * Getter for the third vertex of the triangle
	 * @return The third vertex of the triangle
	 */
	public Point3D getP3() {
		return new Point3D(_p3);
	}
	
	/**
	 * For Inner usage, we can check for a given point if the triangle 
	 * that is represented by this object contains it. 
	 * @param point The given point which we check if it is in the triangle.
	 * @return True if the point is in the triangle and false otherwise.
	 */
	private boolean isInTriangle(Point3D point) {
		double alpha = (new Vector(_p2.subtract(point)).
				crossProduct(new Vector(_p3.subtract(point))).vectorLength())/(2*_area);
		double beta = (new Vector(_p3.subtract(point)).
				crossProduct(new Vector(_p1.subtract(point))).vectorLength())/(2*_area);
		double gama = (new Vector(_p2.subtract(point)).
				crossProduct(new Vector(_p1.subtract(point))).vectorLength())/(2*_area);
		if (util.calcs.subtract(alpha + beta + gama , 1) == 0)	
			return true;
		return false;

	}
	
	/**
	 * Returns the normal to this triangle in that given point
	 */
	@Override
	public Vector getNormal(Point3D point) {
		return isInTriangle(point) ? this.getNormal() : null;
	}

	/**
	 * override function that finds intersections of the ray with
	 * this triangle.
	 */
	@Override
	public ArrayList<Point3D> findIntersections(Ray ray) {
		//get the intersections with the plane that 
		//contains the triangle.
		ArrayList<Point3D> points = super.findIntersections(ray);
		points.removeIf(point -> !isInTriangle(point));
		return points;	
	}

	/**
	 * Creation of the minimum box that 
	 * contain this Triangle object.
	 */
	@Override
	protected BBox createBoundingBox() {
		
		double x1 = _p1.getX().getNumber(),
			   x2 = _p2.getX().getNumber(),
			   x3 = _p3.getX().getNumber(),
			   y1 = _p1.getY().getNumber(),
			   y2 = _p2.getY().getNumber(),
			   y3 = _p3.getY().getNumber(),
			   z1 = _p1.getZ().getNumber(),
			   z2 = _p2.getZ().getNumber(),
			   z3 = _p3.getZ().getNumber();
		
		double xmin = Math.min(Math.min(x1, x2), x3) - 0.5,
			   ymin = Math.min(Math.min(y1, y2), y3) - 0.5,
			   zmin = Math.min(Math.min(z1, z2), z3) - 0.5,
			   xmax = Math.max(Math.max(x1, x2), x3) + 0.5,
			   ymax = Math.max(Math.max(y1, y2), y3) + 0.5,
			   zmax = Math.max(Math.max(z1, z2), z3) + 0.5; 
		
		return new BBox(xmin, xmax, ymin, ymax, zmin, zmax);
	}
}
