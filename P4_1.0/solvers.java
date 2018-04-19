import java.util.ArrayList;
import java.util.Arrays;

/**
 * solvers.java, ECE4960-P4
 * Created by Yuan He(yh772) on 2018/04/17
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 * 
 * P4, solvers for
 */
public class solvers {

	/* Class Variants: */
	static final double eR = 1e-7;
	static final double eA = 1e-4;
	static final double Tol1 = 1e-2;
	static final double Tol2 = 1e-6;

	/* Class Methods: */
	/**Function: The ground truth x(t)
	 * @param: double ti, String fxType
	 * @return: */
	public static double trueX(double t) {
		double a = Math.exp(0.8*t)-Math.exp(-0.5*t);
		double b = Math.exp(-0.5*t);
		return (a*4/1.3+b*2);
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
		else if(fType.equals("ODE Validation2")) {
			return 4*Math.exp(0.8*t)-0.5*x.v[1];
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

	/**Funtion: calculate the X using RK3, and X using RK4
	 * @param: Vector x_i0, double t_i0, double h, String[] fType
	 * @return: ArrayList<Vector> X, containing RK3's X, and RK4's X*/
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

	/**Function: Calculate the RK34 without adaptive h, so mathematically it's just RK4
	 * @param: Vector x_i0, double t_i0, double h, String fType[]
	 * @return: */
	public static void RK4(Vector x, double t, double h, String fType[]) {
		// Initialize the method data structures
		ArrayList<Vector> xRK3_xRK4 = new ArrayList<Vector>(x.len);
		xRK3_xRK4 = xRK3_xRK4(x, t, h, fType);
		x.v = xRK3_xRK4.get(1).v.clone();

		System.out.println(Arrays.toString(x.v) +"      trueX = "+ trueX(t+1) + 
				"  Error% = " + Math.abs(x.v[0] - trueX(t+1))/trueX(t+1));
		return;
	}

	/**Function: Calculate the RK34 with adaptive h
	 * @param: Vector x_i0, double t_i0, double h, String fType[]
	 * @return: */
	public static double RK34AdaptiveH(Vector x_i0, double t_i0, double h, String fType[]) {
		// Initialize the method data structures
		ArrayList<Vector> xRK3_xRK4 = new ArrayList<Vector>(x_i0.len);
		double r = 0;
		double step = 0;
		double stepLimit = h;
		double tr = t_i0;

		// Within each time-stamp
		while(step < stepLimit) {
			xRK3_xRK4 = xRK3_xRK4(x_i0, t_i0, h, fType);
			r = r(xRK3_xRK4);

			// Adapt the h
			while(Math.abs(r-1) > Tol1 || Math.abs(r-1) < Tol2) {
				h /= Math.pow(r, 1.0/3.0); 
				if(h+step > 1) 
					break;
				xRK3_xRK4 = xRK3_xRK4(x_i0, t_i0, h, fType);
				r = r(xRK3_xRK4);
			}

			// Update the x_i0
			t_i0 += h;
			step += h;
			x_i0.v = xRK3_xRK4.get(1).v.clone();
			h = 1 - step;
		}
		System.out.println(Arrays.toString(x_i0.v) +"  TrueX = "+trueX(tr+1)+ "  Error% = " + (x_i0.v[0] - trueX(tr+1))/trueX(tr+1));
		return x_i0.v[0];
	}

	public static void main(String args[]) {
//		double x[] = {2,2};
//		Vector xx = new Vector(x);
//		String fType[] = {"hp1", "hp2"};
		
		double x[] = {2,0};
		Vector xx = new Vector(x);
		String fType[] = {"ODE Validation", ""};

		double h = 1;
		for(double t=0; t<4 ; t++) {
						RK34AdaptiveH(xx, t, h, fType);

//			RK4(xx, t, h, fType);
		}
	}

}
