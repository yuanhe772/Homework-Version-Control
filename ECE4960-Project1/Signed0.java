/** Last Update: 02/12/2018; Author: Yuan He (yh772); Platform: Eclipse, Java8 */


public class Signed0 {
	public static void signed0() {
		//		for(float x=1; x>(1/ Float.POSITIVE_INFINITY); x/=10) {
		//			Print.Float(x);
		//			Print.Float((float) Math.log(x));
		//		}

		float p0 = 1/Float.POSITIVE_INFINITY;
		float n0 = 1/Float.NEGATIVE_INFINITY;

		Print.String("(1) log(+0)");
		Print.Float((float) Math.log(1/p0));

		Print.String("(2) log(-0)");
		Print.Float((float) Math.log(+1/n0));

		Print.String("Therefore we can induct that log(0) = ");
		Print.Float((float) Math.log(0));

		Print.String("(3) sin(+0) / (+0)");
		Print.Float((float) Math.sin(p0)/p0);

		Print.String("(4) sin(-0) / (-0)");
		Print.Float((float) Math.sin(n0)/n0);

		Print.String("(5) sin(-0) / |0|");
		Print.Float((float) (0/Math.sin(0)));

		//		Print.String("(5.1) 0 / 0");
		//		try{//int x = (int) (0.0/0.0);
		//			System.out.println(0/0);}
		//		//Print.Float((float) x);}
		//		catch (ArithmeticException e){Print.String("Divided by 0");}

		// 如果是 0.0/0.0, 0/0.0, 0.0/0, 得到NaN
		// 但是如果是 x/0 (包括0/0) 会产生exception
		// 如果做0/sin(0), java不会产生runtime exception，而是产生NaN

		// 然而通过洛必达法则，sin(0)/0， 应该为1
		
		
	}
}
