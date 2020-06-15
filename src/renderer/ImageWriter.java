package renderer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageWriter {

	/**
	 * The number of pixels in the breadth.
	 */
	private int _imageWidth;
	
	/**
	 * The number of pixels in the height.
	 */
	private int _imageHeight;
	
	/**
	 * The breadth of the view plane in length units. That is,
	 * how many columns there are in the view plane.
	 */
	private int _Nx;
	
	/**
	 * The height of the view plane in length units. That is,
	 * how many rows there are in the view plane.
	 */
	private int _Ny;

	/**
	 * The width of a single view plane's coordinate in units of pixels.
	 */
	private double _Rx;
	
	/**
	 * The height of a single view plane's coordinate in units of pixels.
	 */
	private double _Ry;

	/**
	 * The path where the images are saved.
	 */
	final String PROJECT_PATH = System.getProperty("user.dir");
	
	/**
	 * The image, where we put the colors of the pixels.
	 */
	private BufferedImage _image;
	
	/**
	 * The name of the image.
	 */
	private String _imageName;

	/**
	 * Constructor for creating a new object, which with we create
	 * and put a new image into the user directory of the project.
	 * @param imageName The name of the image.
	 * @param width The number of pixels in the breadth.
	 * @param height The number of pixels in the height.
	 * @param Nx The breadth of the view plane in length units. That is,
	 * how many columns there are in the view plane.
	 * @param Ny The height of the view plane in length units. That is,
	 * how many rows there are in the view plane.
	 */
	public ImageWriter(String imageName, int width, int height, int Nx, int Ny) {
		_imageName = imageName;
		_imageWidth = width;
		_imageHeight = height;
		_Nx = Nx;
		_Ny = Ny;
		
		_Rx = _imageWidth / Nx;
		_Ry = _imageHeight / Ny;
		
		_image = new BufferedImage(_imageWidth, _imageHeight, BufferedImage.TYPE_INT_RGB);
	}
	
	/**
	 * Copy constructor for a deep copy of an ImageWriter object.
	 * @param other The object that is being copied.
	 */
	public ImageWriter (ImageWriter other) {
		this(other._imageName,
			 other._imageWidth, other._imageHeight,
			 other._Nx, other._Ny);
	}
	
	/**
	 * Getter for the number of pixels in the breadth.
	 * @return The number of pixels in the breadth.
	 */
	public int getWidth()  { 
		return _imageWidth;  
	}
	
	/**
	 * Getter for the number of pixels in the height.
	 * @return The number of pixels in the height.
	 */
	public int getHeight() { 
		return _imageHeight; 
	}

	/**
	 * Getter for the height of the view plane in length units.
	 * @return The height of the view plane in length units, that is,
	 * how many rays we send along the height of the view plane.
	 */
	public int getNy() { 
		return _Ny; 
	}
	
	/**
	 * Getter for the breadth of the view plane in length units.
	 * @return The breadth of the view plane in length units, that is,
	 * how many rays we send along the breadth of the view plane.
	 */
	public int getNx() { 
		return _Nx;
	}

	/**
	 * Setter for the height of the view plane in length units.
	 * @param _Ny The height of the view plane in length units. 
	 * That is, how many rows there are in the view plane.
	 */
	public void setNy(int _Ny) { 
		this._Ny = _Ny; 
	}
	
	/**
	 * Setter for the breadth of the view plane in length units.
	 * @param _Nx The breadth of the view plane in length units.
	 * That is, how many columns there are in the view plane.
	 */
	public void setNx(int _Nx) { 
		this._Nx = _Nx; 
	}
	
	/**
	 * Function for creating the image into the user directory.
	 */
	public void writeToimage(){
		File ouFile = new File(PROJECT_PATH + "/" + _imageName + ".jpg");

		try {
			ImageIO.write(_image, "jpg", ouFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Puts a new rgb color in a given (x,y) pixel.
	 * @param xIndex The x index of the pixel.
	 * @param yIndex The y index of the pixel.
	 * @param r The red component of the color.
	 * @param g The green component of the color.
	 * @param b The blue component of the color.
	 */
	public void writePixel(int xIndex, int yIndex, int r, int g, int b){
		int rgb = new Color(r, g, b).getRGB();
		_image.setRGB(xIndex, yIndex, rgb);
	}
	
	/**
	 * Puts a new rgb color in a given (x,y) pixel.
	 * @param xIndex The x index of the pixel.
	 * @param yIndex The y index of the pixel.
	 * @param rgbArray Red green and blue are inside of that given array.
	 */
	public void writePixel(int xIndex, int yIndex, int[] rgbArray){
		int rgb = new Color(rgbArray[0], rgbArray[1], rgbArray[2]).getRGB();
		_image.setRGB(xIndex, yIndex, rgb);
	}
	
	/**
	 * Puts a new rgb color in a given (x,y) pixel.
	 * @param xIndex The x index of the pixel.
	 * @param yIndex The y index of the pixel.
	 * @param color The color is given as a color, rather then 
	 * red green and blue components. Those components are inside
	 * the given color.
	 */
	public void writePixel(int xIndex, int yIndex, Color color){
		//In case of super sampling, we call the 'setRGB' function only once.
		//In other cases, we print number of pixels with the same color in the loop.
		int xPixels = (_Rx < 1 ? 1 : (int)_Rx), yPixels = (_Ry < 1 ? 1 : (int)_Ry);
		for (int row = 0; row < yPixels; row++) {
			for (int column = 0; column < xPixels; column++) {
				_image.setRGB(xIndex * xPixels + column, yIndex * yPixels + row, color.getRGB());
			}
		}
	}
	
}