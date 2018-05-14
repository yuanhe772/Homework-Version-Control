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
	static Runtime runtime = Runtime.getRuntime();
	static long start = 0;
	static long end = 0;


	public static void main(String args[]) throws Exception {
		String Circuit1[] = {"Circuit1-1", "Circuit1-2"};
		String Circuit2[] = {"Circuit2-1", "Circuit2-2"};

		double init1[] = {0, 0};
		double start1 = 0;
		double end1 = 1E-7;
		double stepSize1 = 1E-9;

		// For measuring memory usage and computational time

		// in nano seconds
		start = System.nanoTime();
		end = System.nanoTime();
		System.out.println(end - start);
		
		// in Bytes
		System.out.println("Current MEM usage = "+ (runtime.totalMemory()-runtime.freeMemory()) +" bytes");
		

	}
}
