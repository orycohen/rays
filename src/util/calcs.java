package util;

/**
 * Class with static functions 
 * for better calculations.
 * This class uses the calculation of the 
 */
public class calcs {
	//if a double number, let's say x, equals 0.000001,
	//so x's exponent is -20.
	private static final int ACCURACY = -20;
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
	public static int getExponent(double number) {
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
	 * Subtraction of two numbers.
	 * the function subtracts num2 from num1.
	 * If one of them is very small or very big relatively 
	 * to the other, then we ignore the subtraction of it
	 * @param num1 The first number
	 * @param num2 The second number
	 * @return The result of the operation: num1 - num2
	 * with the rounding mentioned above: 
	 * If one of them is very small or very big relatively 
	 * to the other, then we ignore the Addition of it.
	 */
	public static double subtract(double num1, double num2) {
			return add(num1 , -num2);
	}
	
	/**
	 * Addition of two numbers.
	 * If one of them is very small or very big relatively 
	 * to the other, then we ignore the addition of it
	 * @param num1 The first number
	 * @param num2 The second number
	 * @return The result of the addition of these two numbers
	 * with the rounding mentioned above: 
	 * If one of them is very small or very big relatively 
	 * to the other, then we ignore the Addition of it.
	 */
	public static double add(double num1, double num2) {
		int thisExponent = getExponent(num1),
			otherExponent = getExponent(num2);
		
		//In this case, the absolute value of 'other' number 
		//is much smaller than the absolute value of 'this'
		//number
		if (otherExponent - thisExponent < ACCURACY)
			return num1;
		
		//In this case, the absolute value of 'this' number 
		//is much smaller than the absolute value of 'other'
		//number	
		if (thisExponent - otherExponent < ACCURACY)
			return num2;
		
		double result = num1 + num2;
		int resultExponent = getExponent(result);
		//if the absolute value of the result is too 
		//small, return 0.
		if (resultExponent - thisExponent < ACCURACY)
			return 0.0;
		else
			return result;
	}
	
	/**
	 * Multiplies the 'number' with the 'multiplier'.
	 * The first argument is the multiplier and the second is the 
	 * multiplied number.
	 * @param multiplier The multiplier of the number. 
	 * @param number The multiplied number.
	 * @return the result of multiplier * number.
	 * If the multiplier is very close to 1, then the 
	 * returned number is the original 'number'.
	 */
	public static double mult(double multiplier, double number) {
	    int deltaExp = getExponent(multiplier - 1);
	    
	return deltaExp < ACCURACY ? number : number * multiplier; 
	}
	
	/**
	 * This function gets a number and checks if this number is
	 * close enough to zero. If it is, the function returns true
	 * and false otherwise.
	 * @param number The number that being checked.
	 * @return True if the number is close enough to zero,
	 * and false otherwise.
	 */
	public static boolean closeToZero(double number) {
		return getExponent(number) < ACCURACY;
	}
}
