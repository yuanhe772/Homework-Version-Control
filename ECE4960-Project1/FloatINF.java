/** Last Update: 02/12/2018; Author: Yuan He (yh772); Platform: Eclipse, Java8 */


public class FloatINF {
	public static void floatINF() {
		float pi = (float) (1.0/0.0);
		float ni = (float) (-1.0/0.0);

		Print.String("Generate INF");
		Print.Float(pi);
		Print.String("Generate NINF");
		Print.Float(ni);

		Print.String("1/INF");
		Print.Float(1/pi);
		Print.String("1/NINF");
		Print.Float(1/ni);

		Print.String("sin(INF)");
		Print.Float((float) Math.sin(pi));
		Print.String("sin(NINF)");
		Print.Float((float) Math.sin(ni));

		Print.String("exp(INF)");
		Print.Float((float) Math.exp(pi));
		Print.String("exp(NINF)");
		Print.Float((float) Math.exp(pi));
		
		//Interactions between INF and NINF:
		



	}
}
