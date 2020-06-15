package primitives;

public class PlaneTextureImp extends TextureEmissionImp {
	
	private double _width;
	private double _height;
	
	public PlaneTextureImp(Texture texture, Axis axis, double width, double height) {
		super(axis, texture);
		this._width = width;
		this._height = height;
	}
	
	@Override
	public Color getEmission(Point3D point) {
		double x = this._axis.getXProjection(point), y = this._axis.getYProjection(point);
		return this._texture.getColor(x/_width, y/_height);
	}
	
	public double getIntensity(Point3D point) {
		return _texture.getIntensity(_axis.getXProjection(point), _axis.getYProjection(point));
	}
}
