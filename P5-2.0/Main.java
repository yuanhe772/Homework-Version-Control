
/**
 * Main.java, ECE4960-P5
 * Created by Yuan He(yh772) on 2018/05/16
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 */
public class Main {

	public static void main(String args[]) {

		/*------------------------- Testing: Helper Functions --------------------------*/


		/*------------------------- Testing: 1D Parabolic PDE --------------------------*/
		// Initial conditions, and constants
		double[] initCondition1D = {0, 0, 10, 0, 0};
		double D1 = 0.1;
		double h1 = 1;
		double dt1 = 1;
		double startTime1 = 1;
		double endTime1 = 30;

		// 1D - Forward Euler
		System.out.println("-------------------Forward--------------------");
		Vector initial1D = new Vector(initCondition1D);
		PDESolver.solveForward(new forward1D(), "1D", initial1D, h1, D1, dt1, startTime1, endTime1);

		// 1D - Backward Euler
		System.out.println("-------------------Backward--------------------");
		initial1D = new Vector(initCondition1D);
		PDESolver.solve(new backward1D(), "1D", initial1D, h1, D1, dt1, startTime1, endTime1);

		// 1D - Trapezoid Euler
		System.out.println("-------------------Trapezoid--------------------");
		initial1D = new Vector(initCondition1D);
		PDESolver.solve(new trapezoid1D(), "1D", initial1D, h1, D1, dt1, startTime1, endTime1);


		/*------------------------- Testing: 2D Parabolic PDE --------------------------*/
		// Initial conditions, and constants
		double[] initCondition2D = {0,0,0,0,10,0,0,0,0};
//		double[] initCondition2D = {0,0,0,0,0,0,0,10,10,0,0,0,0,0,0,0};

		double D2 = 0.1;
		double h2 = 1;
		double dt2 = 1;
		double startTime2 = 1;
		double endTime2 = 30;

		// 2D - Forward Euler
		System.out.println("-------------------Forward--------------------");
		Vector initial2D = new Vector(initCondition2D);
		PDESolver.solveForward(new forward2D(), "2D", initial2D, h2, D2, dt2, startTime2, endTime2);

		// 2D - Backward Euler
		System.out.println("-------------------Backward--------------------");
		initial2D = new Vector(initCondition2D);
		PDESolver.solve(new backward2D(), "2D", initial2D, h2, D2, dt2, startTime2, endTime2);

		// 2D - Trapezoid Euler
		System.out.println("-------------------Trapezoid--------------------");
		initial2D = new Vector(initCondition2D);
		PDESolver.solve(new trapezoid2D(), "2D", initial2D, h2, D2, dt2, startTime2, endTime2);
	}

}
