
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
	// Define QN = 1, and SC = 2
	static final int QN = 1;
	static final int SC = 2;

	public static void main(String args[]) {

		// Generating report:
		fileIO.createReport("report.txt", "\nECE4960-Project 3 Report: ");

		// Task 1

		// Task 2

		// Task 3

		// Task 4
//		double m[] = {0.0000001,1,1};
		double m[] = {8.90e-07, 8.90e-01, 7.50e-01};
//		System.out.println(iterativeSolver.funcV(new Vector(m)));
		iterativeSolver.iterSolver(QN,new Vector(m));

		// Task 5

	}
}