import java.util.Arrays;

/**
 * Test.java, ECE4960-P4
 * Created by Yuan He(yh772) on 2018/04/18
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 * 
 * P4, Test script:
 * Testings for basic functions, and validations for ODE solvers
 */
public class Test {

	/**Function: Validate ODE solvers, using the example in class
	 * 			Including RK34 with adaption, RK4, and Forward-Euler solver
	 * @param: None
	 * @return: None*/
	public static void validateODESolvers() {
		/* Testing data initializing*/
		double xx0[] = {2};
		Vector x0 = new Vector(xx0);
		String fType[] = {"ODE Validation"};
		double h = 1;
		Vector x1 = new Vector(x0.len);

				/* Testing RK34 */
				System.out.println("\nTesting RK34 with the in-class example");
				for(double t=0; t<4 ; t++) {
					// Estimate next x: extract x(RK4) out of xRK3_xRK4
					x1 = Solvers.RK34AdaptiveH(x0, t, h, fType);
					// Update the initial x
					x0.v = x1.v.clone();
					System.out.print(Arrays.toString(x1.v) + "  Error% = " + Solvers.relativeErr(x1, t, fType)+"\n");
				}

//		/* Testing RK34 */
//		System.out.println("\nTesting RK34 with the in-class example");
//		for(double t=0; t<4 ; t++) {
//			// Estimate next x: extract x(RK4) out of xRK3_xRK4
//			x1 = refe.RK34AdaptiveH(x0, t, h, fType);
//			// Update the initial x
//			x0.v = x1.v.clone();
//			System.out.print(Arrays.toString(x1.v) + "  Error% = " + Solvers.relativeErr(x1, t, fType)+"\n");
//		}

		/* Testing RK4 */
		System.out.println("\nTesting RK4 with the in-class example");
		x0 = new Vector(xx0);
		for(double t=0; t<4 ; t++) {
			// Estimate next x: extract x(RK4) out of xRK3_xRK4
			x1 = Solvers.xRK3_xRK4(x0, t, h, fType).get(1);
			// Update the initial x
			x0.v = x1.v.clone();
			System.out.print(Arrays.toString(x1.v) + "  Error% = " + Solvers.relativeErr(x1, t, fType)+"\n");
		}

		/* Testing Forward Euler */
		System.out.println("\nTesting Forward-Euler with the in-class example");
		x0 = new Vector(xx0);
		for(double t=0; t<4; t++) {
			// Estimate next x:
			x1 = Solvers.forwardEuler(x0, t, h, fType);
			// Update the initial x
			x0.v = x1.v.clone();
			System.out.print(Arrays.toString(x1.v) + "  Error% = " + Solvers.relativeErr(x1, t, fType)+"\n");
		}
	}
}
