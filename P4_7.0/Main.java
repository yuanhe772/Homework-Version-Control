
/**
 * main.java, ECE4960-P4
 * Created by Yuan He(yh772) on 2018/04/19
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 * 
 * P4, Main script:
 * For executing testing, and circuit simulation
 */
public class Main {
	public static void main(String args[]) {
		/* ------------ Task 1 - Task 3: ----------- */

		// Test ODE solvers, including RK34, RK4, Forward-Euler
		Test.validateODESolvers();

		/* ---------------- Task 4: ---------------- */

		/* 1ns step size: */

		// Forward Euler
		Simulation.forwardEulerCircuit1(1e-9);
		// RK4
		Simulation.RK4Circuit1(1e-9);
		// RK34 with time adaption
		Simulation.RK34AdaptiveCircuit1(1e-9);

		/* 0.2ns step size: */

		// Forward Euler
		Simulation.forwardEulerCircuit1(2e-10);
		// RK4
		Simulation.RK4Circuit1(2e-10);
		// RK34 with time adaption
		Simulation.RK34AdaptiveCircuit1(2e-10);

		/* ---------------- Task 5: ---------------- */

		/* 1ns step size: */

		// Forward Euler
		Simulation.forwardEulerCircuit2(1e-9);
		// RK4
		Simulation.RK4Circuit2(1e-9);
		// RK34 with time adaption
		Simulation.RK34AdaptiveCircuit2(1e-9);

		/* 0.2ns step size: */

		// Forward Euler
		Simulation.forwardEulerCircuit2(2e-10);
		// RK4
		Simulation.RK4Circuit2(2e-10);
		// RK34 with time adaption
		Simulation.RK34AdaptiveCircuit2(2e-10);
	}
}
