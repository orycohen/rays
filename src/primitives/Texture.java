package primitives;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Texture {
	
	private BufferedImage _colorFirstMap;
	private BufferedImage _colorSecondMap;
	private BufferedImage _intensityMap;
	private double _width;
	private double _height;
	private double _checkerSize;
	private double _outlineWidth;
	private boolean _isChecker = false;
	private boolean _outlineIntensity = false;
	private Color _firstCheckerColor;
	private Color _secondCheckerColor;
	private Color _outlineCheckerColor;
	
	private enum checkerPlace { first, second, outline };
	
	/**
	 * Constructor for a checker based texture.
	 * Instead of getting the color from an image, we get a 
	 * color based on the position in the texture area that could 
	 * be one of three predefined colors.
	 * @param firstColor The first possible color
	 * @param secondColor The second possible color
	 * @param outlineColor The color between the two other colors
	 * @param checkerSize The area that each of the first two colors take
	 * @param outlineWidth The size of the area that is between the two 
	 * other colors. If this size is zero, then the outlineColor would not be seen
	 */
	public Texture(Color firstColor, Color secondColor, Color outlineColor, double checkerSize, double outlineWidth) {
		_checkerSize = checkerSize;
		_outlineWidth = outlineWidth;
		_firstCheckerColor = new Color(firstColor);
		_secondCheckerColor = new Color(secondColor);
		_outlineCheckerColor = new Color(outlineColor);
		_isChecker = true;
	}
	
	/**
	 * Constructor for an image based texture mapping.
	 * @param colorMapImageDirectory The file name that contains the color mapping
	 * @param width the width of the texture in Cartesian units
	 * @param height the height of the texture in Cartesian units
	 */
	public Texture(String colorMapImageDirectory, double width, double height) {
		try {
			_colorFirstMap = ImageIO.read(new File(colorMapImageDirectory));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		_width = width < 0 ? 0 : width;
		_height = height < 0 ? 0 : height;
	}
	
	/**
	 * A copy constructor for deep copying a texture.
	 * <b>Note</b> that the texture map would not be deep copied
	 * @param other The texture to be copied
	 */
	public Texture(Texture other) {
		this._colorFirstMap = other._colorFirstMap;
		this._colorSecondMap = other._colorSecondMap;
		this._intensityMap = other._intensityMap;
		this._width = other._width;
		this._height = other._height;
		this._checkerSize = other._checkerSize;
		this._outlineWidth = other._outlineWidth;
		this._isChecker = other._isChecker;
		this._outlineIntensity = other._outlineIntensity;
		this._firstCheckerColor = new Color(other._firstCheckerColor);
		this._secondCheckerColor = new Color(other._secondCheckerColor);
		this._outlineCheckerColor = new Color(other._outlineCheckerColor);
	}
	
	
	public void setChecker(String secondColorMapImageDirectory, Color outlineColor, double checkerSize, double checkerBorderWidth) {
		try {
			if (_colorFirstMap == null) {
				throw new Exception("First color map cannot be null with checker.");
			}
			_colorSecondMap = ImageIO.read(new File(secondColorMapImageDirectory));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		_outlineCheckerColor = outlineColor == null ? Color.black : new Color(outlineColor);
		_checkerSize = checkerSize < 0 ? 0 : checkerSize;
		_outlineWidth = checkerBorderWidth < 0 ? 0 : checkerBorderWidth;
		_isChecker = true;
	}
	
	public Color getColor(double x, double y) {
		return _isChecker ? _getCheckerColor(x, y) : _getColor(_colorFirstMap, x, y);
	}
	
	/**
	 * Method that gets two numbers x, y that are in the range [0, 1] to map it to 
	 * the appropriate color on the texture.
	 * @param x The number that is mapped to the width of the texture
	 * @param y The number that is mapped to the height of the texture
	 * @param image The texture map
	 * @return The appropriate color on the texture map
	 */
	private Color _getColor(BufferedImage image, double x, double y) {
		int imageHeight = image.getHeight() - 1;
		java.awt.Color c = new java.awt.Color(image.getRGB((int)((x/_width)*image.getWidth()), imageHeight - (int)((y/_height)*imageHeight)));
		return new Color(c.getRed(), c.getGreen(), c.getBlue());
	}
	
	
	private Color _getCheckerColor(double x, double y) {
		switch (_checkPlace(x, y)) {
		case first:
			return _colorFirstMap == null ? new Color(_firstCheckerColor) : _getColor(_colorFirstMap, x, y);
		case second:
			return _colorSecondMap == null ? new Color(_secondCheckerColor) : _getColor(_colorSecondMap, x, y);
		case outline:
			return new Color(_outlineCheckerColor);
		default:
			return new Color(255, 0, 0);
		}
	}
	
	private checkerPlace _checkPlace(double x, double y) {
		int ix = (int)Math.floor(x/_checkerSize), iy = (int)Math.floor(y/_checkerSize);
		double fx = x/_checkerSize - ix, fy = y/_checkerSize - iy, width = 0.5 * _outlineWidth/_checkerSize; 
		boolean in_outline = (fx < width || fx > 1.0 - width) || (fy < width || fy > 1.0 - width);
		if (in_outline) {
			return checkerPlace.outline;
		} if ((ix + iy) % 2 == 0) {
			return checkerPlace.first;
		} else {
			return checkerPlace.second;
		}
	}
	
	public double getIntensity(double x, double y) {
		if (_intensityMap != null) {
			int height = _intensityMap.getHeight() - 1;
			java.awt.Color c = new java.awt.Color(_intensityMap.getRGB((int)((x/_width)*_intensityMap.getWidth()), height - (int)((y/_height)*height)));
			return ((double)c.getRed())/255.0;
		}
		if (_outlineIntensity) {
			return _checkPlace(x,y) == checkerPlace.outline ? 1 : 0;
		}
		return 1;
	}
	
	public void setOutlineIntensity(boolean outlineIntensity) {
		_outlineIntensity = outlineIntensity;
	}
	public void setOutlineIntensity(BufferedImage intensityMap) {
		_intensityMap = intensityMap;
		_outlineIntensity = true;
	}
	
	public double getWidth() {return _width;}
	public double getHeight() {return _height;}
}
