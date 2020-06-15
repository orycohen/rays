package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
 * Represents a box in the 3D space.
 * The panels of the box are parallel to the axis.
 */
public class BBox {
	
	/**
	 * The minimum x of this box
	 */
	private double Xmin;
	
	/**
	 * The maximum x of this box
	 */
	private double Xmax;

	/**
	 * The minimum y of this box
	 */
	private double Ymin;
	
	/**
	 * The maximum y of this box
	 */
	private double Ymax;

	/**
	 * The minimum z of this box
	 */
	private double Zmin;
	
	/**
	 * The maximum z of this box
	 */
	private double Zmax;
	
	/**
	 * The center point of the box
	 */
	private Point3D _center;

	/**
	 * Constructs the bounding box.
	 * @param xmin The minimal value of x that can be in point that is inside the box
	 * @param xmaxThe minimal value of x that can be in point that is inside the box
	 * @param ymin The minimal value of y that can be in point that is inside the box
	 * @param ymax The maximal value of y that can be in point that is inside the box
	 * @param zmin The minimal value of z that can be in point that is inside the box
	 * @param zmax The maximal value of z that can be in point that is inside the box
	 */
	public BBox
	(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
		Xmin = xmin;
		Xmax = xmax;
		Ymin = ymin;
		Ymax = ymax;
		Zmin = zmin;
		Zmax = zmax;
		_center = new Point3D((Xmin + Xmax)/2, (Ymin + Ymax)/2, (Zmin + Zmax)/2);
	}
	
	/**
	 * Copy constructor for creating a new bounding box.
	 * @param other The object that is being copied.
	 */
	public BBox(BBox other) {
		Xmin = other.Xmin;
		Xmax = other.Xmax;
		Ymin = other.Ymin;
		Ymax = other.Ymax;
		Zmin = other.Zmin;
		Zmax = other.Zmax;
		_center = new Point3D(other._center);
	}

	/**
	 * Getter for the minimal value of x that can be in point 
	 * that is inside the box
	 * @return The minimal value of x that can be in point 
	 * that is inside the box
	 */
	public double getMinX() {
		return Xmin;
	}
	
	/**
	 * Getter for the maximal value of x that can be in point 
	 * that is inside the box
	 * @return The maximal value of x that can be in point that is inside the box
	 */
	public double getMaxX() {
		return Xmax;
	}
	
	/**
	 * Getter for the minimal value of y that can be in point 
	 * that is inside the box
	 * @return The minimal value of y that can be in point that is inside the box
	 */
	public double getMinY() {
		return Ymin;
	}
	
	/**
	 * Getter for the maximal value of y that can be in point 
	 * that is inside the box
	 * @return The maximal value of y that can be in point that is inside the box
	 */
	public double getMaxY() {
		return Ymax;
	}
	
	/**
	 * Getter for the minimal value of z that can be in point 
	 * that is inside the box
	 * @return The minimal value of z that can be in point that is inside the box
	 */
	public double getMinZ() {
		return Zmin;
	}
	
	/**
	 * Getter for the maximal value of z that can be in point 
	 * that is inside the box
	 * @return The maximal value of z that can be in point that is inside the box
	 */
	public double getMaxZ() {
		return Zmax;
	}
	
	/**
	 * Getter for the center of the box
	 * @return A new point in the space that represents the point which is the center of the box
	 */
	public Point3D getCenter() {
		return new Point3D(_center);
	}

//******************************Operations**************************//
	/**
	 * Checks if a given ray intersects this box
	 * @param ray The ray which with we search for intersection
	 * @return True if the given ray intersects this box and false
	 * otherwise.
	 */
	public boolean isIntersect(Ray ray) {
		Vector dir = ray.getDirection();
		Point3D temp = dir.getHead();
		
		double dirX = temp.getX().getNumber(),
			   dirY = temp.getY().getNumber(),
			   dirZ = temp.getZ().getNumber();
		
		temp = ray.getPoint();
		
		double pX = temp.getX().getNumber(),
			   pY = temp.getY().getNumber(),
			   pZ = temp.getZ().getNumber();
		
		//If the x, y or z direction is zero - move it a bit 
		//so we would not get an error.
		//After we move the direction from 0 to 0.0000001
		//1/0.0000001 will give us 10000000.
		dirX = dirX == 0 ? 1000000 : 1/dirX;
		dirY = dirY == 0 ? 1000000 : 1/dirY;
		dirZ = dirZ == 0 ? 1000000 : 1/dirZ;
		
		double t1 = (Xmin - pX)*dirX,
			   t2 = (Xmax - pX)*dirX,
			   t3 = (Ymin - pY)*dirY,
			   t4 = (Ymax - pY)*dirY,
			   t5 = (Zmin - pZ)*dirZ,
			   t6 = (Zmax - pZ)*dirZ;
		
		double tmin = Math.max(Math.max(Math.min(t1, t2), Math.min(t3, t4)), Math.min(t5, t6));
		double tmax = Math.min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));
		
		if (tmax <= 0) 
			return false;
		if (tmin >= tmax) 
			return false;
		return true;	
	}

	/**
	 * Creating a union of the box that is represented by this object 
	 * with the given BBox object. The union BBox is the minimum box that contains 
	 * both boxes.
	 * @param box The given box for the union result.
	 * @return A new BBox object that is the union of the two boxes
	 */
	public BBox union(BBox box) {
		double xmin = Math.min(this.Xmin, box.Xmin),
			   xmax = Math.max(this.Xmax, box.Xmax),
			   ymin = Math.min(this.Ymin, box.Ymin),
			   ymax = Math.max(this.Ymax, box.Ymax),
			   zmin = Math.min(this.Zmin, box.Zmin),
			   zmax = Math.max(this.Zmax, box.Zmax);
		
		return new BBox(xmin, xmax, ymin, ymax, zmin, zmax);
	}
}
