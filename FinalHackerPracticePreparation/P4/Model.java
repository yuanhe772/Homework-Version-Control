
/**
 * Model.java, ECE4960-P4-modified
 * Created by Yuan He(yh772) on 2018/05/13
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 */
public class Model {
	
	/**Function: The ground-truth x(t)
	 * 			Take in any arbitrary Strings that represent the different models
	 * 			Return the Ground-Truth of that model
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

	/**Function: ODE functions: f(x,t) = dx(t) / dt
	 * 			 Functions are chosen according to the input fType
	 * @param: double x, double t, String fxType
	 * @return: double value*/
	public static double f(Vector x, double t, String fType) {
		// The constants
		double C1 = 1e-12;
		double C2 = 1e-12;
		double R1 = 1e4;
		double R2 = 1e4;
		double R3 = 1e4;
		double RG = 1e4;
		double RL = 1e4;
		double Vdd = 5;

		// The functions
		if(fType.equals("ODE Validation")) {
			return 4*Math.exp(0.8*t)-0.5*x.v[0];
		}
		else if(fType.equals("Circuit1-1")) {
			double former = -(1/(C1*R1) + 1/(C1*R2)) * x.v[0];
			double latter = 1/(C1*R2) * x.v[1];
			return (former + latter + i(t*1e9)/C1);
		}
		else if(fType.equals("Circuit1-2")) {
			double former = 1/(C2*R2) * x.v[0];
			double latter = -(1/(C2*R2) + 1/(C2*R3)) * x.v[1];
			return (former + latter);
		}
		else if(fType.equals("Circuit2-1")) {
			return (i(t*1e9)*RG - x.v[0]) / (RG*C1);
		}
		else if(fType.equals("Circuit2-2")) {
			double former = - IdEKV(x.v[0], x.v[1]) / C2;
			double latter = (Vdd - x.v[1])/ (RL*C2);
			return (former + latter);
		}
//		else if(fType.equals("Circuit3-1")) {
////			double former
//		}
//		else if(fType.equals("Circuit3-2")) {
//			
//		}
//		else if(fType.equals("Circuit3-3")) {
//			
//		}
		else return 0;
	}
	
	
	/* Helper functions that assist constructing the ODE functions*/
	/**Function: Transient i(t)
	 * @param: double t, unit = (ns)
	 * @return: double value*/
	public static double i(double t) {
		// Divide by period T = 20ns
		double remainder= t%20;

		// Calculate i(t)
		if(remainder>=0 && remainder<1) 
			return remainder*1e-4;
		else if(remainder>=1 && remainder<10) 
			return 1e-4;
		else if(remainder>=10 && remainder<11) 
			return (-0.1*remainder+1.1)*1e-3;
		else return 0;
	}

	/**Function: EKV IdEKV(t)
	 * @param: double V1, double V2
	 * @return: double Id*/
	private static double IdEKV(double V1, double V2) {
		// The constants
		double Is = 5e-6;
		double Kappa = 0.7;
		double Vth = 1;
		double Vt = 0.026;

		// The calculation
		double former= 1 + Math.exp(Kappa*(V1-Vth) / (2*Vt));
		double latter= 1 + Math.exp((Kappa*(V1-Vth)-V2) / (2*Vt));
		return Is*(Math.log(former)*Math.log(former) - Math.log(latter)*Math.log(latter));
	}
}
