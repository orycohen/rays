package primitives;

/**
 * Part of the bridge design pattern.
 * @author oricohen
 */
public interface IGetEmission {
	public Color getEmission(Point3D point);
	public double getIntensity(Point3D point);
}
