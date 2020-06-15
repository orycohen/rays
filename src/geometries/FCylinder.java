package geometries;

import java.util.ArrayList;

import primitives.IGetEmission;
import primitives.Material;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import util.calcs;

/**
 * This class represents a finite cylinder.
 * The cylinder has a length value that is represented by 
 * (p2 - p1) vector.
 * This vector is one of the fields in this class.  
 */
public class FCylinder extends Cylinder{
	
	/**
	 * Helper enum for information 
	 * about a point in the cylinder: if it intersects the cylinder,
	 * the planes of the cylinder, or none of them.
	 */
	static private enum _where {
		NoIntersection,
		OnCylinder,
		OnUpperPlane,
		OnBottomPlane
	}
		
	/**
	 * The point on the z aligned axis where the cylinder ends.
	 */
	private Point3D _endPoint;

	/**
	 * Constructor for the finite cylinder
	 * @param radius The radius of the cylinder
	 * @param direction The direction vector from p1 to p2
	 * @param p1 Starting point of the direction vector
	 * @param p2 The point in which the cylinder ends.
	 * @param material Represents what kind of material this geometry is in terms of diffusion, specularity and shininess.
	 * @param emissionImp The IGetEmission implementor
	 */
	public FCylinder(double radius, Point3D p1, Point3D p2, Material material, IGetEmission emissionImp) {
		super(radius, new Vector(p2.subtract(p1)), p1, material, emissionImp);
		if (p1.distance(p2) == 0)
			throw new IllegalArgumentException("direction vector cannot be (0,0,0)");
		_endPoint = new Point3D(p2);
		_box = this.createBoundingBox();
	}

	/**
	 * A copy constructor
	 * @param other The object being copied
	 */
	public FCylinder(FCylinder other) {
		super(other);
		_endPoint = new Point3D(other._endPoint);
		this._box = new BBox(other._box);
	}
	
	/**
	 * Getter for the direction of the cylinder.
	 * @return e new Vector that represents 
	 * the direction of the cylinder.
	 */
	public Vector getDirection() { 
		return new Vector(_axis.getZVector());
	}
	
	/**
	 * Getter for the starting point of the cylinder.
	 * @return e new Point3D that represents 
	 * the starting point of the cylinder.
	 */
	public Point3D getP1() {
		return new Point3D(_axis.getOrigin());
	}
	
	/**
	 * Getter for the ending point of the cylinder.
	 * @return e new Point3D that represents 
	 * the ending point of the cylinder.
	 */
	public Point3D getP2() {
		return new Point3D(_endPoint);
	}

	/**
	 * Inner function to check if a given point is on this cylinder.
	 * @param point The point that the function checks
	 * @return True if the point on this cylinder and false otherwise.
	 */
	private _where _pointOn(Point3D point) {
		//The vector from p1 (start point) to the given point.
		Vector vector1 = new Vector(point.subtract(_axis.getOrigin()));
		
		//The vector from p2 (end point) to the given point.
		Vector vector2 = new Vector(point.subtract(_endPoint));
		
		//Checks if the point is on one of the edges of the cylinder
		double v1_dot_dir = vector1.dotProduct(_axis.getZVector()),
			   v2_dot_dir = vector2.dotProduct(_axis.getZVector());
		
		if (calcs.closeToZero(v1_dot_dir)) {
			if (calcs.subtract(_radius, vector1.vectorLength()) >= 0)
				return _where.OnBottomPlane;
		}
		
		if (calcs.closeToZero(v2_dot_dir)) {
			if (calcs.subtract(_radius, vector2.vectorLength()) >= 0)
				return _where.OnUpperPlane;
		}
		
		if (v1_dot_dir > 0 && v2_dot_dir < 0) {
			return _where.OnCylinder;
		}
		return _where.NoIntersection;	
	}

	/**
	 * Function to get the normal to this cylinder in 
	 * a given point that is on the cylinder
	 */
	@Override
	public Vector getNormal(Point3D point) {
		switch (_pointOn(point)) {
		case OnUpperPlane:
			return this.getDirection();
		case OnBottomPlane:
			return this.getDirection().scale(-1);
		case OnCylinder:
			return super.getNormal(point);
		default:
			return null;
		}
	}
	
	/**
	 * Function that finds intersections with this final cylinder.
	 * Note that unlike the Cylinder, here there are point that is not on the
	 * cylinder whereas they on the Cylinder
	 */
	@Override
	public ArrayList<Point3D> findIntersections(Ray ray) {

		ArrayList<Point3D> temp = super.findIntersections(ray);
		ArrayList<Point3D> toReturn = new ArrayList<Point3D>();
		
		Plane planeP1 = new Plane(_axis.getOrigin(), _axis.getZVector(), new Material(0, 0, 0, 0, 0, 0, 0), this._emissionImp);
		Plane planeP2 = new Plane(_endPoint, _axis.getZVector(), new Material(0, 0, 0, 0, 0, 0, 0), this._emissionImp);
		
		ArrayList<Point3D> temp1 = planeP1.findIntersections(ray);
		ArrayList<Point3D> temp2 = planeP2.findIntersections(ray);
		
		for (Point3D point1 : temp1) {
			if (point1.distance(_axis.getOrigin()) <= _radius)
				toReturn.add(point1);
		}
		
		for (Point3D point2 : temp2) {
			if (point2.distance(_endPoint) <= _radius)
				toReturn.add(point2);
		}

		
		for (Point3D p : temp) {
			if (_pointOn(p) != _where.NoIntersection)
				toReturn.add(p);
		}
		return toReturn;
	}
	
	/**
	 * Creation of the minimum box that 
	 * contain this Finite Cylinder object.
	 */
	@Override
	protected BBox createBoundingBox() {
		double x1 = _axis.getOrigin().getX().getNumber(),
			   y1 = _axis.getOrigin().getY().getNumber(),
			   z1 = _axis.getOrigin().getZ().getNumber(),
			   x2 = _endPoint.getX().getNumber(),
			   y2 = _endPoint.getY().getNumber(),
			   z2 = _endPoint.getZ().getNumber();
		
		double dis = _axis.getOrigin().distance(_endPoint);
		
		double Zmove = _radius*Math.sqrt(1-((z1-z2)/dis)*((z1-z2)/dis)),
			   Xmove = _radius*Math.sqrt(1-((x1-x2)/dis)*((x1-x2)/dis)),
			   Ymove = _radius*Math.sqrt(1-((y1-y2)/dis)*((y1-y2)/dis));
		
		//The addition of half unit is for avoiding errors.
		double Xmax = Math.max(x1, x2) + Xmove + 0.5,
			   Xmin = Math.min(x1, x2) - Xmove - 0.5,
			   Ymax = Math.max(y1, y2) + Ymove + 0.5,
			   Ymin = Math.min(y1, y2) - Ymove - 0.5,
			   Zmax = Math.max(z1, z2) + Zmove + 0.5,
			   Zmin = Math.min(z1, z2) - Zmove - 0.5;

		return new BBox(Xmin, Xmax, Ymin, Ymax, Zmin, Zmax);
	}
}