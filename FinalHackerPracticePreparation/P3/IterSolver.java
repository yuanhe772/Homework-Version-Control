import java.util.ArrayList;
import java.util.Arrays;

/**
 * IterSolver.java, ECE4960-P3-modified
 * Created by Yuan He(yh772) on 2018/05/13
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 * 
 * The iterative solver based upon Least-Square Error and Gradient descent.
 */
public class IterSolver {

	public static final int maxIter = 1000000;
	static double perturb = 0.0001;	

	/**Function: Extract parameters out of non-linear equations
	 * @param: Vector para, ArrayList<Vector> x(the measured data), ModelInterface model
	 * @return: paraReturn: [currPara, linear convergence indicator, quadratic convergence indicator, count]*/
	public static paraReturn parameterExtract(Vector para, ArrayList<Vector> x, ModelInterface model) {
		int count = 0;
		Vector deltaPara = new Vector(para.len);
		Vector currPara = new Vector(para.v.clone());
		Vector prevPara = new Vector(para.len);
		Vector preprevPara = new Vector(para.len);

		while(count < maxIter) {
			// Calculate delta parameter
			deltaPara.v = getDeltaPara(currPara, x, model).v.clone();

			// Update the parameter with deltaPara
			preprevPara.v = prevPara.v.clone();
			prevPara.v = currPara.v.clone();
			currPara = currPara.add(deltaPara, 1);

			// Update counter
			count += 1;

			// Break if delta parameter is converging to an extremely small value
			if(deltaPara.norm() < 1E-7) {
				break;
			}

			// Break if Loss is smaller than an extremely small value
			if(V(currPara, x, model) < 1E-7) {
				break;
			}
		}
		
		// Return [parameter, linear convergence indicator, quadratic convergence indicator, and counter]
		paraReturn res = new paraReturn(currPara, lineConv(prevPara, currPara, x, model), 
				quadConv(preprevPara, prevPara, currPara, x, model), count);
		return res;
	}

	

	/**Function: Calculate the delta parameter
	 * @param: Vector para, ArrayList<Vector> x(the measured data), ModelInterface model
	 * @return: Vector delta parameter*/
	public static Vector getDeltaPara(Vector para, ArrayList<Vector> X, ModelInterface model) {
		// Calculate the Hessian Matrix of V
		FullMatrix HessV =new FullMatrix(para.len);
		for(int i=0; i<para.len; i++) {
			for(int j=0; j<para.len; j++) {
				HessV.full[i][j] = doubleDerivative(para, i, j, X, model);
			}
		}

		// Calculate the inverse of V's Hessian Matrix
		FullMatrix HessVInverse = new FullMatrix(para.len);
		if(para.len == 2) 
			HessVInverse.full = HessV.inverseRank2().full.clone();
		else if(para.len == 3) 
			HessVInverse.full = HessV.inverseRank3().full.clone();

		// Calculate the partial derivative of V, the delta V
		Vector deltaV = new Vector(para.len);
		for(int i=0; i<para.len; i++) {
			deltaV.v[i] = partialDerivative(para, i, X, model);
		}

		// Calculate the delta parameter = -(HessVInverse) * deltaV
		Vector deltaPara = new Vector(para.len);
		for (int i = 0; i < para.len; i++){
			for (int j = 0; j < para.len; j++) {
				deltaPara.v[i] += -1.0* (HessVInverse.full[j][i] * deltaV.v[j]);
			}
		}
		return deltaPara;
	}

	/**Function: Calculate the partial dericative of V
	 * @param: Vector para, int i(the index of the variable), ArrayList<Vector> x(the measured data), ModelInterface model
	 * @return: Vector partial-derivative*/
	public static double partialDerivative(Vector para, int i, ArrayList<Vector> X, ModelInterface model) {
		// Perturb the parameters
		Vector paraPert = new Vector(para.v);		
		paraPert.v[i] = para.v[i] * (1+perturb);

		// Calculate the partial derivative
		return (V(paraPert, X, model) - V(para, X, model)) / (para.v[i]*perturb);
	}

	/**Function: Calculate the double dericative of V
	 * @param: Vector para, int i, int j, ArrayList<Vector> x(the measured data), ModelInterface model
	 * @return: Vector partial-derivative*/
	public static double doubleDerivative(Vector para, int i, int j, ArrayList<Vector> X, ModelInterface model) {
		// Perturb the parameters
		Vector paraPert1 = new Vector(para.v);
		paraPert1.v[i] = para.v[i] * (1+perturb);
		Vector paraPert2 = new Vector(paraPert1.v);
		paraPert2.v[j] = para.v[j] * (1+perturb);

		// Calculate the double derivative
		return 6 * (V(paraPert2, X, model) - 2*V(paraPert1, X, model) + V(para, X, model)) / (para.v[i]*para.v[j]*perturb*perturb);
	}

	/**Function: Calculate the Loss value = SUM ( error^2 )
	 * @param: Vector para, ArrayList<Vector> x(the measured data), ModelInterface model
	 * @return: Vector partial-derivative*/
	public static double V(Vector para, ArrayList<Vector> X, ModelInterface model) {
		double v = 0;
		int dataSize = X.size();

		// Loss v = Sum (error^2)
		for(int i=0; i<dataSize; i++) {
			v += model.error(para, X.get(i));
		}
		return v;
	}

	/**Function: check if an iterative solver is quadratically converging, by looking at:
	 * 			 When k -> infinity, |V(k) - V*| / |V(k-1) - V*|^2 == CONSTANT,
	 * 			 where V* is the loss function for true parameters, so V* == 0 in this case.
	 * 			 Therefore the quadratic convergence formula above could be degenerated into: 
	 * 			 k -> infinity, |V(k)| / |V(k-1)|^2 == CONSTANT.
	 * 			 The iteration time is limited, so if |prevQuad - currQuad| / currQuad < 0.1%,
	 * 			 Then consider it is quadratically converging
	 * @param: Vector preprevPara, Vector prevPara, Vector currPara, int normType
	 * @return: int convergence, 1 for convergence, and 0 for fail to converge */
	public static int quadConv(Vector preprevPara, Vector prevPara, Vector currPara, ArrayList<Vector> x, ModelInterface model) {
		// Calculate the previous and current Quadratic convergence indicator
		double prevQuad = V(prevPara, x, model) / (V(preprevPara, x, model)*V(preprevPara, x, model));
		double currQuad = V(currPara, x, model) / (V(prevPara, x, model)*V(prevPara, x, model));

		// check if |delta Quad| / currQuad is within tolerance
		if(Math.abs(prevQuad - currQuad)/currQuad < Math.pow(10,-7))
			return 1;
		else return 0;
	}

	/**Function:help check in each iteration, if V is linearly converging to 0
	 * @param: Vector prevPara, Vector currParaint normType 
	 * @return: int convergence, where 1 for convergence and 0 for fail to converge*/
	public static int lineConv(Vector prevPara, Vector currPara, ArrayList<Vector> x, ModelInterface model) {
		int convRes = V(currPara, x, model) < 1.00001*V(prevPara, x, model)? 1 : 0;
		return convRes;
	}
}
