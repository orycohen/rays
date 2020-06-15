package util;

import java.util.ArrayList;

import geometries.BBox;
import geometries.Geometry;
import primitives.Ray;
/**
 * Implementation of a binary tree that holds 
 * bounding box as keys.
 */
public class binaryTree { //In our case, T1 would be a geometry or a bounding box.
	
	private static Node help1;
	private static Node help2;
	
	private class Node {
		
		public Geometry geometry;
		public BBox _box;
		public Node _right;
		public Node _left;
				
		public Node (Geometry g, BBox box, Node r, Node l) {
			geometry = g;
			_box = box;
			_right = r;
			_left = l;
		}
	}
	
	public Node _root;
		
	public binaryTree(ArrayList<Geometry> lst) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		for (Geometry g : lst) {
			nodes.add(new Node(g, g.getBoundingBox(),null,null));
		}
		_root = recursiveTree(nodes);
	}
	
	private Node recursiveTree(ArrayList<Node> nodes){
		if (nodes.size() == 0)
			return null;
		if (nodes.size() == 1)
			return nodes.get(0);
		double minDis = Double.MAX_VALUE, temp;
		for (Node node1 : nodes) {
			for (Node node2 : nodes) {
				if (node1 == node2)
					continue;
				temp = node1._box.getCenter().distance(node2._box.getCenter());
				if (temp < minDis) {
					help1 = node1;
					help2 = node2;
					minDis = temp;
				}	
			}
		}
		
		nodes.remove(help1);
		nodes.remove(help2);
		nodes.add(new Node(null, help1._box.union(help2._box), help1, help2));
		return recursiveTree(nodes);
	}
	
	public ArrayList<Geometry> minListGeometries(Ray ray) {
		ArrayList<Geometry> toReturn = new ArrayList<Geometry>();
		fillGeometryList(ray, toReturn, _root);
		return toReturn;
	}

	private void fillGeometryList
	(Ray ray, ArrayList<Geometry> lst, Node node) {
		if (node.geometry != null) {
			if (node._box.isIntersect(ray))
				lst.add(node.geometry);
			return;
		}
		if (node._box.isIntersect(ray)) {
			fillGeometryList(ray, lst, node._left);
			fillGeometryList(ray, lst, node._right);
		}
	}
}
