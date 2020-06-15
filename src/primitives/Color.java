package primitives;

import util.Counter;

/**
 * The class that represent a color.
 * Limits to RGB with either integers or double,
 * ensures limits, (0-255) for integer or (0-1) for double, are not broken,
 * and allow single point of interface color manipulations.
 * @author orycohen
 */
public class Color {
	
	/**
	 * The basic red color of this Color object
	 */
	private double _red;
	
	/**
	 * The basic green color of this Color object
	 */
	private double _green;
	
	/**
	 * The basic blue color of this Color object
	 */
	private double _blue;
	
	/**
	 * The color black for reusing.
	 */
	public static Color black = new Color(0,0,0);
	
	/**
	 * The white color for reusing
	 */
	public static Color white = new Color(255,255,255);
	
	private static Counter counter = new Counter();;
	public static Counter getNewCounter() {return counter;}
	
	/**
	 * Default Constructor for this Color object.
	 * With this default constructor, the color would be black.
	 */
	public Color() {
		_red = _green = _blue = 0;
		try { counter.increase(); } catch(Exception e) {}
	}
	
	/**
	 * Constructor for the Color class. The constructor gets
	 * integers, and builds a color with them.
	 * @param r the red component of the color
	 * @param g the green component of the color
	 * @param b the blue component of the color
	 */
	public Color(double r, double g, double b) {
		if (r < 0 || g < 0 || b < 0)
			throw new IllegalArgumentException("Colors' value cannot be negative");
		_red = r;
		_green = g;
		_blue = b;
		try { counter.increase(); } catch(Exception e) {}
	}
	
	/**
	 * Copy constructor for this class.
	 * There is no deep copy to do here since the _red _green and _blue
	 * are all primitives.
	 * @param other The object that is being copied
	 */
	public Color(Color other) {
		if (other == null) {
			_red = _green = _blue = 0;
		} else {
			_red = other._red;
			_green = other._green;
			_blue = other._blue;
		}
		counter.increase();
	}
	
	/**
	 * Getter for the red component of this Color object.
	 * @return the red component's value of this Color object.
	 */
	public double getRed() {
		return _red;
	}
	
	/**
	 * Getter for the green component of this Color object.
	 * @return the green component's value of this Color object.
	 */
	public double getGreen() {
		return _green;
	}

	/**
	 * Getter for the blue component of this Color object.
	 * @return the blue component's value of this Color object.
	 */
	public double getBlue() {
		return _blue;
	}

	/**
	 * Getter for the color that is represented by this object.
	 * @return the java.awt.Color color that is represented by this object.
	 */
	public java.awt.Color getColor(){
		return new java.awt.Color(
				_red > 255.0 ? 255 : (int)_red,
				_green > 255.0 ? 255 : (int)_green,
				_blue > 255.0 ? 255 : (int)_blue);
	}
	
	/**
	 * Adding one or more colors to this color. The function
	 * adds the colors to this object's color (!not changes it!)
	 * and returns the addition of the colors, a new Color.
	 * @param args The colors to add to this color.
	 * @return A new Color that is the addition of this color and the 
	 * colors in the input.
	 */
	public Color add(Color... args) {
		double r = _red, g = _green, b = _blue;
		for (Color c : args) {
			r += c.getRed();
			g += c.getGreen();
			b += c.getBlue();
		}
		return new Color(r,g,b);
	}
	
	/**
	 * function for scaling a color with a parameter
	 * @param scale the parameter for the scale operation
	 * @return a new color
	 */
	public Color scale(double scale) {
		double r = _red*scale;
		double g = _green*scale;
		double b = _blue*scale;
		return new Color(r, g, b);
	}

	/**
	 * function for reducing a color with a parameter
	 * @param scale the parameter for the reduce operation
	 * @return a new color
	 */
	public Color reduce(double scale) {
		if (scale == 0)
			throw new IllegalArgumentException
			("the scale parameter cannot be zero");
		double r = _red/scale;
		double g = _green/scale;
		double b = _blue/scale;
		return new Color(r, g, b);
	}
	
	public void square() {
		_red = _red * _red;
		_blue = _blue * _blue;
		_green = _green * _green;
	}
	
	public void squareRoot() {
		_red = Math.sqrt(_red);
		_blue = Math.sqrt(_blue);
		_green = Math.sqrt(_green);
	}
}
