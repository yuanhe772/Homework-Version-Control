import java.util.ArrayList;
import java.util.Arrays;

/**
 * nonlinearSolver.java, ECE4960-P3
 * Created by Yuan He(yh772) on 2018/04/05
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 * 
 * Project 3, task 4 and 5, parameter extraction with least-square fitting:
 * The parameter extraction could be implemented with either Quasi-Newton
 * nonlinear matrix solver, or Secant nonlinear matrix solver, which are
 * implemented below, with QN being 1 and SC being 2.
 */
public class iterativeSolver {

	/* Class Variants: The file inputs, and DEFINE values*/
	// Define QN = 1, SC = 2, NORMALIZED Y = 1, UNNORMALIZED Y = 0
	static final int QN = 1;
	static final int SC = 2;
	static final int NORM = 1;
	static final int UNNORM = 0;

	// The measured data for power law parameter extraction's validation
	static final ArrayList<ArrayList<Double>> powMeasure = dataGenerate.powLawData(10, 10, -0.5);
	//	static final int dataSize = NMOSMeasure.get(0).size();

	// The measured data for NMOS parameter extraction, and the constant VT
	static final ArrayList<ArrayList<Double>> NMOSMeasure = dataGenerate.readNMOS("outputNMOS.txt");
	static final int dataSize = NMOSMeasure.get(0).size();
	static final double VT = 0.026;

	/* Class methods: Implement the iterative solver for parameter extraction*/
	/**Function: the Loss Function(the objective function), V = sum (YModel - YMeasure)^2
	 * 			 Where the Y_model is the predicted Ids (calculated from the given parameters and measured Vgs, Vds)
	 * 			 Y_measure is the measured Ids.
	 * Parameters: Vector para = [Is, kapa, Vth], int normalType = either NORM or UNNORM
	 * Return: double V
	 * @throws Exception */
	public static double V(Vector para, int normalType) throws Exception {
		// The structures for calculating the loss function
		double YModel = 0;
		double YMeas = 0;
		double V = 0;

		// According to different normalTypes, iteratively calculate V = sum (YModel - YMeasure)^2
		if(normalType == UNNORM) {
			for(int i=0; i<dataSize; i++) {
				YModel = yEKVModel(para, NMOSMeasure.get(0).get(i), NMOSMeasure.get(1).get(i));
				YMeas = NMOSMeasure.get(2).get(i);
				V += Math.pow(YModel - YMeas,2);
			}
		}
		// To avoid if-statement in for-loop, implement two scenarios separately
		else if(normalType == NORM) {
			for(int i=0; i<dataSize; i++) {
				YModel = yEKVModel(para, NMOSMeasure.get(0).get(i), NMOSMeasure.get(1).get(i));
				YMeas = NMOSMeasure.get(2).get(i);
				V += Math.pow((YModel - YMeas)/YMeas,2);
			}
		}
		else throw new Exception("\nWrong gradient estimation type!!! Please input: QN or SC.");;
		return V;
	}

	/**Function: calculate a single Y_model, with given parameters, and a pair of measured Vgs and Vds
	 * Parameters: Vector para = [Is, kapa, Vth], double Vgs, double Vds
	 * Return: double res, ekvModel's output*/
	public static double yEKVModel(Vector para, double Vgs, double Vds) {
		double expo1 = Math.exp((para.v[1]*(Vgs - para.v[2])) / (2*VT));
		double expo2 = Math.exp((para.v[1]*(Vgs - para.v[2]) - Vds) / (2*VT));
		return para.v[0] * (Math.log(1+expo1) * Math.log(1+expo1) - Math.log(1+expo2) * Math.log(1+expo2));
	}

	/**Function: the core function of this script, an iterative solver with line search to solve a 
	 * 			 nonlinear matrix (in this case, to extract parameters)
	 * Parameters: int type_of_QN_or_SC, int normalized_or_not, Vector paraGuess[], double tolerace
	 * Return: double[] convResult = [V's convergence, V's quadratic convergence, V's value, count]
	 * 		   0 representing fail to converge, 1 representing converges
	 * @throws Exception */
	public static double[] iterSolver(int estType, int normType, Vector paraGuess, double tolerance) throws Exception {		
		// Create delta V vector, Hessian V matrix, and the inversed Hessian V matrix
		Vector delV = new Vector(paraGuess.len);
		FullMatrix hessV = new FullMatrix(paraGuess.len);
		FullMatrix hessInverse = new FullMatrix(paraGuess.len);	

		// Create structures for iterative solver
		Vector deltaPara = new Vector(paraGuess.v);
		Vector preprevPara = new Vector(paraGuess.v);
		Vector prevPara = new Vector(paraGuess.len);
		Vector currPara = new Vector(paraGuess.v);
		int count = 0;
		double convResult[] = new double[4];

		// Create scalar, and norms for line search
		double t = 1;
		double norm1 = 0;
		double norm2 = 0;

		// Iterative solver, two tolerances: 1. the loss function's value;  2. || deltaPara / para ||
		while(V(currPara, normType) > Math.pow(10, -7) && increMag(deltaPara, currPara) > tolerance) {
			count += 1;

			// Assign delV and hessV according to different gradient estimation type
			if(estType == QN) {
				delV = gradientEsti.delQN(currPara, normType);
				hessV = gradientEsti.hessianQN(currPara, normType);
			}
			else if(estType == SC) {
				delV = gradientEsti.delSC(currPara, normType);
				hessV = gradientEsti.hessianSC(currPara, normType);
			}
			else throw new Exception("\nWrong gradient estimation type!!! Please input: QN or SC.");

			// Calculate Hessian matrix's inverse, and delta parameter
			hessInverse = hessV.inverseRank3();
			deltaPara = (hessInverse.product(delV)).scale(-1);

			// Line search to determine the scalar t
			t = 1;
			norm1 = V(currPara.add(deltaPara, t), normType);
			norm2 = V(currPara.add(deltaPara, t/2), normType);
			while(norm1 > norm2) {
				norm1 = norm2;
				t/=2;
				norm2 = V(currPara.add(deltaPara, t/2), normType);
			}

			// Update parameters by para = para + t*deltaPara, and update sensitivity Vector
			currPara = currPara.add(deltaPara, t);

			// Convergence observation:
			System.out.println((estType==1?"Quasi-Newton":"Secant")+" method's "+count + 
					"th iteration:\n    [Is, Kapa, Vth] = " + Arrays.toString(currPara.v) + 
					", || relative delta || = "+increMag(deltaPara, prevPara) + 
					"\n    Their sensitivity = " + Arrays.toString(paraSensi(deltaPara, currPara, normType).v) + 
					"\n    || V || = " + V(currPara, normType) );

			// update the previous V, and previous parameters
			preprevPara = prevPara;
			prevPara = currPara;

			// Terminate the iterative solver and return 0 as an indicator of fail to converge when too many iterations
			if(count > 20000) {
				//				// Convergence observation:
				//				System.out.println((estType==1?"Quasi-Newton":"Secant")+" method's "+count + 
				//						"th iteration:\n    [Is, Kapa, Vth] = " + Arrays.toString(currPara.v) + 
				//						", || relative delta || = "+increMag(deltaPara, prevPara) + 
				//						"\n    Their sensitivity = " + Arrays.toString(paraSensi(deltaPara, currPara, normType).v) + 
				//						"\n    || V || = " + V(currPara, normType) );
				convResult[0] = 0;
				convResult[1] = quadConv(preprevPara, prevPara, currPara, normType);
				convResult[2] = V(currPara, normType);
				convResult[3] = count;
				return convResult;
			}
		}
		//		// Convergence observation:
		//		System.out.println((estType==1?"Quasi-Newton":"Secant")+" method's "+count + 
		//				"th iteration:\n    [Is, Kapa, Vth] = " + Arrays.toString(currPara.v) + 
		//				", || relative delta || = "+increMag(deltaPara, prevPara) + 
		//				"\n    Their sensitivity = " + Arrays.toString(paraSensi(deltaPara, currPara, normType).v) + 
		//				"\n    || V || = " + V(currPara, normType) );
		convResult[0] = 1;
		convResult[1] = quadConv(preprevPara, prevPara, currPara, normType);
		convResult[2] = V(currPara, normType);
		convResult[3] = count;
		return convResult;
	}

	/* Calculation of increment vector magnitude, parameter sensitivity, and quadratic convergence observation*/
	/**Function: Increment Vector magnitude || delta || = sum (delta para / para)^2
	 * Parameters: Vector deltaPara = [delta Is, delta kapa, delta Vth], Vector oldPara = [Is, kapa, vth]
	 * Return: double magnitude*/
	public static double increMag(Vector deltaPara, Vector oldPara) {
		double mag=0;
		for(int i=0; i<oldPara.len; i++) {
			mag += Math.pow((deltaPara.v[i]/oldPara.v[i]),2);
		}
		return mag;
	}

	/**Function: Parameter sensitivity paraSensi = ((perturbedV - originalV)/originalV) / (deltaPara/para),
	 * 			 with the numerator being the difference of V, and the denominator being the difference of parameter,
	 * 			 Reflecting how a perturbed parameter can change the objective function V
	 * Parameters: Vector deltaPara = [delta Is, delta kapa, delta Vth], Vector para = [Is, kapa, vth]
	 * Return: Vector paraSensitivity = [Is sensitivity, kapa sensitivity, Vth sensitivity]
	 * @throws Exception */
	public static Vector paraSensi(Vector deltaPara, Vector para, int normType) throws Exception {
		// Structure for storing sensitivity vector
		double sens[] = new double[para.len];

		// Calculating the sensitivity,
		Vector perturbed = new Vector(para.len);
		double numerator = 0;
		double denominator = 0;
		for(int i=0; i<para.len; i++) {
			// The parameter vector that being perturbed once with the deltaPara
			perturbed.v = para.v.clone();
			perturbed.v[i] += deltaPara.v[i];

			// The calculation of sensitivity
			numerator = V(perturbed, normType)/ V(para, normType);
			denominator = (deltaPara.v[i] + para.v[i]) / para.v[i];
			sens[i] = numerator / denominator;
		}
		return new Vector(sens);
	}

	/**Function: check if a solver is quadratically converging, by looking at:
	 * 			 When k -> infinity, |V(k) - V*| / |V(k-1) - V*|^2 == CONSTANT,
	 * 			 where V* is the loss function for true parameters, making V* == 0 in this case.
	 * 			 So that the formula above could be degenerated into: 
	 * 			 k -> infinity, |V(k)| / |V(k-1)|^2 == CONSTANT.
	 * 			 The iteration time is limited, so if |prevQuad - currQuad| / currQuad < 0.1%,
	 * 			 Then consider it is quadratically converging
	 * Parameters: Vector preprevPara, Vector prevPara, Vector currPara, int normType
	 * @throws Exception 
	 * 			 */
	public static int quadConv(Vector preprevPara, Vector prevPara, Vector currPara, int normType) throws Exception {
		// Calculate the previous and current Quadratic convergence indicator
		double prevQuad = V(prevPara, normType) / (V(preprevPara, normType)*V(preprevPara, normType));
		double currQuad = V(currPara, normType) / (V(prevPara, normType)*V(prevPara, normType));

		// check if |delta Quad| / currQuad is within tolerance
		if(Math.abs(prevQuad - currQuad)/currQuad < Math.pow(10,-3))
			return 1;
		else return 0;
	}
}
