package geometries;

import java.util.ArrayList;

import primitives.IGetEmission;
import primitives.Material;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
 * Can be used to represent imported images on rectangles, 
 * or rectangles with no images on them, using textures techniques.
 */
public class Rectangle extends Plane {

	/**
	 * The width of the rectangle in the 3D space.
	 */
	private double _width;
	/**
	 * The height of the rectangle in the 3D space.
	 */
	private double _height;
	
	/**
	 * Constructs a new rectangle.
	 * @param origin The bottom left point of the rectangle, where it starts in the 3D space.
	 * @param endX The bottom right point of the rectangle in the 3D space.
	 * @param endY The upper left point of the rectangle in the 3D space.
	 * @param imageDirectory The directory of the image to be represented on the rectangle.
	 * @param material The material component of the rectangle.
	 * @param emissionImp The IGetEmission implementor
	 */
	public Rectangle(Point3D origin, Point3D endX, Point3D endY, Material material, IGetEmission emissionImp) {
		super(origin, endX, endY, material, emissionImp);
		_width = origin.distance(endX);
		_height = origin.distance(endY);
		_box = createBoundingBox();
	}

	/**
	 * A copy constructor
	 * @param other The object being copied
	 */
	public Rectangle(Rectangle other) {
		super(other);
		this._width = other._width;
		this._height = other._height;
		this._box = new BBox(other._box);
	}
	
	/**
	 * Assuming that the point is on the plane that contains this rectangle, 
	 * this function checks whether it is inside the rectangle or not.
	 * @param point The point that is to be checked.
	 * @return Whether the given point is inside the rectangle or not. <br>
	 * Returns true if it is inside this rectangle and false otherwise.
	 */
	private boolean isInside(Point3D point) {
		Vector temp = new Vector(point.subtract(_axis.getOrigin()));
		double x = temp.dotProduct(_axis.getXVector()), y = temp.dotProduct(_axis.getYVector());
		return x <= _width && y <= _height && x >= 0 && y >= 0;
		
	}
	
	/**
	 * Checks for intersection of the given ray with this rectangle.
	 */
	@Override
	public ArrayList<Point3D> findIntersections(Ray ray) {
		ArrayList<Point3D> intersections = super.findIntersections(ray);
		if (intersections.size() != 0 && isInside(intersections.get(0))) {
			return intersections;
		}
		return new ArrayList<Point3D>();
	}

	/**
	 * Creates the minimal box that contains this rectangle. <br>
	 * see {@link geometries.BBox}.
	 */
	@Override
	protected BBox createBoundingBox() {
		Vector  widthVec = _axis.getXVector().scale(_width),
				heightVec = _axis.getYVector().scale(_height);
		Point3D p2 = _axis.getOrigin().add(widthVec.getHead()),
				p3 = _axis.getOrigin().add(heightVec.getHead()),
				p4 = _axis.getOrigin().add(widthVec.add(heightVec).getHead());
		
		double  x1 = _axis.getOrigin().getX().getNumber(),
				y1 = _axis.getOrigin().getY().getNumber(),
				z1 = _axis.getOrigin().getZ().getNumber(),
				x2 = p2.getX().getNumber(),
				y2 = p2.getY().getNumber(),
				z2 = p2.getZ().getNumber(),
				x3 = p3.getX().getNumber(),
				y3 = p3.getY().getNumber(),
				z3 = p3.getZ().getNumber(),
				x4 = p4.getX().getNumber(),
				y4 = p4.getY().getNumber(),
				z4 = p4.getZ().getNumber();
		
		double  minX = Math.min(Math.min(Math.min(x1, x2), x3), x4) - 0.5, 
			    minY = Math.min(Math.min(Math.min(y1, y2), y3), y4) - 0.5,
			    minZ = Math.min(Math.min(Math.min(z1, z2), z3), z4) - 0.5,
			    maxX = Math.max(Math.max(Math.max(x1, x2), x3), x4) + 0.5, 
			    maxY = Math.max(Math.max(Math.max(y1, y2), y3), y4) + 0.5, 
			    maxZ = Math.max(Math.max(Math.max(z1, z2), z3), z4) + 0.5;
		
		return new BBox(minX, maxX, minY, maxY, minZ, maxZ);
	}
}
