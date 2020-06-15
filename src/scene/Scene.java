package scene;

import geometries.Geometries;
import geometries.Plane;
import primitives.Point3D;
import primitives.Vector;
import primitives.Color;
import primitives.Material;

import java.util.ArrayList;
import java.util.List;

import elements.AmbientLight;
import elements.Camera;
import elements.LightSource;

/**
 * This class represents a scene with camera and geometries.
 * @author orycohen
 */
public class Scene {
	
	/**
	 * The name of the scene
	 */
	private String _name;
	
	/**
	 * The ambient-light color. It affects all the geometries 
	 * the same way.
	 */
	private AmbientLight _ambientLight;
	
	/**
	 * The background color of the scene.
	 */
	private Color _background;
	
	/**
	 * The shapes that are in the scene
	 */
	private Geometries _geometries;
	
	/**
	 * All the light sources in the scene are in this list.
	 */
	private List<LightSource> _lights;
	
	/**
	 * The camera in the scene
	 */
	private Camera _camera;
	
	/**
	 * 	The distance of the camera from the view plane.
	 */
	private double _distance;
	
	/**
	 * The focal plane which makes the depth of field effect.
	 */
	private Plane _focalPlane;
	/**
	 * 	The distance of the camera from the view plane.
	 */
	private double _maxReflectedDistance;
	/**
	 * Constructor. The constructor puts default values 
	 * to the rest of the properties.
	 * @param name The name of the scene
	 */
	public Scene(String name) {
		//Only the name of the scene is not a default value.
		_name = name;
		_ambientLight = new AmbientLight(new Color(), 1);
		_background = new Color(0, 0, 0);
		_geometries = new Geometries(new Material(0,0,0,0,0,0,0), null);
		_lights = new ArrayList<LightSource>();
		_camera = new Camera(Point3D.zero, new Vector(0, -1, 0), new Vector(0, 0, -1), 3);
		_distance = 1;
		_maxReflectedDistance = 100;
	}
	
	/**
	 * Copy constructor for a deep copy of an Scene object.
	 * @param other The object that is being copied.
	 */
	public Scene(Scene other) {
		_name = other._name;
		_ambientLight = new AmbientLight(other._ambientLight);
		_background = new Color(other._background);
		_geometries = other._geometries;
		_lights = other._lights;
		_camera = new Camera(other._camera);
		_distance = other._distance;
		_focalPlane = new Plane(other._focalPlane);
		_maxReflectedDistance = other._maxReflectedDistance;
	}
	
	/**
	 * Function that sets the ambient 
	 * light that infect the whole scene
	 * @param r The basic red color of the ambient light.
	 * @param g The basic red color of the ambient light.
	 * @param b The basic red color of the ambient light.
	 * @param ka represents how much of the intensity 
	 * of the background ambient light we have.
	 */
	public void setAmbientLight(int r, int g, int b, double ka) {
		_ambientLight = new AmbientLight(new Color(r, g, b), ka);
	}
	
	/**
	 * Function that sets the background 
	 * color that the scene has.
	 * @param background The parameter with which we paint the background,
	 * not the object in it, of the scene.
	 */
	public void setBackground(Color background) {
		_background = new Color(background);
	}
	
	/**
	 * Function that sets the camera that is in the scene.
	 * @param P0 The P0 of the camera.
	 * @param up The upward vector from the camera.
	 * @param to The forward vector from the camera.
	 * @param shutter The radius of the shutter.
	 */
	public void setCamera(Point3D P0, Vector up, Vector to, double shutter) {
		_camera = new Camera(P0, up, to, shutter);
	}

	/**
	 * Function that sets the distance of the camera that is in the scene
	 * from the view-plane.
	 * @param distance The distance that will bet set as the 
	 * distance of the camera that is in the scene
	 * from the view-plane.
	 */
	public void setDistance(double distance) {
		_distance = distance;
	}
	
	/**
	 * Function that sets the geometries that will be
	 * in the scene.
	 * @param geometries The geometry that contains a list of geometries
	 * that will be in the scene.
	 */
	public void setGeomtries(Geometries geometries) {
		_geometries = geometries;
	}
	
	/**
	 * Function that sets the light sources that will be
	 * in the scene.
	 * @param lights The list of all the light sources that
	 * are in the scene.
	 */
	public void setLights(List<LightSource> lights) {
		_lights = lights;
	}
	
	/**
	 * The function gets the distance from the 
	 * @param distance The distance form the view plane
	 * to the focal plane.
	 */
	public void setFocalPlane(double distance) {
		if (distance <= 0) {
			throw new IllegalArgumentException("focal plane distance cannot be behind view plane.");
		}
		Point3D point = new Vector(_camera.getP0())
				.add(new Vector(_camera.getVTo()
						.normalizedVector().scale(_distance + distance))).getHead();
		_focalPlane = new Plane(point, new Vector(_camera.getVTo()), new Material(), null);
	}
	
	/**
	 * Setter for the numFocusRays attribute.
	 * @param numFocusRays The number of rays that would be sent through
	 * each pixel for the 'depth of field' effect.
	 */
	public void setNumFocusRays(int numFocusRays) {
		_camera.setNumFocusRays(numFocusRays);
	}
	
	/**
	 * Setter for the MaxReflectedDistance attribute.
	 * @param max The distance
	 */
	public void setMaxReflectedDistance(double max) {
		_maxReflectedDistance = max;
	}
	
	public double getMaxReflectedDistance() {
		return _maxReflectedDistance;
	}
	
	/**
	 * Getter for the ambient light of the whole scene 
	 * that is represented by the ambientLight field.
	 * @return A new AmbientLight object that represents the 
	 * ambient light of this scene.
	 */
	public AmbientLight getAmbientLight() {
		return new AmbientLight(_ambientLight);
	}
	
	/**
	 * Getter for the background color of the scene.
	 * This background color infects the color that the scene has where is no 
	 * other object.
	 * @return The background color of the scene that infects the color 
	 * that the scene has where is no other object.
	 */
	public Color getBackground() {
		return new Color(_background);
	}
	
	/**
	 * Getter for the camera that is in this scene.
	 * @return A new Camera object that represents the camera that is 
	 * in this scene.
	 */
	public Camera getCamera() {
		return new Camera(_camera);
	}
	
	/**
	 * Getter for the distance of the camera that is in this scene
	 * from the view-plane.
	 * @return The distance (double) that represents 
	 * the distance of the camera that is in this scene
	 * from the view-plane.
	 */
	public double getDistance() {
		return _distance;
	}
	
	/**
	 * Getter for the geometries that is in this scene.
	 * @return The instance itself! every change in the returned 
	 * value will affect the original value and visa versa.
	 */
	public Geometries getGeometries() {
		return _geometries;
	}
	
	/**
	 * Getter for the list of all the light that are in 
	 * this scene.
	 * @return The list that contains all the lights that 
	 * are in this scene.
	 */
	public List<LightSource> getLights(){
		return _lights;
	}
	
	/**
	 * Getter for the name of this scene. that is, the name 
	 * field of this scene.
	 * @return The name of this scene.
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Getter for the focal plane.
	 * @return A new plane that represents the focal plane.
	 */
	public Plane getFocalPlane() {
		return new Plane(_focalPlane);
	}
	
}
