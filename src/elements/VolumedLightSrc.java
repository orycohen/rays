package elements;

import java.util.ArrayList;

import primitives.Color;

public abstract class VolumedLightSrc extends Light implements LightSource {
	
	/**
	 * Define the number of samples that are checked for every shadow ray 
	 */
	static protected int NUMBER_OF_SAMPLES = 1;
	
	/**
	 * A list of scalar pairs for calculating the movement vectors 
	 * from the center of the light source. 
	 * The usage is for checking the shadow
	 */
	public ArrayList<double[]> Scalars;
	
	/**
	 * Constructor
	 * @param color The color of the light
	 */
	public VolumedLightSrc(Color color) {
		super(color);
	}
	/**
	 * Getter for the light's radius
	 * @return The light's radius if the light source has a radius, else return 0 
	 */
	abstract public double getRadius();
	
	/**
	 * Function for setting up the samples of points .
	 * @param numberOfSamples The number of samples that will be checked 
	 * for every ray that is sent to this light source.
	 */
	abstract protected void setSamples(int numberOfSamples);
	
	/**
	 * Getter for the number of samples that are in this volumed light source.
	 * @return The number of samples that are in this volumed light source.
	 */
	abstract public int getNumOfSamples();
}
