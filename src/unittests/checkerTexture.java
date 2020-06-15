package unittests;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import elements.LightSource;
import elements.PointLight;
import geometries.Geometries;
import geometries.Sphere;
import primitives.Axis;
import primitives.Color;
import primitives.ColorEmissionImp;
import primitives.Coordinate;
import primitives.Material;
import primitives.Point3D;
import primitives.Ray;
import primitives.SphericalTextureImp;
import primitives.Texture;
import primitives.Vector;
import renderer.ImageWriter;
import renderer.Renderer;
import scene.Scene;

class checkerTexture {

	@Test
	void test() {
		Scene scene = new Scene("scene");
		
		List<LightSource> lights = new ArrayList<LightSource>();
		
		Geometries geometries = new Geometries(new Material(), null);
		
		scene.setCamera(
				new Point3D(0, 1100, 3200),
				new Vector(0, 1, 0),
				new Vector(0, 0, -1),
				0);
		
		scene.setNumFocusRays(1);
		
		scene.setDistance(3180);
		
		scene.setGeomtries(geometries);
		
		scene.setLights(lights);
		
		scene.setFocalPlane(20 + 430*3);
		
		scene.setBackground(new Color(10,10,10));
		
		scene.setMaxReflectedDistance(4000);
		
		lights.add(new PointLight(
				Color.white,
				new Point3D(0, 3600, -6000), 
				56, 0.00001, 0.000000001, 0.00000005));
				
		geometries.addGeometry(new geometries.Rectangle(
										new Point3D(-4000,0,0), 
										new Point3D(4000,0,0), 
										new Point3D(-4000,0,-16000),  
										new Material(0.3, 0.15, 80, 0, 0, 1, 0.26),
										new ColorEmissionImp(new Color(103, 93, 80))));
		geometries.addGeometry(new geometries.Rectangle(
										new Point3D(-4000,0,0), 
										new Point3D(-4000,0,-16000), 
										new Point3D(-4000,5000,0),  
										new Material(0.3, 0.15, 80, 0, 0, 1, 0.26),
										new ColorEmissionImp(new Color(103, 93, 80))));
		geometries.addGeometry(new geometries.Rectangle(
										new Point3D(-4000,0,-16000), 
										new Point3D(4000,0,-16000), 
										new Point3D(-4000,5000,-16000),  
										new Material(0.3, 0.15, 80, 0, 0, 1, 0.26),
										new ColorEmissionImp(new Color(103, 93, 80))));
		geometries.addGeometry(new geometries.Rectangle(
										new Point3D(4000,0,-16000), 
										new Point3D(4000,0,0), 
										new Point3D(4000,5000,-16000), 
										new Material(0.3, 0.15, 80, 0, 0, 1, 0.26),
										new ColorEmissionImp(new Color(103, 93, 80))));
		
		double Kr = 0, Kt = 0, Tq = 1, Rq = 0;
		
		Texture texture = new Texture(
				new Color(100, 20, 20), 
				new Color(20, 100, 20), 
				new Color(20, 20, 100), Math.PI/25, Math.PI/160);
		texture.setOutlineIntensity(true);
				
		Sphere s1 = new Sphere(
				50, 
				new Point3D(-600, 52, -1430*5), 
				new Material(0.35, 0.65, 85, Kr, Kt, Tq, Rq),
				new SphericalTextureImp(50, texture, new Axis(new Point3D(-600, 52, -1430*5)))),
				s2 = new Sphere(
						50, 
						new Point3D(-500, 52, -1070*5), 
						new Material(0.35, 0.65, 85, Kr, Kt, Tq, Rq),
						new SphericalTextureImp(50, texture, new Axis(new Point3D(-500, 52, -1070*5)))),
				s3 = new Sphere(
						50, 
						new Point3D(-650, 52, -800*5), 
						new Material(0.35, 0.65, 85, Kr, Kt, Tq, Rq),
						new SphericalTextureImp(50, texture, new Axis(new Point3D(-650, 52, -800*5)))),
				s4 = new Sphere(
						200, 
						new Point3D(-800, 201, -130*5), 
						new Material(0.35, 0.65, 85, Kr, Kt, Tq, Rq),
						new SphericalTextureImp(200, texture, new Axis(new Point3D(-800, 201, -130*5)))),
				s5 = new Sphere(
						450, 
						new Point3D(-1680, 451, -1660*5), 
						new Material(0.35, 0.65, 85, Kr, Kt, Tq, Rq),
						new SphericalTextureImp(450, texture, new Axis(new Point3D(-1680, 451, -1660*5)))),
				s6 = new Sphere(
						400, 
						new Point3D(800, 401, -430*5), 
						new Material(0.15, 0.25, 85, Kr, Kt, Tq, Rq),
						new SphericalTextureImp(400, texture, new Axis(new Point3D(800, 401, -430*5)))),
				s7 = new Sphere(
						330, 
						new Point3D(30, 331, -670*5), 
						new Material(0.35, 0.65, 85, Kr, Kt, Tq, Rq),
						new SphericalTextureImp(330, texture, new Axis(new Point3D(30, 331, -670*5)))),
				s8 = new Sphere(
						700, 
						new Point3D(1550, 701, -1830*5), 
						new Material(0.15, 0.25, 85, Kr, Kt, Tq, Rq),
						new SphericalTextureImp(700, texture, new Axis(new Point3D(1550, 701, -1830*5)))),
				s9 = new Sphere(
						430, 
						new Point3D(-270, 431, -1870*5), 
						new Material(0.35, 0.65, 85, Kr, Kt, Tq, Rq),
						new SphericalTextureImp(430, texture, new Axis(new Point3D(-270, 431, -1870*5))));
				
		geometries.addGeometry(s1);
		geometries.addGeometry(s2);
		geometries.addGeometry(s3);
		geometries.addGeometry(s4);
		geometries.addGeometry(s5);
		geometries.addGeometry(s6);
		geometries.addGeometry(s7);
		geometries.addGeometry(s8);
		geometries.addGeometry(s9);

		
		ImageWriter imageWriter = new ImageWriter(
				"Bridge",
				2000, 2000, 1000, 1000);
		Renderer render = new Renderer(imageWriter, scene);
		render.renderImage();
		render.writeToImage();
		BigInteger ColorCounter = Color.getNewCounter().getNumber(),
				   CoordinateCounter = Coordinate.getNewCounter().getNumber(),
				   Point3dCounter = Point3D.getNewCounter().getNumber(),
				   RayCounter = Ray.getNewCounter().getNumber(),
				   VectorCounter = Vector.getNewCounter().getNumber();
		BigInteger AllNewCallsSum = ColorCounter.add(CoordinateCounter)
							.add(Point3dCounter)
							.add(RayCounter)
							.add(VectorCounter);

		System.out.println(AllNewCallsSum);
	}
}
