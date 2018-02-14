/** Last Update: 02/12/2018; Author: Yuan He (yh772); Platform: Eclipse, Java8 */


public class FloatNaN {
	public static void floatNaN() {

		Print.String("(1) generating NaN through illegal calculation");
		float n1= Float.POSITIVE_INFINITY - Float.POSITIVE_INFINITY;
		float n2= Float.POSITIVE_INFINITY * 0;
		float n3= Float.POSITIVE_INFINITY / Float.POSITIVE_INFINITY;
		float n= Float.POSITIVE_INFINITY % 1; // /will still generate INF, but % is NaN

		Print.Float( n );
		Print.String("NaN*0");
		Print.Float( n * 0 );
		Print.String("NaN + NaN");
		Print.Float( n + n );

		Print.String("(2) detecting NaN");
		if(n != n) 
			{Print.String("NaN != NaN, therefore NaN detected");}
		
	}
}
