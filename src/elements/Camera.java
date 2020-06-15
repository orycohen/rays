
package elements;

import java.util.ArrayList;
import java.util.Random;

import geometries.Plane;
import primitives.Point3D;
import primitives.Vector;
import primitives.Ray;

/**
 * This class represent a camera in the scene.
 * The camera has its upward rightward and forward vectors 
 * that together sets the direction of the view of the camera.
 */
public class Camera {
	
	/**
	 * The point from which we see the whole picture
	 */
	private Point3D _p0;
	/**
	 * Vector from P0 upwards.
	 */
	private Vector _vUp; 
	/**
	 * Vector from P0 forwards.
	 */
	private Vector _vTo; 
	/**
	 * Vector from P0 rightwards.
	 */
	private Vector _vRight; 
	/**
	 * The _shutter value represents the length from 
	 * the center of the shutter to the edge of the shutter.
	 * Here, we treat the shutter as a square.
	 */
	private double _shutter;
	
	/**
	 * The number of rays we send for the 'depth of field' effect.
	 */
	private int _numFocusRays;
	
	/**
	 * Random numbers generator for inside usage.
	 * The usage is for generating the random points inside the 
	 * pixel for the 'depth of field' effect.
	 */
	private Random random = new Random();
	
	/**
	 * constructor for a new camera object that is being created
	 * @param p0 Represents the position of the camera
	 * @param up The vector that goes from to the camera upwards
	 * @param to The vector that goes from to the camera forwards to the object
	 * @param shutter Represents the length from 
	 * the center of the shutter to the edge of the shutter.
	 */
	public Camera(Point3D p0, Vector up, Vector to, double shutter){
		
		//If the Dimensions are negative, put zero value instead.
		if (shutter < 0) {
			shutter = 0;
		}
		
		//throw exception if one of the vectors is (0,0,0)
		if (up.vectorLength() == 0 || to.vectorLength() == 0)
			throw new IllegalArgumentException("vectors cannot be (0,0,0)");
		
		//if the the vectors are not orthogonal, throw exception.
		if (up.dotProduct(to) != 0)
			throw new IllegalArgumentException("the vectors must be orthogonal");
		
		_vRight = to.crossProduct(up).normalizedVector();
		_vUp = new Vector(up).normalizedVector();
		_vTo = new Vector(to).normalizedVector();
		_p0 = new Point3D(p0);
		_shutter = shutter;
		_numFocusRays = 1;
	}
	
	/**
	 * Copy constructor for a camera object.
	 * @param other the object that being copied
	 */
	public Camera(Camera other) {
		this._p0 = new Point3D(other._p0);
		this._vRight = new Vector(other._vRight);
		this._vTo = new Vector(other._vTo);
		this._vUp = new Vector(other._vUp);
		this._shutter = other._shutter;
		this._numFocusRays = other._numFocusRays;
	}
	
	/**
	 * Getter for the P0 of the camera. That is, the point from which
	 * we see the whole picture
	 * @return
	 */
	public Point3D getP0() {
		return new Point3D(_p0);
	}
	
	/**
	 * The function gets the index of a pixel in the view plane and
	 * returns the point in the space that is the center of the wanted pixel.  
	 * IMPORTANT - The indexes start from 1 rather than 0. That is,
	 * when row=column=1 we'll get the first index in the matrix. 
	 * @param pixelsX The number of columns in the view plane - how many
	 * rays we send along the breadth of the view plane.
	 * @param pixelsY the number of rows in the view plane - how many
	 * rays we send along the height of the view plane.
	 * @param row The row's index of the pixel
	 * @param column The column's index of the pixel
	 * @param distance The distance from the camera to the view plane
	 * @param width The width of the picture - how 
	 * many pixels there are in the width of the picture.
	 * @param height The height of the picture - how 
	 * many pixels there are in the height of the picture.
	 * @return The point that is the center of the required pixel.
	 */
	private Point3D findCenterOfPixel(int pixelsX, int pixelsY, int row, int column, double distance, double width, double height) {
		//The variables Rx and Ry are the width and height of 
		//a single pixel, IN UNITS OF PIXELS.
		double Rx = width / (double)pixelsX;
		double Ry = height / (double)pixelsY;
		
		//The variables moveX and moveY represents the distance we need to 
		//move in the X axis of the view plane and in the Y axis
		//of the view plane, from the center.
		double moveY = (row - ((double)pixelsY) / 2) * Ry - Ry / 2;
		double moveX = (column - ((double)pixelsX) / 2) * Rx - Rx / 2;
		
		//This vector's length represents the distance 
		//we move from the middle of the matrix.
		Vector temp = _vRight.scale(moveX).subtract(_vUp.scale(moveY));
		
		//The center point of the matrix.
		Point3D Pc = _p0.add(_vTo.scale(distance).getHead());
		
		//The middle of the (row,column) pixel
		return new Point3D(Pc.add(temp.getHead()));
	}
	
	/**
	 * The function gets the index of a pixel in the view plane and
	 * returns the point in the space that is the center of the wanted pixel.  
	 * IMPORTANT - The indexes start from 1 rather than 0. That is,
	 * when row=column=1 we'll get the first index in the matrix. 
	 * @param pixelsX The number of columns in the view plane - how many
	 * rays we send along the breadth of the view plane.
	 * @param pixelsY the number of rows in the view plane - how many
	 * rays we send along the height of the view plane.
	 * @param row The row's index of the pixel
	 * @param column The column's index of the pixel
	 * @param distance The distance from the camera to the view plane
	 * @param width The width of the picture - how 
	 * many pixels there are in the width of the picture.
	 * @param height The height of the picture - how 
	 * many pixels there are in the height of the picture.
	 * @return The point that is the center of the required pixel.
	 */
	private Point3D findRandomPointInPixel(int pixelsX, int pixelsY, int row, int column, double distance, double width, double height) {
		//The variables Rx and Ry are the width and height of 
		//a single pixel, IN UNITS OF PIXELS.
		double Rx = width / (double)pixelsX;
		double Ry = height / (double)pixelsY;
		
		//The variables moveX and moveY represents the distance we need to 
		//move in the X axis of the view plane and in the Y axis
		//of the view plane, from the center.
		double moveY = (row - ((double)pixelsY) / 2) * Ry - Ry / 2;
		double moveX = (column - ((double)pixelsX) / 2) * Rx - Rx / 2;
		
		//This vector's length represents the distance 
		//we move from the middle of the matrix.
		Vector temp = _vRight.scale(moveX + (random.nextDouble() - 0.5)*Rx).subtract(_vUp.scale(moveY + (random.nextDouble() - 0.5)*Ry));
		
		//The center point of the matrix.
		Point3D Pc = _p0.add(_vTo.scale(distance).getHead());
		
		//The middle of the (row,column) pixel
		return new Point3D(Pc.add(temp.getHead()));
	}
	
	/**
	 * The function constructs a ray through a given matrix and indexes.
	 * IMPORTANT: the indexes of the view-plane (rows and columns indexes. That is, 
	 * the parameters row and column) start from 1 rather than 0.
	 * @param pixelsX The number of columns in the view plane - how many
	 * rays we send along the breadth of the view plane.
	 * @param pixelsY the number of rows in the view plane - how many
	 * rays we send along the height of the view plane.
	 * @param row The row's index of the pixel
	 * @param column The column's index of the pixel
	 * @param distance The distance from the camera to the view plane
	 * @param width The width of the picture - how 
	 * many pixels there are in the width of the picture.
	 * @param height The height of the picture - how 
	 * many pixels there are in the height of the picture.
	 * @return A ray through the wanted pixel in the view plane.
	 */
	public Ray constructRayThroughPixel
			(int pixelsX, 
			 int pixelsY, 
			 int row, 
			 int column, 
			 double distance, 
			 double width,
			 double height) {
		
		//the center of the wanted pixel
		Point3D pixel = findCenterOfPixel(pixelsX, pixelsY, row, column, distance, width, height);
		
		//Build and return the ray we gut.
		Vector direction = new Vector(pixel.subtract(_p0));
		return new Ray(new Point3D(_p0), direction);
	}
	
	/**
	 * The function calculates four rays, one from every vertex 
	 * of the shutter (here we use a square shutter).
	 * IMPORTANT - The indexes start from 0. That is,
	 * when row=column=0 we'll get the first index in the matrix. 
	 * @param pixelsX The number of columns in the view plane - how many
	 * rays we send along the breadth of the view plane.
	 * @param pixelsY the number of rows in the view plane - how many
	 * rays we send along the height of the view plane.
	 * @param row The row's index of the pixel
	 * @param column The column's index of the pixel
	 * @param distance The distance from the camera to the view plane
	 * @param width The width of the picture - how 
	 * many pixels there are in the width of the picture.
	 * @param height The height of the picture - how 
	 * many pixels there are in the height of the picture.
	 * @param focalPlane The focal plane.
	 * @return Four rays, one from every vertex of the shutter (here we use a square shutter)
	 */
	public ArrayList<Ray> constructPixelRays
				(int pixelsX, 
				 int pixelsY, 
				 int row, 
				 int column, 
				 double distance, 
				 double width,
				 double height,
				 Plane focalPlane) {
		
		//Increase the row and column indexes by one since the findCenterOfPixel method
		//works with indexes that starts from 1 rather than from 0.
		row++;
		column++;
		
		Point3D centerOfPixel = null;
		if (pixelsX > width && pixelsY > height) {
			centerOfPixel = findRandomPointInPixel(pixelsX, pixelsY, row, column, distance, width, height);
		} else {
			centerOfPixel = findCenterOfPixel(pixelsX, pixelsY, row, column, distance, width, height);
		}
		
		Vector helpVector = new Vector(_p0); //Here the helpVector starts in 
													   //the origin and its head is the 
													   //center of the current pixel.
		ArrayList<Ray> rays = new ArrayList<Ray>();
		
		//The ray that intersects the center of the pixel in the view plane.
		Ray originalRay = new Ray(_p0, new Vector(centerOfPixel.subtract(_p0)));
		
		if (_numFocusRays == 1) {
			rays.add(originalRay);
			return rays;
		}
		
		//The focal point on the focal plane.
		Point3D focalPoint = focalPlane.findIntersections(originalRay).get(0);
		
		
		//temporary, helper point.
		Point3D p;

		
		//Loop for creating and adding the random rays, randomly every call.
		for (int index = 0; index < _numFocusRays; index++) {
			//p is a randomly selected point inside the shutter.
			p = helpVector.add(_vUp.scale(_shutter*((random.nextDouble() * 2) - 1)).add(_vRight.scale(_shutter*((random.nextDouble() * 2) - 1)))).getHead();
			rays.add(new Ray(p, new Vector(focalPoint.subtract(p))));
		}
		
		//Every ray in that list has random start inside the shutter, and vector that starts 
		//from that point and its head is the current focal point in the focal plane.
		return rays;
		}
	
	/**
	 * Getter function for the direction vector '_vTo'
	 * @return A new Vector that represents the vTo vector 
	 * of the camera.
	 */
	public Vector getVTo() {
		return new Vector(_vTo);
	}
	
	/**
	 * Setter for the numFocusRays attribute.
	 * @param numFocusRays The number of rays that would be sent through
	 * each pixel for the 'depth of field' effect. If the parameter is
	 * negative, the function would do nothing. 
	 */
	public void setNumFocusRays(int numFocusRays) {
		if (numFocusRays > 0) {
			_numFocusRays = numFocusRays;
		}
	}
}
