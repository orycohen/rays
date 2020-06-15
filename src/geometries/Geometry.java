
package geometries;
import java.util.List;

import primitives.*;

/**
 * Abstract class that represents a geometry in the space.
 * Any geometry shape can extend this abstract class.
 * @author orycohen
 */
public abstract class Geometry {

	/**
	 * Represents what kind of material in terms of diffusion, 
	 * specularity and shininess.
	 */
	protected Material _material;
	
	/**
	 * Represents the box that is blocking this geometry.
	 */
	protected BBox _box;
	/**
	 * The axis that this geometry is aligned to.
	 * It is useful when 
	 * The default is the axis: <br>
	 * <dt>(0,0,0) is origin, <br>
	 * x vector is (1,0,0)<br>
	 * y vector is (0,1,0)<br>
	 * z vector is (0,0,1)<br>
	 */
	protected Axis _axis;
	
	/**
	 * Implementor for the getEmission method;
	 */
	protected IGetEmission _emissionImp;
	
	/**
	 * Constructor for the emission light.
	 * @param emission The emission light that every Geometry has.
	 */
	public Geometry(Material material, IGetEmission emissionImp) {
		_material = new Material(material);
		_axis = new Axis(new Point3D(0,0,0), new Vector(1, 0, 0), new Vector(0, 1, 0), new Vector(0, 0, 1));
		_emissionImp = emissionImp;
	}
	
	/**
	 * Copy constructor for the Geometry objects
	 * @param other the object that is being copied.
	 */
	public Geometry(Geometry other) {
		this._emissionImp = other._emissionImp;
		this._material = new Material(other._material);
		this._axis = new Axis(other._axis);
	}

	/**
	 * getter for the emission of the geometry.
	 * @return a new Color thet represents the emission color 
	 * of the geometry.
	 */
	public Color getEmission(Point3D point) {
		return this._emissionImp.getEmission(point);
	}
	
	public double getIntensity(Point3D point) {
		return this._emissionImp.getIntensity(point);
	}
	
	/**
	 * Getter for the material that represents what kind of material 
	 * is this geometry made of.
	 * @return A new Material that represents what kind of material 
	 * is this geometry made of.
	 */
	public Material getMaterial() {
		return new Material(_material);
	}
	
	/**
	 * Getter for the bounding box in the 3D space that
	 * blocks this geometry.
	 * @return A new bounding box.
	 */
	public BBox getBoundingBox() {
		return new BBox(_box);
	}

	/**
	 * Setting axis that this geometry would be aligned to;
	 * @param origin The origin of the axis
	 * @param xVector The x direction vector.
	 * @param yVector The y direction vector
	 * @param zVector The z direction vector
	 */
	public void setAxis(Point3D origin, Vector xVector, Vector yVector, Vector zVector) {
		_axis = new Axis(origin, xVector, yVector, zVector);
	}

	/**
	 * @param point some point on the geometry shape
	 * @return a normal vector to this geometry 
	 * shape in that 'point'
	 * @throws Exception in case where the point is not on the 
	 * shape
	 */
	public Vector getNormal(Point3D point) { return null; }
	
	/**
	 * @param ray ray that may be intersecting this geometry
	 * @return list of points of the intersections.
	 */
	public abstract List<Point3D> findIntersections(Ray ray);
	
	/**
	 * Creates a minimal-sized bounding box in the 3D space that
	 * blocks this geometry.
	 * @return A new bounding box.
	 */
	protected abstract BBox createBoundingBox();
}
