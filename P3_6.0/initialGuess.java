import java.util.Arrays;

/**
 * initialGuess.java, ECE4960-P3
 * Created by Yuan He(yh772) on 2018/04/07
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 * 
 * Project 3, task 6:
 * Try different initial guesses for [Is, Kapa, Vth]'s iterative parameter extraction,
 * with regions of IS in (10-8A, 3×10-8A, 10-7A, 3×10-7A, 10-6A, 3×10-6A, 10-5A, 3×10-5A), 
 * Kapa in (0.5, 0.6, 0.7, 0.8, 0.9) and Vth in (0.8, 0.9, 1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 
 * 1.6, 1.7, 1.8, 1.9, 2.0)
 */
public class initialGuess {
	static final double Is[] = {1e-8, 3e-8, 1e-7, 3e-7, 1e-6, 3e-6, 1e-5, 3e-5};
	static final double Kapa[] = {0.5, 0.6, 0.7, 0.8, 0.9};
	static final double Vth[] = {0.8, 0.9, 1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0};

	/**Function: choose the best parameter's initial guess out of the range given above
	 * Parameters: int type_of_QN_or_SC, int normalized_or_not, Vector paraGuess[], double tolerace
	 * Return: Vector guess, the initial guess that renders the smallest V
	 * @throws Exception */
	public static Vector guessWhat(int estType, int normType, int modelType, double tolerance) throws Exception {
		double solverRes[] = new double[4];
		double guess[] = new double[3];
		double minV[] = {0,0,Math.pow(10, 10),0};
		@SuppressWarnings("unused")
		double minGuess[] = new double[3];

		for(int i=0; i<Is.length; i++) {
			guess[0] = Is[i];
			for(int j=0; j<Kapa.length; j++) {
				guess[1] = Kapa[j];
				for(int k=0; k<Vth.length; k++) {
					guess[2] = Vth[k];
					solverRes = iterativeSolver.iterSolver(estType, normType, modelType, new Vector(guess), tolerance);
					if(solverRes[2] < minV[2]) {
						minV = solverRes.clone();
						minGuess = guess.clone();
					}
					System.out.println("\n一次啦一次啦一次啦！\n");
				}
			}
		}

		System.out.println(Arrays.toString(solverRes));
		System.out.println(Arrays.toString(guess));

		return new Vector(guess);
	}
}
