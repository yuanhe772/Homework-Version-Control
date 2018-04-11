import java.util.Arrays;

/**
 * main.java, ECE4960-P3
 * Created by Yuan He(yh772) on 2018/04/05
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 * 
 * Project 3, the main script:
 * For executing each task listed on P3's handout, and generate project 3's report
 */
public class Main {

	/*Class variants, the DEFINE value*/
	static final int QN = 1;
	static final int SC = 2;
	static final int NORM = 3;
	static final int UNNORM = 4;
	static final int POW = 5;
	static final int EKV = 6;

	public static void main(String args[]) throws Exception {

		/* Generating report:*/
		dataIO.createReport("report.txt", "\nECE4960-Project 3 Report:");

		/* ------------------------------ helper functions pre-checking ------------------------------ */
		dataIO.output("\n\n\n   Pre-checking Helper Functions:");

		/* ---------------------------------------- Task 1: ------------------------------------------ */
		dataIO.output("\n\n\n  TASK 1: Direct Matrix Solver's validation:");


		/* ---------------------------------------- Task 2: ------------------------------------------ */
		dataIO.output("\n\n\n  TASK 2: Parameter Extraction with Power Law(Iterative Matrix Solver) and "
				+ "Linear Regression(Direct Matrix Solver):");

		// Linear Regression(Direct Matrix Solver):
		dataIO.output("\n\tLinear Regression(Direct Matrix Solver):");
		long startTime = System.nanoTime();
		Vector amLR = directMatrixSolver.directSolver(POW);
		double lossLR = iterativeSolver.V(amLR, UNNORM);
		long endTime = System.nanoTime();
		dataIO.output("\n\t\tExtracted [a, m] = "+Arrays.toString(amLR.v) + ", V = "+lossLR);
		dataIO.computationCost(endTime - startTime);

		// Power Law(Iterative Matrix Solver):
		dataIO.output("\n\tPower Law(Iterative Matrix Solver):");
		double tolerancePow = Math.pow(10, -7);
		double initialGuessPow[] = {20, -1};
		startTime = System.nanoTime();
		double resPL[] = iterativeSolver.iterSolver(QN, UNNORM, POW, new Vector(initialGuessPow), tolerancePow);
		endTime = System.nanoTime();
		dataIO.output("\n\t\tStep Size = 0.1% of parameter\n\t\tInitial [a, m] = "+Arrays.toString(initialGuessPow));
		dataIO.result(resPL);
		dataIO.convergenceObserve(resPL);
		dataIO.computationCost(endTime - startTime);

		// Result Analysis:
		dataIO.output("\n\n\t< ANALYSIS >:\n\t\tLinear Regression:\n\t\t\tPROS: Predictable computational cost and "
				+ "performance (Time cost in (15ms, 90ms), MEM usage = 2MB, V in (0.3, 1.5))\n\t\t\tCONS: "
				+ "although average case better than iterative solver, its best case is worse"
				+ ";\n\t\tPower Law:"
				+ "\n\t\t\tPROS: Through observation, the best case of Power Law is better than Linear regression, "
				+ "in both aspects of performance (smaller V) and cost (faster computation)."
				+ "\n\t\t\tCONS: Unpredictable computational cost and performance (Time cost in (4ms, 990ms), MEM usage"
				+ " in (2MB, 5MB), V in (0.1, 125)), due to occasional failures in convergence caused by fluctuating "
				+ "measured data and fixed initial guess.");


		/* ---------------------------------------- Task 3: ----------------------------------------- */
		dataIO.output("\n\n\n  TASK 3: Plot S(measure):\n\tAvailable in the Report_of_Task_3_5_7.pdf");


		/* ---------------------------------------- Task 4: ----------------------------------------- */
		dataIO.output("\n\n\n  TASK 4: Extract EKV's parameters with iterative solver:");

		// Quasi-Newton estimation: stepSize/parameter = 0.0001
		dataIO.output("\n\tQuasi-Newton method, with 2nd-order central-difference: f(x + h) - f(x - h) / 2h:");
		double tolerance1 = Math.pow(10, -12);
		double initGuessQN[] = {9e-7, 0.9, 0.9};
		double resQN[] = iterativeSolver.iterSolver(QN, UNNORM, EKV, new Vector(initGuessQN), tolerance1);
		dataIO.output("\n\t\tStep Size = 0.1% of parameter\n\t\tInitial [Is, Kappa, Vth] = "+Arrays.toString(initGuessQN));
		dataIO.result(resQN);
		dataIO.convergenceObserve(resQN);


		// Secant estimation: stepSize/parameter = 0.0001
		dataIO.output("\n\tSecant method, with 2nd-order central-difference: f(x + h) - f(x - h) / 2h:");
		double initGuessSC[] = {2.0e-06, 6e-01, 9e-01};
		double resSC[] = iterativeSolver.iterSolver(SC, UNNORM, EKV, new Vector(initGuessSC), tolerance1);
		dataIO.output("\n\t\tStep Size = 0.1% of parameter\n\t\tInitial [Is, Kappa, Vth]] = "+Arrays.toString(initGuessSC));
		dataIO.result(resSC);
		dataIO.convergenceObserve(resSC);

		// Result Analysis:
		dataIO.output("\n\n\t< ANALYSIS >:\n\t\tQuasi-Newton:\n\t\t\tPROS: Quadratic Convergence, and more robust against far-away initial guesses;"
				+ "\n\t\t\tCONS: complex and costly inner-loop implementation;"
				+ "\n\t\tPower Law:\n\t\t\tPROS: straight-forward and less costly inner-loop implementation"
				+ "\n\t\t\tCONS: At most Linear Convergence, greedy and relatively more 'local optimal' (easily converges to an answer that's near the initial"
				+ "guess. Needs initial guesses very close to the 'true answer' to avoid wrong convergence).");

		/* ---------------------------------------- Task 5: ----------------------------------------- */
		dataIO.output("\n\n\n  TASK 5: Plot S(model)/S(measure):\n\tAvailable in the Report_of_Task_3_5_7.pdf");


		/* ---------------------------------------- Task 6: ----------------------------------------- */
		dataIO.output("\n\n\n  TASK 6: Optimized Initial Guess Search:");

		//disable the print-outs, and change the MAX_ITERATION to minimize computational time
		iterativeSolver.logModeEnable(false);
		iterativeSolver.changeMaxIter(50);

		// Unnormalized data, Quasi-Newton:
		dataIO.initialGuess(QN, UNNORM, EKV, tolerance1);

		// Unnormalized data, Secant:
		dataIO.initialGuess(SC, UNNORM, EKV, tolerance1);

		// Normalized data, Quasi-Newton:
		dataIO.initialGuess(QN, NORM, EKV, tolerance1);

		// Normalized data, Secant:
		dataIO.initialGuess(SC, NORM, EKV, tolerance1);

		/* ---------------------------------------- Task 7: ----------------------------------------- */
		dataIO.output("\n\n\n  TASK 7: Validation for Quasi-Newton method: "
				+ "(visualization section available in the Report_of_Task_3_5_7.pdf)");
		double paraQN[] = {resQN[5], resQN[6], resQN[7]};
		Test.testQuasiNewton(new Vector(paraQN));
	}
}