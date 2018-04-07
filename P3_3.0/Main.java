
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
	// Define QN = 1, SC = 2, NORMALIZED Y = 1, UNNORMALIZED Y = 0
	static final int QN = 1;
	static final int SC = 2;
	static final int NORM = 1;
	static final int UNNORM = 0;

	public static void main(String args[]) throws Exception {

		// Generating report:
		fileIO.createReport("report.txt", "\nECE4960-Project 3 Report: ");

		/* Task 1: Implement a direct full-matrix solver.*/


		/* Task 2: Create measured data set, and validate power-law parameter extraction script 
		 * 			with log transformation parameter extraction.*/


		/* Task 3: Plot Id vs. Vds with the different values of Vgs. Implemented with Python's
		 * 			Jupyter Notebook, scripts and plots available in Git repo */


		/* Task 4: Use Quasi-Newton and Secant method to extract NMOS EKV model's parameters*/

		// Quasi-Newton estimation
		// Tol = 0.0001

		//		double guessQN[] = {9e-7, 0.9, 0.9};
		//		double toleranceQN = Math.pow(10, -12);
		//		iterativeSolver.iterSolver(QN, UNNORM, new Vector(guessQN), toleranceQN);

		// Secant estimation:
		// Tol = 0.0001

		//		double guessSC[] = {2e-06, 6e-01, 9e-01};
		//				double toleranceSC = Math.pow(10, -12);
		//		iterativeSolver.iterSolver(SC, UNNORM, new Vector(guessSC), toleranceSC);



		/* Task 5: Repeat Task 4 with normalized Id*/
		// Quasi-Newton estimation
		// Tol = 0.0000001;
		//		double guessQNNormal[] = {2e-06, 6e-01, 9e-01};
		//		iterativeSolver.iterSolver(QN, NORM, new Vector(guessQNNormal), toleranceQN);

		// Secant estimation:
		// Tol = 0.0001
		double toleranceSC = 4.3*Math.pow(10, -12);
		double guessSCNormal[] = {2e-06, 6e-1, 9e-1};
		iterativeSolver.iterSolver(SC, NORM, new Vector(guessSCNormal), toleranceSC);


		/* Task 6*/

		/* Task 7*/


	}
}