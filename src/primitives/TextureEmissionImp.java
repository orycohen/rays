package primitives;

public abstract class TextureEmissionImp implements IGetEmission {
	protected Axis _axis;
	protected Texture _texture;
	
	public TextureEmissionImp(Axis axis, Texture texture) {
		this._axis = new Axis(axis);
		this._texture = texture;
	}
}
