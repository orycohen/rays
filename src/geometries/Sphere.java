
package geometries;

import java.util.ArrayList;

import primitives.IGetEmission;
import primitives.Material;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import util.calcs;

/**
 * Represent a sphere in the space that extends 
 * the RadialGeometry abstract class.
 * @author orycohen*/
public class Sphere extends RadialGeometry {

	/**
	 * constructor for a new sphere object.
	 * @param radius the radius of the sphere
	 * @param center the center point of the sphere
	 * @param material Represents what kind of material this geometry is
	 * in terms of diffusion, specularity and shininess.
	 * @param emissionImp The IGetEmission implementor
	 * @throws Exception in case of negative radius
	 */
	public Sphere(double radius, Point3D center, Material material, IGetEmission emissionImp){
		super(radius, material, emissionImp);
		_axis.setOrigin(center);
		_box = this.createBoundingBox();
	}
	
	/**
	 * Copy constructor for a deep copy of an Sphere object.
	 * @param other the object that being copied
	 */
	public Sphere(Sphere other) {
		super(other);
		this._box = new BBox(other._box);
	}
	
	@Override
	public boolean equals(Object obj) {
		//false if this isn't a Sphere
		if (obj == null || !(obj instanceof Sphere))
			return false;
		Sphere other = (Sphere)obj;
		
		return other == this || 
				(super.equals(other) && 
				_axis.getOrigin().equals(other._axis.getOrigin()));
	}
	
	@Override
	public String toString() {
		return String.format
				("point: " + _axis.getOrigin() + ", radius: " + _radius);
	}
	
	/**
	 * getter for the center property
	 * @return the center of the sphere
	 */
	public Point3D getCenter() {
		return new Point3D(_axis.getOrigin());
	}
	

	/**
	 * get the normal to this sphere in a given point
	 */
	@Override
	public Vector getNormal(Point3D point) {
		Vector orthogonal = new Vector(point.subtract(_axis.getOrigin()));		
		return orthogonal.normalizedVector();
	}

	/**
	 * Override function that finds intersections of the ray with
	 * this sphere.
	 */
	@Override
	public ArrayList<Point3D> findIntersections(Ray ray) {
		
		//the vector from the point of the camera to the center of the sphere
		Vector L = new Vector(_axis.getOrigin().subtract(ray.getPoint()));
		
		//the length from the point of the camera to the
		//(first intersection point with the sphere) + th
		double tm = L.dotProduct(ray.getDirection());
		
		//the length of the L vector from the point of the camera to 
		//the center of this sphere
		double Llen = L.vectorLength();
		
		//the shortest length from the center of this sphere to the vector
		//of the ray
		double d = Math.sqrt(calcs.subtract(Llen * Llen, tm * tm));
		
		//if d > radius there is no intersections, and return an empty list
		//we use square here so there would not be case that th is NaN.
		//If we check here (calcs.subtract(d , _radius) > 0)
		//still, with the approximations, we could get that 
		//(_radius*_radius - d*d) < 0 and th would be NaN and cause an error.
		if (calcs.subtract(d * d, _radius * _radius) > 0) {
			return new ArrayList<>();
		}
		
		//the th length represent 
		//(the length from one intersection point to the second)/2
		double th = Math.sqrt(calcs.add(_radius*_radius, -(d*d)));
		
		ArrayList<Point3D> toReturn = new ArrayList<Point3D>();
				
		//if there is one intersection with this sphere
		if (calcs.closeToZero(d - _radius)) {
			if (tm < 0)
				return new ArrayList<Point3D>();
			Point3D temp = ray.getPoint().add(ray.getDirection().scale(tm).getHead());
			toReturn.add(temp);
			return toReturn;
		}
		//if there is two intersections with this sphere
		else if(calcs.subtract(Llen,_radius) < 0) {
			Point3D temp = ray.getPoint().add(ray.getDirection().scale(tm + th).getHead());
			toReturn.add(temp);
			return toReturn;
		}
		
		//That means that the sphere is behind the camera.
		if (tm < 0) {
			return new ArrayList<Point3D>();
		}
		Point3D p1 = ray.getPoint().add(ray.getDirection().scale(tm - th).getHead()),
				p2 = ray.getPoint().add(ray.getDirection().scale(tm + th).getHead());

		toReturn.add(p1);
		toReturn.add(p2);
		return toReturn;
	}

	/**
	 * Creation of the minimum box that 
	 * contain this Sphere object.
	 */
	@Override
	protected BBox createBoundingBox() {
		
		double temp = _axis.getOrigin().getX().getNumber();
		double xmin = temp - _radius - 0.5,
			   xmax = temp + _radius + 0.5;
		
		temp = _axis.getOrigin().getY().getNumber();
		double ymin = temp - _radius - 0.5,
			   ymax = temp + _radius + 0.5;
		
		temp = _axis.getOrigin().getZ().getNumber();
		double zmin = temp - _radius - 0.5,
			   zmax = temp + _radius + 0.5;
		
		return new BBox(xmin, xmax, ymin, ymax, zmin, zmax);
	}
}
