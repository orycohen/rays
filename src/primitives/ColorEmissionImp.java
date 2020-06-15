package primitives;

public class ColorEmissionImp implements IGetEmission {

	private Color _emission;
	
	public ColorEmissionImp(Color color) {
		this._emission = new Color(color);
	}
	
	@Override
	public Color getEmission(Point3D point) {
		return new Color(this._emission);
	}

	@Override
	public double getIntensity(Point3D point) {
		return 1;
	}

}
