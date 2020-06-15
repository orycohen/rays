
package geometries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import primitives.IGetEmission;
import primitives.Material;
import primitives.Point3D;
import primitives.Ray;
import util.binaryTree;

/**
 * Define a new shape that composed of number of shapes.
 * The class has a list of geometries that represents the 
 * geometries that this class represents
 */
public class Geometries extends Geometry {
	

	/**
	 * A list of points that this object represents
	 */
	private ArrayList<Geometry> _shapes = new ArrayList<Geometry>();
	
	/**
	 * Contains the geometries, after we added them all, in a form of
	 * a Bounding Volume Hierarchy tree.
	 */
	private binaryTree _BVH_Tree;
	
	/**
	 * Constructor for setting a default set of geometries.
	 * @param emissionImp The IGetEmission implementor
	 * @param material The material of the geometry
	 */
	public Geometries(Material material, IGetEmission emissionImp) {
		super(material, emissionImp);
	}

	/**
	 * return all the intersections of the ray with the shapes
	 */
	@Override
	public List<Point3D> findIntersections(Ray ray) {
		List<Point3D> points = new ArrayList<Point3D>();
		
		for (Geometry geometry : _shapes) {
			points.addAll(geometry.findIntersections(ray));
		}
		
		return points;
	}
	
	/**
	 * Finds the intersections of the given ray with every 
	 * geometry in the _shapes list of geometries, and puts in 
	 * a map the geometry as a key and the intersections with this 
	 * geometry as a value of the key.
	 * @param ray The ray with which the function finds the intersections.
	 * @return A map that contains a list of geometries as keys, and list of intersections as a value to every key
	 */
	public Map<Geometry, List<Point3D>> findMapOfIntersections(Ray ray){
		Map<Geometry, List<Point3D>> intersectionPoints = 
				new HashMap<Geometry, List<Point3D>> ();
		List<Point3D> points;
		for (Geometry geometry : _BVH_Tree.minListGeometries(ray)) {
			points = geometry.findIntersections(ray);
			if (points.size() != 0)
				intersectionPoints.put(geometry, points);
		}
		return intersectionPoints;
	}
	
	/**
	 * Adding a new geometry to the list of this Geometries.
	 * If the given Geometry is null, nothing will be added.
	 * @param geometry The Geometry that we want to add to the list of this Geometries.
	 */
	public void addGeometry(Geometry geometry) {
		if (geometry == null)
			return;
		_shapes.add(geometry);
	}

	/**
	 * Function for creating the tree once we 
	 * done adding geometries.
	 * This function creates the Bounding Volume Hierarchy tree
	 * that helps us reducing the number of intersections-checking
	 * every time we send a ray to find intersections.
	 */
	public void createTree() {
		_BVH_Tree = new binaryTree(_shapes);
	}
	
	/**
	 * No implementation for this class.
	 */
	@Override
	protected BBox createBoundingBox() {
		return null;
	}
}
