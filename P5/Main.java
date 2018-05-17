
/**
 * Main.java, ECE4960-P5
 * Created by Yuan He(yh772) on 2018/05/16
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 */
public class Main {

	public static void main(String args[]) {
	
		// Backward
		double[] valueN = {0, 10, 0};
		Vector n = new Vector(valueN);
		PDESolver.solve(n, 1, 1, 1, "Dirichlet0", new backward(), 2, 5);
		n = new Vector(valueN);
		PDESolver.solve(n, 1, 1, 1, "Neumann0", new backward(), 2, 5);
		
//		// Trapezoid
//		n = new Vector(valueN);
//		PDESolver.solve(n, 1, 1, 1, "Dirichlet", new trapezoid(), 2, 5);
//		n = new Vector(valueN);
//		PDESolver.solve(n, 1, 1, 1, "Dirichlet", new trapezoid(), 2, 5);
	}
	
}
