
/**
 * tolNormCalc.java, ECE4960-P3
 * Created by Yuan He(yh772) on 2018/04/06
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 * 
 * Project 3, task 4, 5, 6, and 7:
 * Calculate the delta V and Hessian Matrix according to:
 * Either Quasi-newton method, or Secant method.
 */
public class gradientEsti {

	/* Class Variant: the perturbation proportion, and DEFINE values*/
	static double perturb = 0.0001;	
	static final int POW = 5;
	static final int EKV = 6;


	/* Class Methods:*/
	/* Quasi-Newton method: */
	/**Function: Calculate the delta V by Quasi-Newton gradient estimation.
	 * 			 Use 2nd-order central-difference formula: f(x + h) - f(x - h) / 2h, 
	 * 			 instead of 1st-order forward-difference: f(x + h) - f(x) / h to estimate the gradient.
	 * 			 Because it is proved in Note3, page6's Hacker Practice that the 2nd central difference 
	 * 			 has smaller relative error compared to the 1st forward difference, and would therefore
	 * 			 lead to faster and more precise convergence.
	 * 			 Perturb one element within one iteration.
	 * Parameters: Vector para = [Is, kapa, Vth]
	 * Return: Vector delVQN
	 * @throws Exception */
	public static Vector delQN(Vector para, int normType) throws Exception {
		if(iterativeSolver.MODEL == EKV) {
			// Perturb the parameter with scalar = perturb
			Vector allPerturbed = para.scale(perturb);

			// The parameter vector that got perturbed once
			Vector onePerturbed1 = new Vector(para.len);
			Vector onePerturbed2 = new Vector(para.len);

			// The result delta V vector
			Vector delVQN = new Vector(para.len);
			for(int i=0; i<para.len; i++) {
				// Create (x + h) and (x - h)
				onePerturbed1.v = para.v.clone();
				onePerturbed2.v = para.v.clone();
				onePerturbed1.v[i] += allPerturbed.v[i];
				onePerturbed2.v[i] -= allPerturbed.v[i];
				//Create V(x + h) - V(x - h) / 2*h
				delVQN.v[i] = (iterativeSolver.V(onePerturbed1, normType) - iterativeSolver.V(onePerturbed2, normType)) / (allPerturbed.v[i]*2);
			}
			return delVQN;
		}
		else return delVPow(para);
	}

	/**Function: Calculate the delta V of simple formulas(for instance power law)
	 * Parameters: Vector para = [c, m]
	 * Return: Vector delVPow*/
	public static Vector delVPow(Vector para) {
		Vector delV = new Vector(para.len);
		double c = para.v[0];
		double m = para.v[1];
		double xi = 0;
		double yi = 0;
		for(int i=0; i<iterativeSolver.powMeasure.get(0).size(); i++) {
			xi = iterativeSolver.powMeasure.get(0).get(i);
			yi = iterativeSolver.powMeasure.get(1).get(i);
			delV.v[0] += (Math.pow(xi, 2*m)*c-Math.pow(xi, m)*yi);
			delV.v[1] += (Math.log(xi)*Math.pow(xi, 2*m)*c*c-Math.log(xi)*Math.pow(xi, m)*yi*c);
		}
		return delV;
	}

	/**Function: Calculate the Hessian V by Quasi-Newton gradient estimation.
	 * 			 Use 2nd 2nd-order central-difference: f(x+h1+h2) - f(x-h1+h2) - f(x+h1-h2) + f(x-h1-h2) / 4*h1*h2, 
	 * 			 instead of 1st-order forward-difference: f(x+h1+h2) - f(x+h1) - f(x+h1) + f(x) / h1*h2 
	 * 			 to estimate the gradient.
	 * 			 Because it is proved in Note3, page6's Hacker Practice that the 2nd-order central-difference 
	 * 			 has smaller relative error compared to the 1st-order forward-difference.
	 * 			 Perturb one element within one iteration.
	 * Parameters: Vector para = [Is, kapa, Vth]
	 * Return: FullMatrix HessVQN
	 * @throws Exception */
	public static FullMatrix hessianQN(Vector para, int normType) throws Exception {
		if(iterativeSolver.MODEL == EKV) {
			// Perturb the parameter with scalar = perturb
			Vector allPerturbed = para.scale(perturb);

			// The parameter vector that got perturbed twice
			Vector twoPerturbed1 = new Vector(para.len);
			Vector twoPerturbed2 = new Vector(para.len);
			Vector twoPerturbed3 = new Vector(para.len);
			Vector twoPerturbed4 = new Vector(para.len);

			// The result Hessian Matrix
			FullMatrix HessVQN = new FullMatrix(para.len);
			for(int i=0; i<para.len; i++) {
				for(int j=0; j<para.len; j++) {
					// Create (x+h1+h2)
					twoPerturbed1.v = para.v.clone();
					twoPerturbed1.v[j] += allPerturbed.v[j];
					twoPerturbed1.v[i] += allPerturbed.v[i];
					// Create (x-h1+h2)
					twoPerturbed2.v = para.v.clone();
					twoPerturbed2.v[j] += allPerturbed.v[j];
					twoPerturbed2.v[i] -= allPerturbed.v[i];
					// Create (x+h1-h2)
					twoPerturbed3.v = para.v.clone();
					twoPerturbed3.v[j] -= allPerturbed.v[j];
					twoPerturbed3.v[i] += allPerturbed.v[i];
					// Create (x-h1-h2)
					twoPerturbed4.v = para.v.clone();
					twoPerturbed4.v[j] -= allPerturbed.v[j];
					twoPerturbed4.v[i] -= allPerturbed.v[i];
					// Create V(x+h1+h2) - V(x-h1+h2) - V(x+h1-h2) + V(x-h1-h2) / 4*h1*h2
					HessVQN.full[i][j] = (iterativeSolver.V(twoPerturbed1, normType) - iterativeSolver.V(twoPerturbed2, normType) 
							- iterativeSolver.V(twoPerturbed3, normType) + iterativeSolver.V(twoPerturbed4, normType)) /
							(allPerturbed.v[i]*allPerturbed.v[j]*4);
				}
			}
			return HessVQN;
		}
		else return hessianVPow(para);
	}

	/**Function: Calculate the Hessian V by the combination of true gradient and Quasi-Newton gradient estimation.
	 * Parameters: Vector para = [c, m]
	 * Return: FullMatrix HessVQN
	 * @throws Exception */
	public static FullMatrix hessianVPow(Vector para) {
		FullMatrix HessVQN = new FullMatrix(para.len);

		double xi = 0;
		double yi = 0;
		double c= para.v[0];
		double m= para.v[1];
		for(int i=0; i<iterativeSolver.powMeasure.get(0).size(); i++) {
			xi = iterativeSolver.powMeasure.get(0).get(i);
			yi = iterativeSolver.powMeasure.get(1).get(i);
			HessVQN.full[0][0] += Math.pow(xi, 2*m);
			HessVQN.full[1][0] += Math.log(xi)*Math.pow(xi, 2*m)*2*c-Math.log(xi)*Math.pow(xi, m)*yi;
		}

		double[] perturbed = {1.1*c, 1.1*m};
		Vector delV= delVPow(new Vector(perturbed));
		Vector originalV= delVPow(para);

		HessVQN.full[0][1] = (delV.v[0] - originalV.v[0]) / (perturbed[0] - para.v[0]);
		HessVQN.full[1][1] = (delV.v[1] - originalV.v[1]) / (perturbed[1] - para.v[1]);

		return HessVQN;
	}

	/* Secant method: */
	/**Function: Calculate the delta V by Secant gradient estimation.
	 * 			 Also use 2nd-order central-difference: f(x + h) - f(x - h) / 2h.
	 * 			 Perturb all element within one iteration.
	 * Parameters: Vector para = [Is, kapa, Vth]
	 * Return: Vector delVQN
	 * @throws Exception */
	public static Vector delSC(Vector para, int normType) throws Exception {
		// Perturb the parameter with scalar = perturb
		Vector allPerturbed = para.scale(perturb);
		Vector perturbed1 = para.add(allPerturbed, 1);
		Vector perturbed2 = para.add(allPerturbed, -1);

		// The numerator
		double numerator = iterativeSolver.V(perturbed1, normType) - iterativeSolver.V(perturbed2, normType);

		// The result delta V vector
		Vector delVQN = new Vector(para.len);
		for(int i=0; i<para.len; i++) {
			delVQN.v[i] = numerator / (allPerturbed.v[i]*2);
		}
		return delVQN;
	}

	/**Function: Calculate the Hessian V by Secant gradient estimation
	 * 			 Also use 2nd 2nd-order central-difference: f(x+2h) - f(x) - f(x-2h)/ 4*h*h,
	 * 			 but only assign values to the diagonal elements to make Secant Hessian matrix diagonal 
	 * 			 Perturb all element within one iteration.
	 * Parameters: Vector para = [Is, kapa, Vth]
	 * Return: FullMatrix HessVQN
	 * @throws Exception */
	public static FullMatrix hessianSC(Vector para, int normType) throws Exception {
		// Perturb the parameter with scalar = perturb
		Vector allPerturbed = para.scale(perturb);
		Vector perturbed1 = para.add(allPerturbed, 2);
		Vector perturbed2 = para.add(allPerturbed, -2);

		// The numerator
		double numerator = iterativeSolver.V(perturbed1, normType) - iterativeSolver.V(para, normType) + iterativeSolver.V(perturbed2, normType);

		// The result Hessian Matrix
		FullMatrix HessVQN = new FullMatrix(para.len);
		for(int i=0; i<para.len; i++) {
			HessVQN.full[i][i] = numerator /(allPerturbed.v[i]*allPerturbed.v[i]*4);
		}
		return HessVQN;
	}

	/**Function: change the default perturbing proportion from 0.0001 to any input newPerturb value
	 * 			 to have a faster convergence to the true solution
	 * Parameters: double newPerturb
	 * Return: None*/
	public static void changePerturb(double newPerturb) {
		perturb = newPerturb;
	}
}
