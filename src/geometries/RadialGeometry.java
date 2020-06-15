
package geometries;

import primitives.*;

/**
 * Represent a shape in the space that has a radius - radial geometry.
 * This class extends the Geometry abstract class.
 * Any geometry that has a radius can extend this class.
 * @author orycohen*/
public abstract class RadialGeometry extends Geometry {

	/**
	 * the radius of the shape
	 */
	protected final double _radius;
	
	/**
	 * constructor for radial geometry objects.
	 * @param radius the radius of the shape.
	 * @param material Represents what kind of material this geometry is
	 * @param emissionImp The IGetEmission implementor
	 * in terms of diffusion, specularity and shininess.
	 */
	public RadialGeometry(double radius, Material material, IGetEmission emissionImp) {
		super(material, emissionImp);
		if (radius <= 0)
			throw new IllegalArgumentException("radius cannot be negative!");
		_radius = radius;
	}
	
	/**
	 * Copy constructor for deep copy of a RadialGeometry object 
	 * @param other the object that being copied
	 */
	public RadialGeometry(RadialGeometry other) {
		super(other); //Copy the emission color of the geometry.
		this._radius = other._radius;
	}
	
	/**
	 * Getter for the radius of this radial geometry shape.
	 * @return the radius of this radial geometry shape.
	 */
	public double getRadius() {
		return _radius;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof RadialGeometry))
			return false;
		RadialGeometry other = (RadialGeometry)obj;
		return other == this || 
				this._radius == other._radius;
	}
	
	@Override
	public String toString() {
		return "" + _radius;
	}
	
	/**
	 * A function that gets a point in the space and returns 
	 * a normal to this shape in the given point.
	 * @param point some point on the geometry shape
	 * @return a normal vector to this geometry shape 
	 * in that 'point'
	 * */
	public abstract Vector getNormal(Point3D point);

}
