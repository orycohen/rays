package renderer;

import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;
import util.calcs;
import util.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import elements.*;
import geometries.Geometry;
import geometries.Plane;

/**
 * Class for rendering an image.
 * This class has a scene and image-writer that together 
 * can build the picture of the scene.
 */
public class Renderer {
	
	/**
	 * The maximum depth of the recursion of the calccolor function. 
	 */
	private final int MAX_CALC_COLOR_LEVEL = 5;
	
	/**
	 * The number of rays that are sent to calculate 
	 * the reflected and refracted colors.
	 */
	private final double NUM_REF_RAYS = 5;
		
	/**
	 * The scene from which we build the image.
	 */
	private Scene _scene;
	
	/**
	 * The builder of the image.
	 */
	private ImageWriter _imageWriter;
	
	/**
	 * Inner class so the scene information can be 
	 * saved more easelly when rendering an image.
	 */
	private class sceneInfo
	{
		/**
		 * The width (number of pixels in breadth) of the image
		 */
		public int width; 
		
		/**
		 * The height (number of pixels in height) of the image.
		 */
		public int height; 
		
		/**
		 * The number of rays that we SEND through the width of the view plane. 
		 * That is, the X indexes in the view plane.
		 */
		public int Nx;
		
		/**
		 * The number of rays that we SEND through the height of the view plane. 
		 * That is, the Y indexes in the view plane.
		 */
		public int Ny; 		
		
		/**
		 * The distance of camera from the view plane.
		 */
		public double distance; 
		
		/**
		 * The camera that is in the scene
		 */
		public Camera camera; 
		
		/**
		 * The background color in the scene.
		 */
		public Color background; 
		
		/**
		 * The focal plane for the focus
		 */
		public Plane focalPlane; 
		
		/**
		 * The maximum distance to check for reflected/refracted rays.
		 */
		public double maxRefDistance;
		
		public sceneInfo() {}
		
		public void Initialize()
		{
			width = _imageWriter.getWidth();
			height = _imageWriter.getHeight();		
			Nx = _imageWriter.getNx();
			Ny = _imageWriter.getNy();
			distance = _scene.getDistance();
			camera = _scene.getCamera();
			background = _scene.getBackground();
			focalPlane = _scene.getFocalPlane();
			maxRefDistance = _scene.getMaxReflectedDistance();
		}
	}
	
	/**
	 * Inner class that contains point in 3D space and a geometry.
	 * @author orycohen
	 */
	private class _geoPoint{
		/**
		 * The Geometry in the 3D space
		 */
		Geometry geometry;
		
		/**
		 * The point in the 3D space
		 */
		Point3D point;
	}
	
	/**
	 * All the information about the current rendered scene.
	 */
	final private sceneInfo _info;
	
	/**
	 * Constructor for creating a new instance of the Renderer class
	 * @param scene The scene from which we build the image.
	 * @param imageWriter The builder of the image.
	 */
	public Renderer(ImageWriter imageWriter, Scene scene) {
		_scene = new Scene(scene);
		_imageWriter = new ImageWriter(imageWriter);
		_info = new sceneInfo();
	}
	
	/**
	 * Filling the buffer according to the geometries that are in the scene.
	 * This function does not creating the picture, but rather filling the pixels 
	 * according to the scene.
	 */
	public void renderImage() {
		
		_info.Initialize();
		_scene.getGeometries().createTree();
		int numInx = _info.Nx / _info.width, numIny = _info.Ny / _info.height;
		
		//In that case there is no super sampling.
		if (_info.Nx <= _info.width && _info.Ny <= _info.height) {
			for (int row = 0; row < _info.Ny; row++) {
				System.out.println(row);
				for (int column = 0; column < _info.Nx; column++) {
					processPixel(row,column);
				}
			} 
			
		//Here (else) we need to deal with super sampling.
		} else {
			for (int row = 0; row < _info.height; row++) {
				System.out.println(row);
				for (int column = 0; column < _info.width; column++) {
					processSuperSamplingPixel(row, column, numInx, numIny);
				}
			}
		}
	}
	
	/**
	 * Processes the given ({@code column}, {@code row}) pixel of the view plane
	 * and writes it to the image.
	 * @param row The view plane's row indicator.
	 * @param column The view plane's column indicator.
	 */
	private void processPixel(int row, int column)
	{
		_imageWriter.writePixel(column, row, viewPlanePixelColor(row, column).getColor());
	}	

	/**
	 * Processes the given ({@code column}, {@code row}) pixel of the final image's dimensions.
	 * @param row The final image's row indicator
	 * @param column The final image's column indicator
	 * @param numInx Indicates how many columns there are in every final pixel. 
	 * @param numIny Indicates how many rows there are in every final pixel. 
	 */
	private void processSuperSamplingPixel(int row, int column, int numInx, int numIny)
	{
		Color AveragePixelColor = Color.black;
		//The next two loops are for the super sampling effect.
		for (int insidePixel_Y = 0; insidePixel_Y < numIny; insidePixel_Y++) {
			for (int insidePixel_X = 0; insidePixel_X < numInx; insidePixel_X++) {
				AveragePixelColor = AveragePixelColor.add(viewPlanePixelColor(numIny * row + insidePixel_Y, numInx * column + insidePixel_X));
			} 
		}
		AveragePixelColor = AveragePixelColor.scale(1/(double)(numInx*numIny));
		_imageWriter.writePixel(column, row , AveragePixelColor.getColor());
	}
	
	/**
	 * Function that processes one single pixel of the view plane.
	 * @param ViewPlaneRow The view plane's row indicator. The range of {@code ViewPlaneRow} is: <br> 
	 * {@code 0 <= ViewPlaneRow < }{@link sceneInfo#Ny}
	 * @param ViewPlaneColumn The view plane's column indicator. The range of {@code ViewPlaneRow} is: <br> 
	 * {@code 0 <= ViewPlaneColumn < }{@link sceneInfo#Nx}
	 * @return The appropriate color of the given (column, row) index 
	 * of the view plane.
	 */
	private Color viewPlanePixelColor(int ViewPlaneRow, int ViewPlaneColumn)
	{
		ArrayList<Ray> rays = _info.camera.constructPixelRays(_info.Nx, _info.Ny, ViewPlaneRow, ViewPlaneColumn, _info.distance, _info.width, _info.height, _info.focalPlane);
		Map<Geometry, List<Point3D>> intersectionPoints;
		Color rayColor, toReturn = Color.black;
		for (int focalIndex = 0; focalIndex < rays.size(); focalIndex++) {
			intersectionPoints = _scene.getGeometries().findMapOfIntersections(rays.get(focalIndex));
			if (intersectionPoints.size() == 0) {
				toReturn = toReturn.add(_info.background);
			} else {
				rayColor = calcColor(getClosestPoint(intersectionPoints, _scene.getCamera().getP0()), rays.get(focalIndex));
				toReturn = toReturn.add(rayColor);
			} 
		}
		toReturn = toReturn.scale(1.0/((double)rays.size()));
		return toReturn;
	}
	
	/**
	 * Calculates the color in a given point.
	 * The function gets a point and returns the color in this point.
	 * @param geoPoint Includes the point in which it calculates the color
	 * and the geometry that that point belongs to.
	 * @param inRay The ray which in the first call of this color 
	 * is the ray from the camera to the point in which we calculate the color,
	 * and in every recursive call is the ray the continues form that point either forward
	 * or backward.
	 * @param level The level of the recursion
	 * @param k	The parameter decreased in every recursive call that determine how much we "see".
	 * @return the color in the given point
	 */
	private Color calcColor(_geoPoint geoPoint, Ray inRay, int level, double K) {
		//The recursion condition.
		if (level == 0 || util.calcs.closeToZero(K))
			return Color.black;
		
		double Kt = 1 - geoPoint.geometry.getIntensity(geoPoint.point);
		Color color = _scene.getAmbientLight().getIntensity();
		color = color.add(geoPoint.geometry.getEmission(geoPoint.point).scale(1 - Kt));
		Vector normalToPoint = geoPoint.geometry.getNormal(geoPoint.point);
		Vector intersectsPoint = inRay.getDirection();
		
		//No color if there is no normal vector.
		if (normalToPoint == null) {
			return Color.black;
		}
		Material material = geoPoint.geometry.getMaterial();
		int nShininess = material.getShininess();
		double Kd = material.getKd();
		double Ks = material.getKs();
		
		for (LightSource lightSource : _scene.getLights()) {
			
			Vector l = lightSource.getL(geoPoint.point);
			
			if (l.dotProduct(normalToPoint)*intersectsPoint.dotProduct(normalToPoint) > 0) {
				
				double o = occluded(geoPoint, lightSource); 
				if (!calcs.closeToZero(o * K)) {
					Color lightIntensity = lightSource.getIntensity(geoPoint.point).scale(o);	
					color = color.add(
							calcDiffusive(Kd, l, normalToPoint, lightIntensity),
							calcSpecular(Ks, l, normalToPoint, intersectsPoint, nShininess, lightIntensity));
				}
			}
		}
		//------Parameters for the reflected and refracted.
		Map<Geometry,List<Point3D>> intersections;
		double coneLength = material.getConeLength();
		//------
		
		Ray originalReflected = constructReflectedRay(normalToPoint, geoPoint.point, inRay), ray;
		double reflectRadius = material.getReflectRadius();
		double Kr = geoPoint.geometry.getMaterial().getKr();
		
		Color reflectedLight = Color.black;
		
		//Calculating the reflecting light.
		for (int index = 0; index < NUM_REF_RAYS; index++) {
			ray = random.genRandRay(originalReflected, reflectRadius, coneLength);
			intersections = _scene.getGeometries().findMapOfIntersections(ray);
			if (intersections.size() != 0) {
				_geoPoint reflectedPoint = getClosestPoint(intersections, geoPoint.point);
				
				if (geoPoint.point.distance(reflectedPoint.point) < _info.maxRefDistance && reflectedPoint.point != null) {
					reflectedLight = reflectedLight.add(calcColor(reflectedPoint, ray, level - 1, K * Kr).scale(Kr));
				}
			} 
		}
		
		Ray originalRefracted = constructRefractedRay(geoPoint.point, inRay);
		double refractRadius = material.getTranspRadius();
				
		Color refractedLight = Color.black;		
		
		//Calculating the refrecting light.
		for (int index = 0; index < NUM_REF_RAYS; index++) {
			ray = random.genRandRay(originalRefracted, refractRadius, coneLength);
			intersections = _scene.getGeometries().findMapOfIntersections(ray);
			if (intersections.size() != 0) {
				_geoPoint refractedPoint = getClosestPoint(intersections, geoPoint.point);
				if (refractedPoint.point != null) {
					refractedLight = refractedLight.add(calcColor(refractedPoint, ray, level - 1, K * Kt).scale(Kt));
				}
			} 
		}
		reflectedLight = reflectedLight.scale(1/NUM_REF_RAYS);
		refractedLight = refractedLight.scale(1/NUM_REF_RAYS);
		color = color.add(reflectedLight, refractedLight);
		return new Color(color);
	}
	
	/**
	 * Calculates the color in a given point.
	 * The function gets a point and returns the color in this point.
	 * @param geoPoint the point in which it calculates the color
	 * @param inRay The ray which in the first call of this color 
	 * is the ray from the camera to the point in which we calculate the color,
	 * and in every recursive call is the ray the continues form that point either forward
	 * or backward.
	 * @return the color in the given point
	 */
	private Color calcColor(_geoPoint geoPoint, Ray inRay) {
		return calcColor(geoPoint, inRay, MAX_CALC_COLOR_LEVEL, 1.0);
	}
	
	/**
	 * Inner helper class to change the variable number inside
	 * the lambda expression in the 'getClosestPoint' function.
	 * The 'getClosestPoint' function performs a 'forEach' iteration where 
	 * inside it should change a variable 'distance' accordingly.
	 * We could not change the 'distance' if it was double, hence,
	 * we made a little helper class for that.
	 */
	private static class Number { 
		public double number = Double.MAX_VALUE; 
	}
	
	/**
	 * Finding the closest point to the P0 of the camera.
	 * @param points A list of points, the function should find from
	 * this list the closet point to P0 of the camera in the scene.
	 * @return A new Map<Geometry, Point3D> that contains geometries
	 * as a keys and points as a values. The point is the closest one 
	 * of the list we got as the values of the parameter to the function.
	 */
	private _geoPoint 
	getClosestPoint(Map<Geometry, List<Point3D>> intersectionPoints, Point3D fromPoint) {
		Number distance = new Number();
		
		_geoPoint minDistancePoint = new _geoPoint();
		
		intersectionPoints.forEach((geometry,points)->
		{			
			points.forEach((point) -> {
				
				if (fromPoint.distance(point) < distance.number) {
					minDistancePoint.geometry = geometry;
					minDistancePoint.point = point;
					distance.number = fromPoint.distance(point);
				}
			});
		});
		return minDistancePoint;
	}

	/**
	 * Printing the grid with a fixed interval between lines
	 * @param interval The interval between the lines.
	 */
	public void printGrid(int interval) {
		int width = _imageWriter.getWidth(),
			height = _imageWriter.getHeight();
		//Writing the lines.
		for (int row = 0; row < height; row++)
			for (int col = interval; col < width; col += interval) {
				_imageWriter.writePixel(col, row, 255,255,255);
				_imageWriter.writePixel(row, col, 255,255,255);
			}
	}

	/**
	 * Pots the pixels we wrote by far to an image.
	 * This function creates the final picture.
	 */
	public void writeToImage() {
		_imageWriter.writeToimage();
	}
	
	/**
	 * Inner function for calculating the diffusion of the light 
	 * with the factor Kd, the vectors l and n and the light intensity.
	 * @param Kd The diffusion factor that the geometry has
	 * @param l The vector from the light source to the point
	 * in which we want to calculate the diffuse.
	 * @param n The normal vector to the geometry in the point 
	 * in which we want to calculate the diffuse.
	 * @param lightIntensity The actual intensity of the light source
	 * @return A new color that represents the diffusion of 
	 * the light in the wanted point.
	 */
	private Color calcDiffusive(double Kd, Vector l, Vector n, Color lightIntensity) {
		double LdotN = Math.abs(l.normalizedVector().dotProduct(n));
		return lightIntensity.scale(LdotN * Kd);
	}
	
	/**
	 * Inner function for calculating the specularity of the light 
	 * with the factor Ks, the vectors l and n and the light intensity.
	 * @param Ks The specularity factor that the geometry has.
	 * @param l The vector from the light source to the point
	 * in which we want to calculate the diffuse.
	 * @param n n The normal vector to the geometry in the point 
	 * in which we want to calculate the diffuse.
	 * @param v The vector from the wanted point to the camera.
	 * Note, the head of the vector is the position of the camera.
	 * That is, the vector starts from the point in which we calculate the intensity.
	 * @param nShininess The shininess of the geometry.
	 * @param lightIntensity The actual intensity of the light source
	 * @return A new color that represents the specularity of 
	 * the light in the wanted point.
	 */
	private Color calcSpecular
	(double Ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {
		
		Vector r = l.subtract(n.scale(2 * l.dotProduct(n))).normalizedVector();
		double temp = r.dotProduct(v);
		if (temp > 0)
			return Color.black;
		double factor = Ks * Math.pow(-temp, nShininess);
		return lightIntensity.scale(factor);
	}
	
	
	/**
	 * A boolean function that returns true if the a light source 
	 * is occluded by a geometry in a given point in the space and false 
	 * otherwise.
	 * @param l The vector from the light source to the point.
	 * @param geopoint Point and geometry combined together in one object.
	 * @param isDirectional lag for recognizing a directional light,
	 * that its vector has no length (infinite, actually).
	 * @param radius The radius of the light source
	 * @return True if the a light source is occluded by a geometry 
	 * in a given point in the space and false otherwise.
	 */
	private double occluded(_geoPoint geopoint, LightSource lightSource) {
		Vector l = lightSource.getL(geopoint.point);
		double distance = lightSource instanceof DirectionalLight ? Double.MAX_VALUE : l.vectorLength();
		Vector lightDirection = l.scale(-1); //The vector from the point to the light source.

		// Moving the point a little towards the light source 
		// so the ray we send to the light source won't intersect itself.
		Vector normal = geopoint.geometry.getNormal(geopoint.point);
		Vector epsVector = normal.scale(normal.dotProduct(lightDirection) > 0 ? 2 : -2);
		Point3D geometryPoint = geopoint.point.add(epsVector.getHead());
		
		double globalShadowK = 0;
		
		// a ray from the point to the middle of the light source
		Ray lightRay = new Ray(geometryPoint, lightDirection);
		
		
		// first ray to the center of the light source
		Map<Geometry, List<Point3D>> intersectionPoints = _scene.getGeometries().findMapOfIntersections(lightRay);
		globalShadowK += getShadowK(geopoint, distance, intersectionPoints); 
		
		
		//If the light source does not have a volume so we don't need to send more rays, return.
		if (!(lightSource instanceof VolumedLightSrc)) {
			return globalShadowK;
		}
		
		//From here and on, we deal with a volumed light source.
		VolumedLightSrc VlightSource = (VolumedLightSrc)lightSource;
		double radius = VlightSource.getRadius();
				
		// Orthogonal vectors. These make a new 'X, Y' axis on which we build a spiral. 
		Vector vecY = new Vector(lightDirection.getHead().getY().getNumber(), -lightDirection.getHead().getX().getNumber(), 0).normalizedVector();
		Vector vecX = lightDirection.crossProduct(vecY).normalizedVector();
		Vector movementOnSpiral;
		
		double deltaRadius = radius / (double)VlightSource.getNumOfSamples();
		
		// sending rays from the center of the light source in a spiral up to the radius 
		// of the light source.
		
		int index = 1;
		
		//Creating the spiral with the pre-initialized parameters (sin and cos) in the LightSource.
		//In every entry of lightSource there is a two dimensional array, which in the first index 
		//is the sin and the second is the cos.
		for (int i = 0; i < VlightSource.Scalars.size(); i++) {	
			movementOnSpiral = vecY.scale(VlightSource.Scalars.get(i)[0]).add(vecX.scale(VlightSource.Scalars.get(i)[1])).scale(deltaRadius * (index++));
			lightRay = new Ray(geometryPoint, lightDirection.add(movementOnSpiral));
			intersectionPoints = _scene.getGeometries().findMapOfIntersections(lightRay);
			globalShadowK += getShadowK(geopoint, distance, intersectionPoints);
		}
				
		return globalShadowK / ((double)VlightSource.getNumOfSamples() + 1.0);
	}

	/**
	 * Checks how much a light source is occluded by the geometries that are between
	 * a point on a geometry and a given light source
	 * @param geopoint The point on which we check the shadow parameter and 
	 * the geometry that the point belongs to.
	 * @param distance The distance from the point in geopoint to the light source.
	 * @param intersectionPoints All the geometries that are between the point in 
	 * geopoint and the light source.
	 * @return A shadow parameter between 0 and 1.
	 */
	private double getShadowK(_geoPoint geopoint, double distance, Map<Geometry, List<Point3D>> intersectionPoints) {
		double shadowK = 1.0;
		for (Map.Entry<Geometry, List<Point3D>> entry : intersectionPoints.entrySet()) {
			for (Point3D point : entry.getValue()) {
				if (point.distance(geopoint.point) <= distance) {
					shadowK *=  1 - entry.getKey().getIntensity(point);
					//shadowK *= entry.getKey().getMaterial().getKt();
				}
			}
		}
		return shadowK;
	}
	
	/**
	 * Function for calculating the ray that is reflected from a geometry backwards.
	 * @param n The normal to the geometry in the point that is in the geoPoint.
	 * @param geoPoint Contains the geometry and the point on it 
	 * @param inRay The ray that hits the geometry.
	 * @return A new reflected ray
	 */
	private Ray constructReflectedRay(Vector n, Point3D point, Ray inRay) {
		Vector v = inRay.getDirection();
		Vector eps = v.subtract(n.scale(2 * v.dotProduct(n))).normalizedVector();
		return new Ray(point.add(eps.scale(2).getHead()), eps);
	}
	
	/**
	 * Function for calculating the ray that is reflected from a geometry forwards.
	 * @param geoPoint Contains the geometry and the point on it 
	 * @param inRay The ray that hits the geometry.
	 * @return A new reflected ray
	 */
	private Ray constructRefractedRay(Point3D point, Ray inRay) {
		Vector eps = inRay.getDirection().normalizedVector();
		return new Ray(point.add(eps.scale(2).getHead()), eps);
	}

}


