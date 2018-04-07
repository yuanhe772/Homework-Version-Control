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


	// The measured data from outputNMOS.txt, and the constant VT
	static final ArrayList<ArrayList<Double>> measure = fileIO.readMeasure("outputNMOS.txt");
	static final int dataSize = measure.get(0).size();
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
				YModel = yEKVModel(para, measure.get(0).get(i), measure.get(1).get(i));
				YMeas = measure.get(2).get(i);
				V += Math.pow(YModel - YMeas,2);
			}
		}
		// To avoid if-statement in for-loop, implement two scenarios separately
		else if(normalType == NORM) {
			for(int i=0; i<dataSize; i++) {
				YModel = yEKVModel(para, measure.get(0).get(i), measure.get(1).get(i));
				YMeas = measure.get(2).get(i);
				V += Math.pow((YModel - YMeas)/YMeas,2);
			}
		}
		else throw new Exception("\nWrong gradient estimation type!!! Please input: QN or SC.");;
		return V;
	}

	/**Function: the normalized Loss Function(the objective function), V = sum (YModel - YMeasure)^2
	 * 			 Where the Y_model is the predicted Ids (calculated from the given parameters and measured Vgs, Vds)
	 * 			 Y_measure is the measured Ids
	 * Parameters: Vector para = [Is, kapa, Vth]
	 * Return: double */
	public static double normalV(Vector para) {
		// The structures for calculating the loss function
		double YModel = 0;
		double YMeas = 0;
		double normalV = 0;

		// Iteratively calculate V = sum (YModel - YMeasure)^2
		for(int i=0; i<dataSize; i++) {
			YModel = yEKVModel(para, measure.get(0).get(i), measure.get(1).get(i));
			YMeas = measure.get(2).get(i);
			normalV += Math.pow((YModel - YMeas)/YMeas,2);
		}
		return normalV;
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
	 * Parameters: int type of QN or SC, Vector paraGuess[]
	 * Return: 0 representing fail to converge, 1 representing converges
	 * @throws Exception */
	public static int iterSolver(int estType, int norm, Vector paraGuess, double tolerance) throws Exception {
		// Create delta V vector, Hessian V matrix, and the inversed Hessian V matrix
		Vector delV = new Vector(paraGuess.len);
		FullMatrix hessV = new FullMatrix(paraGuess.len);
		FullMatrix hessInverse = new FullMatrix(paraGuess.len);	

		// Create structures for initial guessed parameter, delta parameter, previous parameter, previous loss function's value, and iteration count
		Vector currPara = paraGuess;
		Vector prevPara = paraGuess;
		Vector deltaPara = paraGuess;
		double prevV = V(paraGuess, norm);
		int count = 0;

		// Create the scalar, and the two norms for line search's comparison
		double t = 1;
		double norm1 = 0;
		double norm2 = 0;

		// Iterative solver, two tolerances: 1. the loss function's value;  2. || deltaPara / para ||
		while(V(currPara, norm) > Math.pow(10, -7) && increMag(deltaPara, currPara) > tolerance) {
			count += 1;

			// Assign delV and hessV according to different gradient estimation type
			if(estType == QN) {
				delV = gradientEsti.delQN(currPara, norm);
				hessV = gradientEsti.hessianQN(currPara, norm);
			}
			else if(estType == SC) {
				delV = gradientEsti.delSC(currPara, norm);
				hessV = gradientEsti.hessianSC(currPara, norm);
			}
			else throw new Exception("\nWrong gradient estimation type!!! Please input: QN or SC.");

			// Calculate the Hessian matrix's inverse, and the delta parameter
			hessInverse = hessV.inverseRank3();
			deltaPara = (hessInverse.product(delV)).scale(-1);

			// Line search to determine the scalar t
			t = 1;
			norm1 = V(currPara.add(deltaPara, t), norm);
			norm2 = V(currPara.add(deltaPara, t/2), norm);
			while(norm1 > norm2) {
				norm1 = norm2;
				t/=2;
				norm2 = V(currPara.add(deltaPara, t/2), norm);
			}

			// Update parameters by para = para + t*deltaPara, and update sensitivity Vector
			currPara = currPara.add(deltaPara, t);

			// Quadratic convergence's:
			System.out.println(count + "th iteration:\n    [Is, Kapa, Vth] = " + Arrays.toString(currPara.v) + 
					", || relative delta || = "+increMag(deltaPara, prevPara) + 
					"\n    sensitivity of [Is, Kapa, Vth] = " + Arrays.toString(paraSensi(deltaPara, currPara, norm).v) + 
					"\n    || V || = " + V(currPara, norm) + 
					";  Quadratic convergence's (V(k) - V(0)) / V(k-1)^2 = "+ ((V(currPara, norm) - V(paraGuess,norm))/(prevV*prevV))+"\n");

			// update the previous V, and previous parameters
			prevV = V(currPara, norm);
			prevPara = currPara;
		}
		return 1;
	}


	/* Calculation of increment vector magnitude, and parameter sensitivity*/
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
	 * 			 reflecting how much perturbation is caused to the loss function's value V with one little change of each
	 * 			 parameter
	 * Parameters: Vector deltaPara = [delta Is, delta kapa, delta Vth], Vector para = [Is, kapa, vth]
	 * Return: Vector paraSensitivity = [Is sensitivity, kapa sensitivity, Vth sensitivity]
	 * @throws Exception */
	public static Vector paraSensi(Vector deltaPara, Vector para, int norm) throws Exception {
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
			numerator = V(perturbed, norm) - V(para, norm) / V(para, norm);
			denominator = deltaPara.v[i] / para.v[i];
			sens[i] = numerator / denominator;
		}
		return new Vector(sens);
	}
}
