
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
		dataGenerate.createReport("report.txt", "\nECE4960-Project 3 Report: ");

		/* Task 1: Implement a direct full-matrix solver.*/
		System.out.println("\n\n-------------------------------------- Task 1 --------------------------------------");


		/* Task 2: Create measured data set, and validate power-law parameter extraction script 
		 * 			with log transformation parameter extraction.*/
		System.out.println("\n\n-------------------------------------- Task 2 --------------------------------------");

		//		ArrayList<ArrayList<Double>> Measured = dataGenerate.powLawData(10, 10, -0.5);
		//		System.out.println(Arrays.toString(Measured.get(0).toArray()));
		//		System.out.println(Arrays.toString(Measured.get(1).toArray()));

		double tolerancePow = Math.pow(10, -9);
		double initGuessPow[] = {20, -1.0};
		iterativeSolver.iterSolver(QN, UNNORM, POW, new Vector(initGuessPow), tolerancePow);


		/* Task 3: Plot Id vs. Vds with the different values of Vgs. Implemented with Python's
		 * 			Jupyter Notebook, scripts and plots available in Git repo */
		System.out.println("\n\n-------------------------------------- Task 3 --------------------------------------");


		/* Task 4: Use Quasi-Newton and Secant method to extract NMOS EKV model's parameters*/
		System.out.println("\n\n-------------------------------------- Task 4 --------------------------------------");


		// Quasi-Newton estimation: Tol = 0.0001
		double tolerance1 = Math.pow(10, -12);
		double initGuessQN[] = {9e-7, 0.9, 0.9};
		iterativeSolver.iterSolver(QN, UNNORM, EKV, new Vector(initGuessQN), tolerance1);

		// Secant estimation: Tol = 0.0001
		//		double initGuessSC[] = {2.0e-06, 6e-01, 9e-01};
		//		iterativeSolver.iterSolver(SC, UNNORM, EKV, new Vector(initGuessSC), tolerance1);



		/* Task 5: Repeat Task 4 with normalized Id*/
		System.out.println("\n\n-------------------------------------- Task 5 --------------------------------------");
		// Quasi-Newton estimation: 
		//		gradientEsti.changePerturb(0.0000001);
		//		double toleranceQN = Math.pow(10, -20);
		//		double initGuessQNNormal[] = {2e-06, 6e-01, 9e-01};
		//		iterativeSolver.iterSolver(QN, NORM, EKV, new Vector(initGuessQNNormal), toleranceQN);

		// Secant estimation: Tol = 0.0001
		//		gradientEsti.changePerturb(0.0001);
		//		double toleranceSCNormal = 4.3*Math.pow(10, -15);
		//		double initGuessSCNormal[] = {2.35E-6, 0.59, 1.11};
		//		iterativeSolver.iterSolver(SC, NORM, EKV, new Vector(initGuessSCNormal), toleranceSCNormal);


		/* Task 6*/
		System.out.println("\n\n-------------------------------------- Task 6 --------------------------------------");
		//		initialGuess.guessWhat(QN, UNNORM, Math.pow(10, -12));

		/* Task 7*/
		System.out.println("\n\n-------------------------------------- Task 7 --------------------------------------");


	}
}