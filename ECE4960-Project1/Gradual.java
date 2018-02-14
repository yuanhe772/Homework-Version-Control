/** Last Update: 02/14/2018; Author: Yuan He (yh772); Platform: Eclipse, Java8 */


public class Gradual {
	public static void gradual() {
		double x = 4 * Math.pow(10, -323);
		double y = 3 * Math.pow(10, -323);
		Print.Double(x);
		Print.Double(y);
		Print.Double(x-y);
		Print.Double(x/y);
		double z = 1.0 * Math.pow(10, -323); // the bottom for gradual
		// In iEEE-754, the bottom for gradual underflow is 4.9*10^(-324)
		// but through observation, the bottom for gradual underflow is 1.0*10^(-323)
		
		// to make z to be as close as possible to 0, let z be the smallest denormalized 
		// number in Java, which is 1.0 * 10^-323 (10 to the power of 323)
		Print.Double(z);
		Print.Double(Math.sin(1.23456789012345 * z)/z); //Correct according to L'Hopital's Rule
	}
}
