package elements;

import java.util.ArrayList;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * Class that represents a point of light 
 * in the 3D space that sends light to all the directions.
 * This point light is like a bulb.
 */
public class PointLight extends VolumedLightSrc {

	/**
	 * The position of the point light in the 3D space
	 */
	protected Point3D _position;
	
	/**
	 * The light's radius
	 */
	protected double _radius;
	
	/**
	 * This parameter is one of the factors of the light source 
	 * intensity in any given point in the 3D space.
	 */
	protected double _Kc, _Kl, _Kq;
	
	
	
	/**
	 * Index for the iteration over the '_spiralSamples' array.
	 */
	protected int _iterationIndex;
	
	/**
	 * A constructor for this Point Light object for building
	 * a new instance of the PointLight class.
	 * @param color The color that represents the intensity of this object.
	 * @param position The position of the point light in the 3D space
	 * @param radius The light's radius
	 * @param Kc
	 * @param Kl
	 * @param Kq
	 */
	public PointLight(Color color, Point3D position, double radius, double Kc, double Kl, double Kq) {
		super(color);
		_position = new Point3D(position);
		_radius = radius;
		_Kc = Kc;
		_Kl = Kl;
		_Kq = Kq;
		
		setSamples(NUMBER_OF_SAMPLES);
	}

	/**
	 * Getter for the parameter is one of the factors of the light source 
	 * intensity in any given point in the 3D space.
	 * @return the Kc factor of the intensity of this light source.
	 */
	public double getKc() { return _Kc; }
	
	/**
	 * Getter for the parameter is one of the factors of the light source 
	 * intensity in any given point in the 3D space.
	 * @return the Kl factor of the intensity of this light source.
	 */
	public double getKl() { return _Kl; }
	
	/**
	 * Getter for the parameter is one of the factors of the light source 
	 * intensity in any given point in the 3D space.
	 * @return the Kq factor of the intensity of this light source.
	 */
	public double getKq() { return _Kq; }

	
	/**
	 * Setter for the parameter is one of the factors of the light source 
	 * intensity in any given point in the 3D space.
	 * @param Kc represents the Kc factor of the intensity of this light source.
	 */
	public void setKc(double Kc) { _Kc = Kc; }
	
	/**
	 * Setter for the parameter is one of the factors of the light source 
	 * intensity in any given point in the 3D space.
	 * @param Kl represents the Kl factor of the intensity of this light source.
	 */
	public void setKl(double Kl) { _Kl = Kl; }
	
	/**
	 * Setter for the parameter is one of the factors of the light source 
	 * intensity in any given point in the 3D space.
	 * @param Kq represents the Kq factor of the intensity of this light source.
	 */
	public void setKq(double Kq) { _Kq = Kq;  }

	/**
	 * Getter for the position of the light source that 
	 * is represented by this PointLight object
	 * in the 3D space.
	 * @return A new Point3D that represents the position of the light source in the 3D space.
	 */
	public Point3D getPosition() {
		return new Point3D(_position); 
	}
	
	/**
	 * Getter for the radius of the light source
	 * @return The light's radius
	 */
	@Override
	public double getRadius() {
		return _radius; 
	}
	
	/**
	 * This override function is for getting the intensity of the 
	 * light that is represented by this PointLight object 
	 * in the 3D space.
	 */
	@Override
	public Color getIntensity(Point3D point) {
		double distance = this.getL(point).vectorLength();
		double factor = _Kc + _Kl * distance + _Kq * distance * distance;
		return _color.scale(1/factor);
	}
	
	/**
	 * This override function is for getting the vector from
	 * the point-light source of light to the given point 
	 * in the 3D space.
	 */
	@Override
	public Vector getL(Point3D point) {
		return new Vector(point.subtract(_position));
	}
	
	/**
	 * Function for overwriting the initiated-one-entry array with 
	 * vary number of samples.
	 * @param numberOfSamples The number of samples that will be checked 
	 * for every ray that is sent to this light source.
	 */
	@Override
	public void setSamples(int numberOfSamples) {
		double angleDelta = (10 * Math.PI) / numberOfSamples;
		
		Scalars = new ArrayList<double[]>();
		
		for (int i = 0; i < numberOfSamples; i++) {
			Scalars.add(new double[] { Math.sin(angleDelta * i), Math.cos(angleDelta * i) });
		}

	}
	/**
	 * Getter for the number of samples for each ray 
	 * that is sent to the light source.
	 */
	@Override
	public int getNumOfSamples() {
		return Scalars.size();
	}
}
