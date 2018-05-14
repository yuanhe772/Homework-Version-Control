/**
 * Main.java, ECE4960-P4
 * Created by Yuan He(yh772) on 2018/04/19
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 * 
 * P4, Main script:
 * The main entry for entire project 4, executes testing for helper functions, 
 * and ODE solvers, and executes circuit simulations
 */
public class Main {
	/* For measuring memory usage and computational time */
	static Runtime runtime = Runtime.getRuntime();
	static long start = 0;
	static long end = 0;


	public static void main(String args[]) throws Exception {
		/*--------------------------------- Test ---------------------------------*/
		// Test helper functions:
		Test.testHelper();

		// Test ODE solvers:
		Test.validateODESolvers("Forward Euler");
		Test.validateODESolvers("RK4");
		Test.validateODESolvers("RK34");



		/*--------------------------------- Solve ---------------------------------*/
		// Initial conditions:
		String Circuit3[] = {"Circuit3-1", "Circuit3-2", "Circuit3-3"};
		double init3[] = {0, 0, 0};
		double start3 = 0;
		double end3 = 1E-7;
		double stepSize3 = 1E-9;

		// Computational time in nano seconds
		start = System.nanoTime();

		// Runge-Kutta Solver
		Solvers.solve(new Vector(init3), "RK4", Circuit3, start3, end3, stepSize3);

		// Computational time in nano seconds
		end = System.nanoTime();

		// Computational MEM in Bytes
		System.out.println("\nComputational Time = " + (end - start)/1e6 + "ms"
				+ "\nCurrent MEM usage = "+ (runtime.totalMemory()-runtime.freeMemory()) +" bytes");


	}
}
