/** Last Update: 02/12/2018; Author: Yuan He (yh772); Platform: Eclipse, Java8 */


public class FloatOverflow {
	public static void floatOverflow() {
		
		Print.String("(1) multiplies pow(10,i)");
		float f=0;
		for(int i=0; i<41; i++) {
			f = (float) (1.23456789012345 * Math.pow(10, i));
			Print.Float(f);
		}
		
		//Print.String("(2) ");

	}
}
