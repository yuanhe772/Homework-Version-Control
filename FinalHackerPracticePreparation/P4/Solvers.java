import java.util.ArrayList;

/**
 * Solvers.java, ECE4960-P4
 * Created by Yuan He(yh772) on 2018/04/17
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 * 
 * P4, implement ODE solvers:
 * Implements ODE solvers, including forward Euler, RK4, and RK34 with time adaption
 */
public class Solvers {

	/* Class Variants: */
	static final double eR = 1e-4;
	static final double eA = 1e-7;
	static final double Tol1 = 1e-2;
	static final double Tol2 = 1e-6;

	/* Class Methods: */
	/**Function: Invoke the ODE solver functions by indicating which method to use and the ODE functions
	 * @param: Vector x0, String solverType, String[] ODEType, double start, double end, double stepSize
	 * @return: None
	 * @throws Exception */
	public static void solve(Vector x0, String solverType, String[] ODEType, double start, double end, double stepSize) throws Exception {

		// Construct new x
		Vector x1 = new Vector(x0.len);

		// Step-wise estimation starts
		for(double t=start; t<end ; t += stepSize) {
			// Estimate next x
			if(solverType.equals("RK34"))
				x1 = RK34AdaptiveH(x0, t, stepSize, ODEType);
			else if(solverType.equals("RK3"))
				x1 = xRK3_xRK4(x0, t, stepSize, ODEType).get(0);
			else if(solverType.equals("RK4"))
				x1 = xRK3_xRK4(x0, t, stepSize, ODEType).get(1);
			else if(solverType.equals("FE"))
				x1 = forwardEuler(x0, t, stepSize, ODEType);
			else throw new Exception("Please indicate correct form of ODE solver methods!");

			// Update initial x
			x0.v = x1.v.clone();
			System.out.println(x1.v[0] + "\t" + x1.v[1]);
		}
	}

	/**Function: Calculate the values of K for RK3, and RK4
	 * @param: Vector xi, double ti, double h, String fxType
	 * @return: ArrayList<Vector> K = [K1, K2, K3, K4], where each K
	 * 			contains two elements, respectively for V1 and V2
	 * @throws Exception */
	public static ArrayList<Vector> K(Vector x, double t, double h, String[] fType) throws Exception {
		ArrayList<Vector> K = new ArrayList<Vector>();
		K.add(new Vector(x.len));
		K.add(new Vector(x.len));
		K.add(new Vector(x.len));
		K.add(new Vector(x.len));

		// Update K1
		for (int i=0; i<x.len; i++) {
			K.get(0).v[i] = Model.f(x, t, fType[i]);
		}
		// Update K2
		for(int i=0; i<x.len; i++) {
			K.get(1).v[i] = Model.f(x.add(K.get(0), 0.5*h), t + 0.5*h, fType[i]);
		}
		// Update K3
		for(int i=0; i<x.len; i++) {
			K.get(2).v[i] = Model.f(x.add(K.get(1), 3.0/4*h), t + 3.0/4*h, fType[i]);
		}
		// Update K4
		for(int i=0; i<x.len; i++) {
			K.get(3).v[i] = Model.f(x.add(K.get(2), h), t + h, fType[i]);
		}
		return K;
	}

	/**Function: Calculate x[] by RK3 and RK4
	 * 			if you want the result of method RK3, choose the first element of return value
	 * 			if you want the result of method RK4, choose the second element of return value
	 * @param: Vector x, double t, double h(step length), String[] fType
	 * @return: ArrayList<Vector> X = [X(RK3), X(RK4)]
	 * @throws Exception */
	public static ArrayList<Vector> xRK3_xRK4(Vector x, double t, double h, String[] fType) throws Exception {
		ArrayList<Vector> K = K(x, t, h, fType);
		Vector K1 = K.get(0);
		Vector K2 = K.get(1);
		Vector K3 = K.get(2);
		Vector K4 = K.get(3);

		// Calculate x(RK3)
		Vector KsumRK3 = ((K1.scale(2.0)).add(K2, 3.0)).add(K3, 4.0);
		Vector x_i1_RK3 = x.add(KsumRK3, h*1.0/9.0);

		// Calculate x(RK4)
		Vector KsumRK4 = (((K1.scale(7.0)).add(K2, 6.0)).add(K3, 8.0)).add(K4, 3.0);
		Vector x_i1_RK4 = x.add(KsumRK4, h*1.0/24.0);

		// Return RK3, RK4 together as an ArrayList
		ArrayList<Vector> xRK3_xRK4 = new ArrayList<Vector>();
		xRK3_xRK4.add(x_i1_RK3);
		xRK3_xRK4.add(x_i1_RK4);
		return xRK3_xRK4;
	}

	/**Function: Calculate x[] by RK34 with adaptive h
	 * @param: Vector x, double t, double h(step length), String fType[]
	 * @return: 
	 * @throws Exception */
	public static Vector RK34AdaptiveH(Vector x, double t, double h, String fType[]) throws Exception {
		// Initialize the method data structures
		ArrayList<Vector> xRK3_xRK4 = new ArrayList<Vector>(x.len);
		double r = 0;
		double step = 0;
		double stepLimit = h;

		// Within each time-step
		while(step < stepLimit) {
			xRK3_xRK4 = xRK3_xRK4(x, t, h, fType);
			r = r(xRK3_xRK4);
			// Adapt the h
			while(Math.abs(r-1) > Tol1 || Math.abs(r-1) < Tol2) {
				h /= Math.pow(r, 1.0/3.0); 
				if(h+step > stepLimit) 
					break;
				xRK3_xRK4 = xRK3_xRK4(x, t, h, fType);
				r = r(xRK3_xRK4);
			}
			// Update the x_i0
			t += h;
			step += h;
			x.v = xRK3_xRK4.get(1).v.clone();
			h = stepLimit - step;
		}
		return x;
	}

	/**Function: Calculate the normalized estimated error, r = (xRK3 - xRK4) / (eR*xRK4 + eA)
	 * @param: ArrayList<Vector> [x(RK3), x(RK4)]
	 * @return: double r*/
	public static double r(ArrayList<Vector> xRK3_xRK4) {
		Vector xRK3 = xRK3_xRK4.get(0);
		Vector xRK4 = xRK3_xRK4.get(1);
		Vector E = xRK3.add(xRK4, -1);
		double r = E.norm() / (xRK4.scale(eR).norm() + eA);
		return r;
	}

	/**Function: Calculate x[] by Forward-Euler
	 * @param: Vector x, double t, double h(step length), String fType[]
	 * @return:
	 * @throws Exception */
	public static Vector forwardEuler(Vector x, double t, double h, String fType[]) throws Exception {
		Vector f = new Vector(x.len);
		for(int i=0; i<x.len; i++) {
			f.v[i] = Model.f(x, t, fType[i]);
		}
		x = x.add(f, h);
		return x;
	}

	/**Function: Calculate the normalized relative error, ||Error%||2:
	 * 			By computing the relative error of each unknown variable, 
	 * 			and take the 2nd-order norm of their sum
	 * @param: Vector x, double t, String[] fType
	 * @return: double ||Error%||2 */
	public static double relativeErr(Vector x, double t, String[] fType) {
		double truth = 0;
		double sum = 0;

		// Computing each unknown variable's 2nd-order norm
		for(int i=0; i<x.len; i++) {
			truth = Model.trueX(t+1, fType[i]);
			sum += Math.pow((truth - x.v[i])/truth, 2);
		}
		return Math.pow(sum, 0.5);
	}
}
