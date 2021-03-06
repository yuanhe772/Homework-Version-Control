import java.util.ArrayList;

/**
 * solvers.java, ECE4960-P4
 * Created by Yuan He(yh772) on 2018/04/17
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 * 
 * P4, solvers for
 */
public class Solvers {

	/* Class Variants: */
	static final double eR = 1e-7;
	static final double eA = 1e-4;
	static final double Tol1 = 1e-2;
	static final double Tol2 = 1e-6;

	/* Class Methods: */
	/**Function: The ground truth x(t)
	 * @param: double ti, String fxType
	 * @return: */
	public static double trueX(double t, String fType) {
		if(fType.equals("ODE Validation")) {
			double a = Math.exp(0.8*t)-Math.exp(-0.5*t);
			double b = Math.exp(-0.5*t);
			return (a*4/1.3+b*2);
		}
		else return 0;
	}

	/**Function: f(x,t)
	 * @param: double xi, double ti, String fxType
	 * @return: */
	public static double f(Vector x, double t, String fType) {
		if(fType.equals("sin")) { 
			return -Math.PI * x.v[1];
		}
		else if(fType.equals("cos")) {
			return Math.PI * x.v[0];
		}
		else if(fType.equals("ODE Validation")) {
			return 4*Math.exp(0.8*t)-0.5*x.v[0];
		}
		else return 0;
	}

	/**Function: calculate the values of K for RK3, and RK4
	 * @param: Vector xi, double ti, double h, String fxType
	 * @return: K[Vector], K.length = 4, where each Vector represents K1-K4, and each Vector
	 * 			contains two elements, respectively for V1 and V2*/
	public static ArrayList<Vector> K(Vector x, double t, double h, String[] fType) {
		ArrayList<Vector> K = new ArrayList<Vector>();
		K.add(new Vector(x.len));
		K.add(new Vector(x.len));
		K.add(new Vector(x.len));
		K.add(new Vector(x.len));

		// Update K for both V1 and V2
		for (int i=0; i<x.len; i++) {
			// K1
			K.get(0).v[i] = f(x, t, fType[i]);
			// K2
			K.get(1).v[i] = f(x.add(K.get(0), 0.5*h), t + 0.5*h, fType[i]);
			// K3
			K.get(2).v[i] = f(x.add(K.get(1), 3.0/4*h), t + 3.0/4*h, fType[i]);
			// K4
			K.get(3).v[i] = f(x.add(K.get(2), h), t + h, fType[i]);
		}
		return K;
	}

	/**Function: calculate the X(RK3) and X(RK4)
	 * @param: Vector x_i0, double t_i0, double h, String[] fType
	 * @return: ArrayList<Vector> X = [X(RK3), X(RK4)]*/
	public static ArrayList<Vector> xRK3_xRK4(Vector x, double t, double h, String[] fType) {
		ArrayList<Vector> K = K(x, t, h, fType);
		Vector K1 = K.get(0);
		Vector K2 = K.get(1);
		Vector K3 = K.get(2);
		Vector K4 = K.get(3);

		// Calculate RK3's X
		Vector KsumRK3 = ((K1.scale(2.0)).add(K2, 3.0)).add(K3, 4.0);
		Vector x_i1_RK3 = x.add(KsumRK3, h*1.0/9);

		// Calculate RK4's X
		Vector KsumRK4 = (((K1.scale(7.0)).add(K2, 6.0)).add(K3, 8.0)).add(K4, 3.0);
		Vector x_i1_RK4 = x.add(KsumRK4, h*1.0/24);

		// Return RK3, RK4 together as an ArrayList
		ArrayList<Vector> xRK3_xRK4 = new ArrayList<Vector>();
		xRK3_xRK4.add(x_i1_RK3);
		xRK3_xRK4.add(x_i1_RK4);
		return xRK3_xRK4;
	}

	/**Function: Calculate the RK34 with adaptive h
	 * @param: Vector x_i0, double t_i0, double h, String fType[]
	 * @return: */
	public static Vector RK34AdaptiveH(Vector x, double t, double h, String fType[]) {
		// Initialize the method data structures
		ArrayList<Vector> xRK3_xRK4 = new ArrayList<Vector>(x.len);
		double r = 0;
		double step = 0;
		double stepLimit = h;

		// Within each time-stamp
		while(step < stepLimit) {
			xRK3_xRK4 = xRK3_xRK4(x, t, h, fType);
			r = r(xRK3_xRK4);
			// Adapt the h
			while(Math.abs(r-1) > Tol1 || Math.abs(r-1) < Tol2) {
				h /= Math.pow(r, 1.0/3.0); 
				if(h+step > 1) 
					break;
				xRK3_xRK4 = xRK3_xRK4(x, t, h, fType);
				r = r(xRK3_xRK4);
			}
			// Update the x_i0
			t += h;
			step += h;
			x.v = xRK3_xRK4.get(1).v.clone();
			h = 1 - step;
		}
		return x;
	}

	/**Function: Calculate x with Forward Euler
	 * @param: 
	 * @return:*/
	public static Vector forwardEuler(Vector x, double t, double h, String fType[]) {
		Vector f = new Vector(x.len);
		for(int i=0; i<x.len; i++) {
			f.v[i] = f(x, t, fType[i]);
		}
		x = x.add(f, h);
		return x;
	}
	
	/**Function: Calculate the normalized error r = (xRK3 - xRK4) / (eR*xRK4 + eA)
	 * @param: ArrayList<Vector> RK3_RK4, double eR, double eA
	 * @return: double r*/
	public static double r(ArrayList<Vector> xRK3_xRK4) {
		Vector xRK3 = xRK3_xRK4.get(0);
		Vector xRK4 = xRK3_xRK4.get(1);
		Vector E = xRK3.add(xRK4, -1);
		double r = E.norm() / (xRK4.scale(eR).norm() + eA);
		return r;
	}
	

	/**Function: Calculate the ||Error%||2
	 * 			By computing the relative error of each unknown variable, and then the 2nd-order norm
	 * @param: Vector x, double t, String[] fType
	 * @return: double ||Error%||2 */
	public static double relativeErr(Vector x, double t, String[] fType) {
		double truth = 0;
		double sum = 0;

		// Computing each unknown variable's 2nd-order norm
		for(int i=0; i<x.len; i++) {
			truth = trueX(t+1, fType[i]);
			sum += Math.pow((truth - x.v[i])/truth, 2);
		}
		return Math.pow(sum, 0.5);
	}

}
