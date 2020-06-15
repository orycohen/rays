
package geometries;

import java.util.ArrayList;
import java.util.Random;

import primitives.Axis;
import primitives.IGetEmission;
import primitives.Material;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import util.calcs;

/**
 * Represents an infinite cylinder in the 3D space.
 * That is, the cylinder does not have a length.

 */
public class Cylinder extends RadialGeometry {

	/**
	 * constructor for a new Cylinder object
	 * @param radius the radius of the cylinder
	 * @param direction the direction of the cylinder
	 * @param emission the emission color of the cylinder
	 * @param material Represents what kind of material this geometry is in terms of diffusion, specularity and shininess.
	 * @throws Exception in case of a negative radius
	 */
	public Cylinder(double radius, Vector direction, Point3D point, Material material, IGetEmission emissionImp) {
		super(radius, material, emissionImp);
		if (direction.vectorLength() == 0) {
			throw new IllegalArgumentException("direction vector cannot be (0,0,0)");
		}
		double xDirection = direction.getHead().getX().getNumber(),
			   yDirection = direction.getHead().getY().getNumber(),
			   zDirection = direction.getHead().getZ().getNumber(),
			   xPoint = point.getX().getNumber(),
			   yPoint = point.getY().getNumber(),
			   zPoint = point.getZ().getNumber();
		
		Random rand = new Random();
		double temp = 0, t1 = rand.nextDouble(), t2 = rand.nextDouble();
		Vector v = null;
		if (xDirection != 0) {
			temp = (xDirection*xPoint - yDirection*(t1 - yPoint) - zDirection*(t2 - zPoint))/xDirection;
			v = new Vector(temp, t1, t2).normalizedVector().scale(radius);
		} else if (yDirection != 0) {
			temp = (yDirection*yPoint - xDirection*(t1 - xPoint) - zDirection*(t2 - zPoint))/yDirection;
			v = new Vector(t1, temp, t2).normalizedVector().scale(radius);
		} else {
			temp = (zDirection*zPoint - xDirection*(t1 - xPoint) - yDirection*(t2 - yPoint))/zDirection;
			v = new Vector(t1, t1, temp).normalizedVector().scale(radius);
		}
		//We need to normalize the vector for later calculations to be correct.
		_axis = new Axis(point, v, direction.crossProduct(v).normalizedVector().scale(radius), direction);
	}
	
	/**
	 * copy constructor for a cylinder object to be deep copied
	 * @param other the object being copied
	 */
	public Cylinder(Cylinder other) {
		super(other);
	}
	
	/**
	 * checks if 'this' object is equal to another one
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Cylinder))
			return false;
		if (this == obj)
			return true;
		Cylinder other = (Cylinder)obj;
		
		//the two vectors needs to be in the same direction, 
		//but not necessary to have the same length.
		return super.equals(other) &&
			   _axis.getZVector().crossProduct(other._axis.getZVector()).equals(new Vector(0,0,0)) &&
			   _axis.getOrigin().equals(other._axis.getOrigin());
		
	}
	
	@Override
	public String toString() {
		return "point: "      + _axis.getOrigin() + 
			   " direction: " + _axis.getZVector() + 
			   ", radius: "   + _radius;
	}
	
	/**
	 * Function to get the direction of the cylinder
	 * @return a new Vector that represents the direction of the cylinder
	 */
	public Vector getDirection() {
		return new Vector(_axis.getZVector());
	}
	
	/**
	 * Function to get a center point in the cylinder
	 * @return a new Point3D that represents the 
	 * center of the cylinder
	 */
	public Point3D getPoint() {
		return new Point3D(_axis.getOrigin());
	}
	
	/**
	 * returns the normal to the cylinder in a given point
	 * This function uses linear algebra calculations.
	 */
	@Override
	public Vector getNormal(Point3D point) {
		//The vector from the point of the cylinder to the given point
		Vector vector1 = new Vector(point.subtract(_axis.getOrigin()));
		
		//We need the projection to multiply the _direction unit vector
		double projection = vector1.dotProduct(_axis.getZVector());
		
		Vector vector2 = _axis.getZVector().scale(projection);
		
		//This vector is orthogonal to the _direction vector.
		Vector check = vector1.subtract(vector2);
		return check.normalizedVector();
	}

	/**
	 * Function for finding intersections points with an infinite 
	 * cylinder.
	 * @param ray The ray that we check if it intersects the cylinder.
	 * @return A list of intersection points, if any.
	 */
	@Override
	public ArrayList<Point3D> findIntersections(Ray ray) {
		ArrayList<Point3D> toReturn = new ArrayList<Point3D>();
		
		Point3D P = ray.getPoint();
		
		Vector V = ray.getDirection(),
			   Va = _axis.getZVector(),
			   DeltaP = new Vector(P.subtract(_axis.getOrigin())),
			   temp_for_use1, temp_for_use2;
		
		double V_dot_Va = V.dotProduct(Va),
			   DeltaP_dot_Va = DeltaP.dotProduct(Va);
		
		temp_for_use1 = V.subtract(Va.scale(V_dot_Va));
		temp_for_use2 = DeltaP.subtract(Va.scale(DeltaP_dot_Va));
		
		double A = temp_for_use1.dotProduct(temp_for_use1);
		double B = 2*V.subtract(Va.scale(V_dot_Va)).dotProduct(DeltaP.subtract(Va.scale(DeltaP_dot_Va)));
		double C = temp_for_use2.dotProduct(temp_for_use2) - _radius * _radius;
		double desc = calcs.subtract(B*B, 4*A*C);
		
		if (desc < 0) {//No solution
			return toReturn;
		}
		
		double t1 = (-B+Math.sqrt(desc))/(2*A),
			   t2 = (-B-Math.sqrt(desc))/(2*A);
		
		if (desc == 0) {//One solution
			if (-B/(2*A) < 0)
				return toReturn;
			toReturn.add(new Vector(P.add(V.scale(-B/(2*A)).getHead())).getHead());
			return toReturn;
		}
		else if (t1 < 0 && t2 < 0){
			return toReturn;
		}
		else if (t1 < 0 && t2 > 0) {
			toReturn.add(new Vector(P.add(V.scale(t2).getHead())).getHead());
			return toReturn;
		}
		else if (t1 > 0 && t2 < 0) {
			toReturn.add(new Vector(P.add(V.scale(t1).getHead())).getHead());
			return toReturn;
		}
		else {
			toReturn.add(new Vector(P.add(V.scale(t1).getHead())).getHead());
			toReturn.add(new Vector(P.add(V.scale(t2).getHead())).getHead());
			return toReturn;
		}
	}

	/**
	 * There is no box that could contain an infinite cylinder.  
	 */
	@Override
	protected BBox createBoundingBox() {
		return null;
	}
}
