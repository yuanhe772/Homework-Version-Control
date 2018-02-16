/** Last Update: 02/15/2018; Author: Yuan He (yh772); Platform: Eclipse, Java8 */


public class Signed0 {

	static float p0 = 1/FloatINF.INF;
	static float n0 = 1/FloatINF.NINF;
	static int flag4 = 0;
	static int flag5 = 0;

	public static void check0() {

		try {
			if( 1/p0 < 0 || Math.abs(p0) != 0) {
				flag4 = 1;
				throw new Exception("3");
			}
			if( 1/n0 > 0 || Math.abs(p0) != 0) {
				flag5 = 1;
				throw new Exception("4");
			}
		}catch(Exception e) {
			if(e.getMessage().toString().equals("4")) {
				System.out.println("\nPlease regenerate +0");
			}
			else if(e.getMessage().toString().equals("5")) {
				System.out.println("\nPlease regenerate -0");
			}
		}
	}

	public static void log0() {
		if(flag4 == 1 || flag5 == 1) 
			return;
		System.out.println("\nlog(+0) = " + Math.log(p0));
		System.out.println("log(-0) = " + Math.log(n0));
		System.out.println("log(0) = " + Math.log(0));
	}

	public static void sinx_x() {
		if(flag4 == 1 || flag5 == 1) 
			return;
		System.out.println("\nsin(+0)/(+0) = " + Math.sin(p0)/p0);
		System.out.println("sin(-0)/(-0) = " + Math.sin(n0)/n0);
		System.out.println("sin(-0)/|-0| = " + Math.sin(n0)/Math.abs(n0));
	}
}
