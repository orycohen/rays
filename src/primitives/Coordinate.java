
package primitives;

import util.Counter;

/**
 * Class that has a number that represent a coordinate.
 * This Class is the base of the Point3D and Point2D classes.
 * They are not extends this class, but rather has it as a field.
 */
public class Coordinate {
		
	private static Counter counter = new Counter();
	public static Counter getNewCounter() {return counter;}
	
	/**
	 * the number that represent the coordinate
	 */
	private final double _coordinate;
	
	/**
	 * Constructor for creating a new coordinate, a number, that is
	 * represented by this class
	 * @param num the number that will represent the coordinate
	 */
	public Coordinate(double number) {
		
		//if the absolute value of the number is too 
		//small, put 0 in _coordinate
		if (this.getExponent(number) < ACCURACY)
			_coordinate = 0.0;
		else 
			_coordinate = number;
		counter.increase();
	}
	
	/** 
	 * Copy constructor for copying an object of this class.
	 * Actually, there is no deep copy here since the field 
	 * _coordinate is a primitive type.
	 * @param other The object that being copied
	 */
	public Coordinate(Coordinate other) {
		_coordinate = other._coordinate;
		counter.increase();
	}
		
	/**
	 * Getter for the Coordinate number that is 
	 * represented by this class
	 * @return the coordinate that is 
	 * represented by this class
	 */
	public double getNumber() {
		return _coordinate;
	}
	
	/**
	 * Overrides the equal function
	 * */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Coordinate))
			return false;
		if (this == obj)
			return true;
		Coordinate other = (Coordinate)obj;
		double difference = 
				this.subtract(other.getNumber());
		return difference == 0.0;
	}

	/**overrides the toString function*/
	@Override
	public String toString() {
		return "" + _coordinate;
	}
	
	/**This function adds two coordinates to 
	 * get a new coordinate that is the result of the addition.
	 * @param other the object being added
	 * @return new coordinate object, 
	 * addition of the two coordinates
	 */
	public Coordinate add(Coordinate other) {
		return new Coordinate
				(this.add(other.getNumber()));
	}
	
	/**This function subtracts two coordinates to 
	 * get a new coordinate that is the result of the subtraction.
	 * @param other the object being subtracted
	 * @return new coordinate object, 
	 * subtraction of the two coordinates
	 */
	public Coordinate subtract(Coordinate other) {
		return new Coordinate
				(this.subtract(other.getNumber()));
	}
	
	/**
	 * This function multiplies this coordinate with a scalar to 
	 * get a new coordinate that is the result of the multiplication.
	 * @param scalar The scalar that the function uses for the multiplication
	 * @return a new coordinate that is the result of the multiplication.
	 */
	public Coordinate scale(double scalar) {
		return new Coordinate(_scale(scalar));
	}
	
	/**
	 * This function multiplies this coordinate with another coordinate to
	 * get a new coordinate that is the result of the multiplication.
	 * @param other The other Coordinate that is being used for the
	 * multiplication that the function does.
	 * @return a new coordinate that is the result of the
	 * multiplication that the function does.
	 */
	public Coordinate mult(Coordinate other) {
		return new Coordinate(_scale(other._coordinate));
	}

	//if a double number, let's say x, equals 0.000001,
	// so x's exponent is -20.
	private final int ACCURACY = -20;
	/**
	 * gets a double number that is represented in the 
	 * pattern of (s,eeee....ee,mmm......mmmm) in 64 bits
	 * from right to left:
	 * 		1  s bit: says if the number is positive or negative.
	 * 		11 e bits: the exponent (+1023).
	 * 		52 m bits: the mantisa.
	 * @param number from which we take the exponent
	 * @return the exponent of the number
	 */
	public int getExponent(double number) {
		return (int)((Double.
		doubleToRawLongBits(number) >> 52) & 0x7FFL) - 1023;
	}
	
	/* EXPLAINATION:
	 * the closer the number to zero, the smaller
	 * the exponent goes.
	 * for example: if the number is 100.0 so the 
	 * exponent (the exponent in the computer - 1023) would
	 * be 6, And if the number is 0.000001 (closer to zero)
	 * so the exponent (the exponent in the computer - 1023)
	 * would be -20, and so on. 
	 * */
	
	/**
	 * the function subtracts one number from this
	 * coordinate's number, but ignores a relatively
	 * insignificant result.
	 * @param other the other number
	 * @return the result of the subtraction
	 */
	private double subtract(double other) {
		int thisExponent = getExponent(_coordinate),
			otherExponent = getExponent(other);
		
		//In this case, the absolute value of 'other' number 
		//is much smaller than the absolute value of 'this'
		//number relatively.
		if (otherExponent - thisExponent < ACCURACY)
			return _coordinate;
		
		//In this case, the absolute value of 'this' number 
		//is much smaller than the absolute value of 'other'
		//number	 relatively.
		if (thisExponent - otherExponent < ACCURACY)
			return -other;
		
		double result = _coordinate - other;
		int resultExponent = getExponent(result);
		//if the absolute value of the result is too 
		//small, return 0.
		if (resultExponent - thisExponent < ACCURACY)
			return 0.0;
		else
			return result;
	}
	
	/**
	 * the function adds one number to this
	 * coordinate's number, but ignores a relatively
	 * insignificant result
	 * @param other the other number
	 * @return the result of the subtraction
	 */
	private double add(double other) {
		int thisExponent = getExponent(_coordinate),
			otherExponent = getExponent(other);
		
		//In this case, the absolute value of 'other' number 
		//is much smaller than the absolute value of 'this'
		//number
		if (otherExponent - thisExponent < ACCURACY)
			return _coordinate;
		
		//In this case, the absolute value of 'this' number 
		//is much smaller than the absolute value of 'other'
		//number	
		if (thisExponent - otherExponent < ACCURACY)
			return other;
		
		double result = _coordinate + other;
		int resultExponent = getExponent(result);
		//if the absolute value of the result is too 
		//small, return 0.
		if (resultExponent - thisExponent < ACCURACY)
			return 0.0;
		else
			return result;
	}

	/**
	 * checks if we multiply with a scalar that is very
	 * close to one
	 * @param num the scalar
	 * @return the result
	 */
	private double _scale(double num) {
	    int deltaExp = getExponent(num - 1);
	return deltaExp < ACCURACY ? _coordinate : _coordinate * num; 
	}
}
