
/**
 * main.java, ECE4960-P4
 * Created by Yuan He(yh772) on 2018/04/19
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 */
public class Main {
	public static void main(String args[]) {
		/* Task 1 - Task 3: */

		// Test ODE solvers, including RK34, RK4, Forward-Euler
		//		Test.validateODESolvers();

		/* Task 4: */

		/* 1ns step size: */
		// Forward Euler
		//		Simulation.forwardEulerCircuit1(0.2e-9);

		// RK4 (RK34 without time adaption)
		//		Simulation.RK4Circuit1(0.2e-9);

		// RK34 with time adaption
		Simulation.RK34AdaptiveCircuit1(0.2e-9);

		/* 0.2ns step size: */
	}
}
