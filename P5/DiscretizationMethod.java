
/**
 * DiscretizationMethod.java, ECE4960-P5
 * Created by Yuan He(yh772) on 2018/05/16
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 */

/**@Interface: provide method handler for different discretizating rules*/
interface ruleType {
	public double[] A1A2B1B2(double h, double D, double dt);
}

/**@Protected_Class: */
class backward implements ruleType {

	/**Function: Construct the A1, A2, B1, B2 for further constructing matrix A and B with backward Euler rule
	 * @param: double h, double D, double dt
	 * @return: [A1, A2, B1, B2]*/
	@Override
	public double[] A1A2B1B2(double h, double D, double dt) {
		// construct A1, A2, B1, B2 according to trapezoid rule
		double A1 = -D/(h*h);
		double A2 = 1.0/dt + 2.0*D/(h*h);
		double B1 = 0;
		double B2 = 1.0/dt;
		double[] res = {A1, A2, B1, B2};
		return res;
	}
}

/**@Protected_Class: */
class trapezoid implements ruleType {

	/**Function: Construct the A1, A2, B1, B2 for further constructing matrix A and B with trapezoid Euler rule
	 * @param: double h, double D, double dt
	 * @return: [A1, A2, B1, B2]*/
	@Override
	public double[] A1A2B1B2(double h, double D, double dt) {
		// construct A1, A2, B1, B2 according to trapezoid rule
		double A1 = -D/(2*h*h);
		double A2 = 1.0/dt + D/(h*h);
		double B1 = D/(2.0*h*h);
		double B2 = 1.0/dt - D/(h*h);
		double[] res = {A1, A2, B1, B2};
		return res;
	}
}

