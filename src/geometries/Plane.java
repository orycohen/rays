
package geometries;

import java.util.ArrayList;

import primitives.*;
import util.calcs;

/**
 * Represent a plane in the 3D space that 
 * extends the Geometry abstract class.
 */
public class Plane extends Geometry {
	
	/**
	 * Constructor for a new Plane object that is being created.
	 * The constructor gets 3 point in the space and builds a plane with them.
	 * As for the axis that is aligned to this plane, <br>
	 * p1 is treated as the origin.
	 * Then it first constructs the z vector as the cross product (p2-p1)X(p3-p1),<br>
	 * the x vector is constructed from (p2-p1) and the y vector is constructed 
	 * from (zVector)X(xVector), where zVector and xVector are the new vectors that were just 
	 * constructed.
	 * @param p1 the first point
	 * @param p2 the second point
	 * @param p3 the third point
	 * @param material Represents what kind of material this geometry is
	 * @param emissionImp The IGetEmission implementor
	 * in terms of diffusion, specularity and shininess.
	 */
	public Plane(Point3D p1, Point3D p2, Point3D p3, Material material, IGetEmission emissionImp) {
		super(material, emissionImp);
		if (p1.equals(p2) || p1.equals(p3) || p2.equals(p3))
			throw new IllegalArgumentException("three points must be different");
		Vector vector1 = new Vector(p2.subtract(p1)), vector2 = new Vector(p3.subtract(p1));
		Vector normal = vector1.crossProduct(vector2);
		
		//if the three given points are in the same 
		//line in the space, throw exception.
		if (normal.equals(new Vector(0,0,0)))
			throw new IllegalArgumentException("cannot build a plane with these three points");
		_axis = new Axis(p1, vector1.normalizedVector(), normal.crossProduct(vector1).normalizedVector(), normal.normalizedVector());
	}
	
	/**
	 * Constructor for a new Plane object that is being created.
	 * The constructor gets point in the plane and normal to 
	 * this plane and builds the plane with them. 
	 * <dt> NOTE: when constructing a plane via this constructor, the axis would be: <br>
	 * {@code point} as origin and {@code normal} as xVector, yVector and zVector!</dt>
	 * @param point some point in the plane
	 * @param normal a normal vector to the plane
	 * @param material Represents what kind of material this geometry is
	 * @param emissionImp The IGetEmission implementor
	 * in terms of diffusion, specularity and shininess.
	 * @throws Exception in case of vector with '(0,0,0)' head
	 */
	public Plane(Point3D point, Vector normal, Material material, IGetEmission emissionImp) {
		super(material, emissionImp);
		if (new Vector(0,0,0).equals(normal))
			throw new IllegalArgumentException
			("the normal vector's head cannot be (0,0,0)");
		_axis = new Axis(point, normal, normal, normal);
	}
	
	/**
	 * Copy constructor for a deep copy of a Plane object
	 * @param other the object that being copied
	 */
	public Plane(Plane other) { super(other); }
	
	/**
	 * checks if this object is equal to another one
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Plane))
			return false;
		Plane other = (Plane)obj;
		if (other == this)
			return true;
		Vector temp = this._axis.getZVector().crossProduct(other._axis.getZVector());
		
		//return false if the normals are not parallel
		if (!(new Vector(0,0,0).equals(temp)))
			return false;
		
		Point3D pointOnOther = new Point3D(other._axis.getOrigin());
				
		//return true if now
		//the two planes have the same point 
		if (this._axis.getOrigin().equals(pointOnOther))
			return true;
		
		//get the vector from the point of 'this' plane to the point
		//on the other plane
		temp = new Vector
				(pointOnOther.getX().subtract(_axis.getOrigin().getX()).getNumber(),
				 pointOnOther.getY().subtract(_axis.getOrigin().getY()).getNumber(),
				 pointOnOther.getZ().subtract(_axis.getOrigin().getZ()).getNumber());
		
		double dotResult = temp.dotProduct(_axis.getZVector());
		
		if (dotResult == 0)//the vectors are orthogonal
			return true;
		else //the vectors are not orthogonal
			return false;
	}
	
	@Override
	public String toString() {
		return "normal: " + _axis.getZVector() + ", point: " + _axis.getOrigin();
	}
	
	/**
	 * Getter for the point that is on the plane
	 * @return A new Point3D that is on the plane.
	 */
	public Point3D getPoint() {
		return new Point3D(_axis.getOrigin());
	}
	
	/**
	 * Getter for the normal to this plane.
	 * @return a new Vector that is normal to this plane.
	 */
	public Vector getNormal() {
		return new Vector(_axis.getZVector());
	}

	/**
	 * function that gets a point on the plane 
	 * and returns the normal to the plane 
	 * in that point
	 */
	@Override
	public Vector getNormal(Point3D point) {
		Vector vector = new Vector(this._axis.getOrigin().subtract(point));
		
		if (calcs.closeToZero(vector.dotProduct(_axis.getZVector())))
			return new Vector(_axis.getZVector());
		return null;
	}

	/**
	 * Function that checks for intersections of a ray 
	 * with this plane.
	 */
	@Override
	public ArrayList<Point3D> findIntersections(Ray ray) {
		//if the ray is orthogonal to the normal of this plane so
		//there is no intersections with the plane 
		if (calcs.closeToZero(ray.getDirection().dotProduct(_axis.getZVector())))
			return new ArrayList<Point3D>();//return an empty list of points.
		
		//the scalar that gives us the point with the formula:
		//Point = P0 + scalar*V, where V and P0 are from the given ray
		
		double x = _axis.getZVector().dotProduct(new Vector(_axis.getOrigin().subtract(ray.getPoint()))),
			   y = _axis.getZVector().dotProduct(ray.getDirection());
		double scalar = x/y;
		
		//that means that the plane is may be behind the camera
		if (scalar < 0 || calcs.closeToZero(scalar))
			return new ArrayList<Point3D>();
		
		//now we have one intersection for sure.
		Point3D point = 
				//here we use the formula: toReturn = P0 + scalar*V.
				new Point3D(ray.getPoint().add(ray.getDirection().scale(scalar).getHead()));
		ArrayList<Point3D> toReturn = new ArrayList<>();
		toReturn.add(point);
		return toReturn;
		
	}

	
	/**
	 * No implementation for this class.
	 * There is no box that could contain an infinite plane.  
	 */
	@Override
	protected BBox createBoundingBox() {
		return null;
	}
}
