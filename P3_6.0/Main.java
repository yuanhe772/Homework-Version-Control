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

		// Generating report:
		dataIO.createReport("report.txt", "\nECE4960-Project 3 Report: \n\n");

		/* Task 1: */
		dataIO.output("\n\nTask 1: Parameter Extraction with Linear Regression(Direct Matrix Solver).");
		Vector lineRegPara = directMatrixSolver.directSolver(POW);
		double lineRegV = iterativeSolver.V(lineRegPara, UNNORM);
		// 两种方法的比较，可以再加入computational cost的差别比较

		/* Task 2: Create measured data set, and validate power-law parameter extraction script 
		 * 			with log transformation parameter extraction.*/
		dataIO.output("\n\nTask 2: Parameter Extraction with Power Law(Iterative Matrix Solver).");
		double tolerancePow = Math.pow(10, -7);
		double initGuessPow[] = {20, -1};
		System.out.println(Arrays.toString(iterativeSolver.iterSolver(QN, UNNORM, POW, new Vector(initGuessPow), tolerancePow)));


		/* Task 3: Plot Id vs. Vds with the different values of Vgs. Implemented with Python's
		 * 			Jupyter Notebook, scripts and plots available in Git repo */
		System.out.println("\n\n-------------------------------------- Task 3 --------------------------------------");


		/* Task 4: Use Quasi-Newton and Secant method to extract NMOS EKV model's parameters*/
		System.out.println("\n\n-------------------------------------- Task 4 --------------------------------------");

		//		
		// Quasi-Newton estimation: Tol = 0.0001
		//		double tolerance1 = Math.pow(10, -12);
		//		double initGuessQN[] = {9e-7, 0.9, 0.9};
		////		double initGuessQN[] = {1e-7, 1, 1};
		//
		//		System.out.println(Arrays.toString(iterativeSolver.iterSolver(QN, UNNORM, EKV, new Vector(initGuessQN), tolerance1)));

		// Secant estimation: Tol = 0.0001
		//				double initGuessSC[] = {2.0e-06, 6e-01, 9e-01};
		//				System.out.println(Arrays.toString(iterativeSolver.iterSolver(SC, UNNORM, EKV, new Vector(initGuessSC), tolerance1)));


		/* Task 5: Repeat Task 4 with normalized Id*/
		System.out.println("\n\n-------------------------------------- Task 5 --------------------------------------");
		// Quasi-Newton estimation: 
		//		gradientEsti.changePerturb(0.0000001);
		//		double toleranceQN = Math.pow(10, -15);
		//		double initGuessQNNormal[] = {2e-06, 6e-01, 9e-01};
		//		System.out.println(Arrays.toString(iterativeSolver.iterSolver(QN, NORM, EKV, new Vector(initGuessQNNormal), toleranceQN)));

		// Secant estimation: Tol = 0.0001
		//		gradientEsti.changePerturb(0.0001);
		//		double toleranceSCNormal = 1.5*Math.pow(10, -14);
		//		double initGuessSCNormal[] = {2E-6, 0.59, 1.11};
		//		//		double toleranceSCNormal = 4.3*Math.pow(10, -15);
		//		//		double initGuessSCNormal[] = {2.35E-6, 0.59, 1.11};
		//		System.out.println(Arrays.toString(iterativeSolver.iterSolver(SC, NORM, EKV, new Vector(initGuessSCNormal), toleranceSCNormal)));


		/* Task 6*/
		System.out.println("\n\n-------------------------------------- Task 6 --------------------------------------");
		//		initialGuess.guessWhat(QN, UNNORM, Math.pow(10, -12));

		/* Task 7*/
		System.out.println("\n\n-------------------------------------- Task 7 --------------------------------------");
	}
}