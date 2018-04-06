import java.util.ArrayList;
import java.util.Arrays;

/**
 * nonlinearSolver.java, ECE4960-P3
 * Created by Yuan He(yh772) on 2018/04/05
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 * 
 * Project 3, task4, parameter extraction for least-square fitting:
 * The parameter extraction could be implemented with either Quasi-Newton
 * nonlinear matrix solver, or Secant nonlinear matrix solver, which are
 * implemented below, with QN being 1 and SC being 2.
 */
public class iterativeSolver {

	/* Class variants: The file inputs, and DEFINE values*/
	// Define QN = 1, and SC = 2
	static final int QN = 1;
	static final int SC = 2;

	// The measured data from outputNMOS.txt, and the constant VT
	static final ArrayList<ArrayList<Double>> measure = fileIO.readMeasure("outputNMOS.txt");
	static final int dataSize = measure.get(0).size();
	static final double VT = 0.026;

	// The perturbing proportion in Quasi-Newton estimation
	static double perturb = 0.0000001;


	/* Class methods: Implement the iterative solver for parameter extraction*/
	/**Function: the Loss Function(the objective function), V = sum (YModel - YMeasure)^2
	 * 			 Where the Y_model is calculated from the given parameters and measured data
	 * 			 For the purpose of least-square fitting
	 * Parameters: double para = [Is, kapa, vth]
	 * Return: double */
	public static double funcV(Vector para) {
		// The structures for calculating the loss function
		double YModel = 0;
		double YMeas = 0;
		double V = 0;

		// Iteratively calculate V = sum (YModel - YMeasure)^2
		for(int i=0; i<dataSize; i++) {
			YModel = yEKVModel(para, measure.get(0).get(i), measure.get(1).get(i));
			YMeas = measure.get(2).get(i);
			V += Math.pow(YModel - YMeas,2);
		}
		return V;
	}

	/**Function: calculate a single Y_model, with given parameters, and a pair of measured Vgs and Vds
	 * Parameters: double[] para = [Is, kapa, vth], double Vgs, double Vds
	 * Return: double res, ekvModel's output*/
	public static double yEKVModel(Vector para, double Vgs, double Vds) {
		double exp1 = Math.exp((para.v[1]*(Vgs - para.v[2])) / (2*VT));
		double exp2 = Math.exp((para.v[1]*(Vgs - para.v[2]) - Vds) / (2*VT));
		return para.v[0] * (Math.log(1+exp1) * Math.log(1+exp1) - Math.log(1+exp2) * Math.log(1+exp2));
	}

	/**Function: calculate the delta parameter by Quasi-newton estimation
	 * Parameters: double[] para
	 * Return: Vector delQN*/
	public static Vector delQN(Vector para) {
		// Perturb the parameter with a scalar = perturb

				double p[] = {1e-11 * para.v[0], 1e-5*para.v[1], 1e-5*para.v[2]};
				Vector allPerturbed = new Vector(p);
//		Vector allPerturbed = para.scale(perturb);

		// The parameter vector got perturbed once for the Quasi-Newton 1st-order derivative calc
		Vector onePerturbed = new Vector(para.len);

		// The result delta funcV Vector
		Vector delVQN = new Vector(para.len);
		for(int i=0; i<para.len; i++) {
			onePerturbed.v = para.v.clone();
			onePerturbed.v[i] += allPerturbed.v[i];
			delVQN.v[i] = (funcV(onePerturbed) - funcV(para)) / allPerturbed.v[i];
		}

		return delVQN;
	}

	public static FullMatrix hessianQN(Vector para) {
		// Perturb the parameter with a scalar = perturb
//		Vector allPerturbed = para.scale(perturb);

				double p[] = {1e-11 * para.v[0], 1e-5*para.v[1], 1e-5*para.v[2]};
				Vector allPerturbed = new Vector(p);

		// The parameter vector got perturbed twice for the Quasi-Newton 2nd-order derivative calc
		Vector twoPerturbed = new Vector(para.len);

		// The parameter vector got perturbed once for the Quasi-Newton 1st-order derivative calc
		Vector onePerturbed = new Vector(para.len);

		// The result Hessian Matrix
		FullMatrix HessVQN = new FullMatrix(para.len);
		for(int i=0; i<para.len; i++) {
			onePerturbed.v = para.v.clone();
			onePerturbed.v[i] += allPerturbed.v[i];
			for(int j=0; j<para.len; j++) {
				twoPerturbed.v = para.v.clone();
				twoPerturbed.v[j] += allPerturbed.v[j];
				twoPerturbed.v[i] += allPerturbed.v[i];
				HessVQN.full[j][i] = (funcV(twoPerturbed) - 2*funcV(onePerturbed) + funcV(para)) /
						(allPerturbed.v[i]*allPerturbed.v[j]);
			}
		}
		return HessVQN;
	}

	/**Function: the core function of this script, an iterative solver with line search to solve a 
	 * 			 nonlinear matrix (in this case, to extract parameters)
	 * Parameters: int type(with 1 being QuasiNewton and 2 being Secant)
	 * Return:*/
	public static void iterSolver(int type, Vector paraGuess) {
		Vector delV = new Vector(paraGuess.len);
		FullMatrix hessV = new FullMatrix(paraGuess.len);
		FullMatrix hessInverse = new FullMatrix(paraGuess.len);		
		Vector para = paraGuess;
		Vector deltaPara = paraGuess;

		if(type == QN) {
			double t = 1;

			double norm1 = 1;
			double norm2 = 0;

			while(funcV(para) > Math.pow(10, -7) && deltaParaNorm(deltaPara, para) > Math.pow(10, -12)) {
				delV = delQN(para);
				hessV = hessianQN(para);
				hessInverse = hessV.inverseRank3();
				deltaPara = (hessInverse.product(delV)).scale(-1);
				t = 1;

//				System.out.println(Arrays.toString(para.v));
//
//				System.out.println(Arrays.toString(deltaPara.v));
//
//				System.out.println(Arrays.toString((para.add(deltaPara, t)).v));
//
//				System.out.println(Arrays.toString((para.add(deltaPara, t/2)).v));


				norm1 = funcV(para.add(deltaPara, t));
				norm2 = funcV(para.add(deltaPara, t/2));

//				System.out.println(norm1+" !!!!!!!!!!!!!!!!!!!! "+norm2);

				while(norm1 > norm2) {
					norm1 = norm2;
					t/=2;
					norm2 = funcV(para.add(deltaPara, t/2));
//					System.out.println("haha");
				}

				System.out.println("t = "+t);
				para = para.add(deltaPara, 1);
				System.out.println(deltaParaNorm(deltaPara, para)+"   "+Arrays.toString(para.v)+"   "+ funcV(para)+"\n\n\n\n\n");

			}
		}
	}

	/**Function: calculate the || deltaPara / para ||
	 * Parameter: Vector deltaPara, vector para
	 * Return: double result*/
	public static double deltaParaNorm(Vector deltaPara, Vector para) {
		double sum=0;
		for(int i=0; i<para.len; i++) {
			sum += Math.pow((deltaPara.v[i]/para.v[i]),2);
		}
		return Math.pow(sum, 0.5);
	}
}
