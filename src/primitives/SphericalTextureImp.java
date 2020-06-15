package primitives;

public class SphericalTextureImp extends TextureEmissionImp {
	
	private double _radius;
	
	public SphericalTextureImp(double radius, Texture texture, Axis axis) {
		super(axis, texture);
		this._radius = radius;
	}
	
	private Point2D translatePoint(Point3D point) {
		double phi = Math.acos(_axis.getYProjection(point) / _radius);
		double psi = Math.asin(_axis.getXProjection(point) / (_radius * Math.sin(phi)));
		return new Point2D(psi / (2*Math.PI), 1 - (phi / Math.PI));
	}
	
	@Override
	public Color getEmission(Point3D point) {
		Point2D p2d = translatePoint(point);
		return _texture.getColor(p2d.getX().getNumber(), p2d.getY().getNumber());
	}
	
	public double getIntensity(Point3D point) {
		Point2D p2d = translatePoint(point);
		return _texture.getIntensity(p2d.getX().getNumber(), p2d.getY().getNumber());
	}
}
