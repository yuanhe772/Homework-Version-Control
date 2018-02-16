/** Last Update: 02/15/2018; Author: Yuan He (yh772); Platform: Eclipse, Java8 */


public class FloatOverflow {

	public static void multiply10() {

		float init = (float) 1.2345678;
		float prev = 0;
		float f = init;
		int n = 0;
		int weight = 10;

		while(f!=(f/weight)) {
			prev = f;
			f = f*weight;
			n++;
		}
		System.out.println("\nFloat number " + init + " multiplies with 10^n, "
				+ "overflows at n = "+ (n-1) + ", where this float number = " + prev );

	}
	
	
	
}
