package primitives;

/**
 * This class represents a m material 
 * with three factors.
 * The diffusion factor _Kd.
 * The specularity factor _Ks.
 * The shininess factor _Ns
 */
public class Material {
	
	//These two parameters together represent the maximum diffuse angle 
	//of this material object.
	//The larger the angle the more blurry the surface would be.
//-------------------------------------
	
	/**
	 * The distance in the calculations when generating 
	 * random rays.
	 * That is the distance from the point of the original ray
	 * to the point where we move the head of the vector in the ray.
	 */
	private final double CONE_LENGTH = 270;
	
	/**
	 * The radius of the abstract finite cone that blocks the random
	 * rays that are reflected from the material.
	 */
	private final double MAX_RADIUS = 20;
	
	
//-------------------------------------
	
	
	/**
	 * This parameter represents the diffusion factor of the material.
	 */
	private double _Kd;
	
	/**
	 * This parameter represents the specular factor of the material.
	 */
	private double _Ks;
	
	/**
	 * This parameter represents the shininess factor of the material.
	 */
	private int _nShininess;
	
	/**
	 * Represents how much the object is reflective
	 */
	private double _Kr;
	
	/**
	 * Represents the transparency of the object
	 */
	private double _Kt;
	
	/**
	 * For diffuse glass.
	 * Represents how good we see through the material.
	 * The value is the angle for the transparency rays.
	 */
	private double _TranspRadius;
	
	/**
	 * For glossy surfaces.
	 * Represents how good material reflects the light.
	 * The value is the angle for the reflective rays.
	 */
	private double _ReflectRadius;
	
	/**
	 * Default constructor for new Material objects.
	 */
	public Material() {
		_Kd = 0.15;
		_Ks = 0.3;
		_nShininess = 50;
		_Kt = 0;
		_Kr = 0.3;
		_TranspRadius = 0;
		_ReflectRadius = 0.85;
	}
	
	/**
	 * This is the constructor for a new instance of material object.
	 * NOTE -  If the parameters, not shininess, is greater than 1, they are replaced by 1,
	 * and if the parameters are smaller than 0, they are replaced by 0. 
	 * @param Kd The parameter that represents the diffusion factor of the material.
	 * @param Ks The parameter that represents the specularity factor of the material.
	 * @param shininess The parameter that represents the shininess 
	 * factor of the material.
	 * @param Kr Represents how much the object is reflective.
	 * @param Kt Represents the transparency of the object.
	 * @param Tquality Represents how good we see through the material 
	 * (Transparency quality).
	 * @param Rquality Represents how good material reflects the light
	 * (Reflection quality).
	 */
	public Material(double Kd, double Ks, int shininess, double Kr, double Kt, 
			double Tquality, double Rquality) {	
		_Kd = (Kd > 1) ? 1 : ((Kd < 0) ? 0 : Kd);
		_Ks = (Ks > 1) ? 1 : ((Ks < 0) ? 0 : Ks);
		_nShininess = shininess;
		_Kr = (Kr > 1) ? 1 : ((Kr < 0) ? 0 : Kr);
		_Kt = (Kt > 1) ? 1 : ((Kt < 0) ? 0 : Kt);
		_TranspRadius = (1 - ((Tquality > 1) ? 1 : ((Tquality < 0) ? 0 : Tquality))) * MAX_RADIUS;
		_ReflectRadius = (1 - ((Rquality > 1) ? 1 : ((Rquality < 0) ? 0 : Rquality))) * MAX_RADIUS;
	}
	
	/**
	 * A copy constructor for copying an instance of this class
	 * to a new object of this class.
	 * @param material The object that is being copied.
	 */
	public Material(Material material) {
		_Kd = material._Kd;
		_Ks = material._Ks;
		_nShininess = material._nShininess;
		_Kr = material._Kr;
		_Kt = material._Kt;
		_TranspRadius = material._TranspRadius;
		_ReflectRadius = material._ReflectRadius;
	}

	/**
	 * Getter for the diffusion factor of the material.
	 * @return The function returns the factor that represents the
	 * diffusion of the material.
	 */
	public double getKd() {
		return _Kd;
	}
	
	/**
	 * Getter for the specularity factor of the material.
	 * @return The function returns the factor that represents the
	 * specularity of the material.
	 */
	public double getKs() {
		return _Ks;
	}
	
	/**
	 * Getter for the shininess factor of the material.
	 * @return The function returns the factor that represents the
	 * shininess of the material.
	 */
	public int getShininess() {
		return _nShininess;
	}	
	
	/**
	 * Getter for the reflection factor of the material.
	 * @return The function returns the factor that represents the
	 * reflectiveness of the material.
	 */
	public double getKr() {
		return _Kr;
	}
	
	/**
	 * Getter for the transparency factor of the material.
	 * @return The function returns the factor that represents the
	 * transparency of the material.
	 */
	public double getKt() {
		return _Kt;
	}
	
	/**
	 * Getter for the transparency radius.
	 * @return The radius for the transparency rays.
	 */
	public double getTranspRadius() {
		return _TranspRadius;
	}
	
	/**
	 * Getter for the reflection radius.
	 * @return The radius for the reflective rays.
	 */
	public double getReflectRadius() {
		return _ReflectRadius;
	}
	
	/**
	 * Getter for the length of the abstract cone that blocks the rays 
	 * that are reflected from this material. This length
	 * is the distance from the point of the original ray
	 * to the point where we move the head of the vector in the ray.
	 * @return
	 */
	public double getConeLength() {
		return CONE_LENGTH;
	}
}
