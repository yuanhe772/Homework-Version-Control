import java.util.ArrayList;
import java.util.Arrays;

/**
 * Solvers.java, ECE4960-P5
 * Created by Yuan He(yh772) on 2018/05/16
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 * 
 * Generic Parabolic PDE solver.
 */
public class PDESolver {

	/**Function: Solve the 1D/2D parabolic PDE equations, with finite element method
	 * 			With discretization rules of Backward Euler and trapezoidal Euler
	 * @param: int rank, double term1, double term2, String Boundary condition
	 * @return: SparseMatrix*/
	public static void solve(String dimension, Vector init, double h, double D, double dt, String BC, ruleType rule, double startT, double endT) {
		// Compute A1, A2, B1, B2
		double[] AB = rule.A1A2B1B2(h, D, dt);

		// Construct matrix A, B according to either 1D or 2D parabolic PDE
		int rank = init.len;
		SparseMatrix A = dimension.equals("1D") ? Construct1D(rank, AB[0], AB[1], BC, "A") : Construct2D(rank, AB[0], AB[1]);
		SparseMatrix B = dimension.equals("1D") ? Construct1D(rank, AB[2], AB[3], BC, "B") : Construct2D(rank, AB[2], AB[3]);

		// Solve step-wisely
		Vector b = B.productVec(init);
		for(double i=startT; i<=endT;i++) {
			b = B.productVec(init);
			// The initial condition (boundary condition comes from previous solutions)
			init = Jacobi.solver(A,b);
			System.out.println("    When t = "+i+": \n        [n] = "+Arrays.toString(init.v));
		}
	}

	/**Function: Solve the 1D/2D parabolic PDE equations, with Forward Euler method
	 * 			The intuition comes from: d^2(Psi) / d(t)^2 * Psi = d(Psi) / d(t)
	 * @param: int rank, double term1, double term2, String Boundary condition
	 * @return: SparseMatrix*/
	public static void solveForward(String dimension, Vector init, double h, double D, double dt, ruleType rule, double startT, double endT) {
		// Compute A1, A2
		double[] AB = rule.A1A2B1B2(h, D, dt);

		// Construct matrix A according to either 1D or 2D parabolic PDE
		int rank = init.len;
		SparseMatrix A = dimension.equals("1D") ? Construct1D(rank, AB[0], AB[1], "Dirichlet", "A"): Construct2D(rank, AB[0], AB[1]);

		// Solve step-wisely
		for(double i=startT; i<=endT;i++) {
			init = A.productVec(init);
			System.out.println("    When t = "+i+": \n        [n1, n2, n3] = "+Arrays.toString(init.v));
		}
	}

	/**Function: The implementation for constructing Matrix A and B
	 * @param: int rank, double term1, double term2, String Boundary condition
	 * @return: SparseMatrix*/
	public static SparseMatrix Construct1D(int rank, double term1, double term2, String BC, String AB) {
		// Construct data structures for SparseMatrix
		ArrayList<Double> value = new ArrayList<Double>();
		ArrayList<Integer> colInd = new ArrayList<Integer>();
		ArrayList<Integer> rowPtr = new ArrayList<Integer>();

		// Add first row into SparseMatrix
		if (AB.equals("A") && BC.equals("Neumann0"))
			value.add(term2-1);
		else
			value.add(term2);
		value.add(term1);
		colInd.add(0);
		colInd.add(1);
		rowPtr.add(0);
		rowPtr.add(2);
		int pointer = 2;

		// Add 2nd ~ (rank-1)th row into SparseMatrix
		for(int i=1; i<rank-1; i++) {
			value.add(term1);
			value.add(term2);
			value.add(term1);
			colInd.add(i-1);
			colInd.add(i);
			colInd.add(i+1);
			pointer += 3;
			rowPtr.add(pointer);
		}

		// Add last row into SparseMatrix
		value.add(term1);
		if (AB.equals("A") && BC.equals("Neumann0"))
			value.add(term2-1);
		else
			value.add(term2);
		colInd.add(rank-2);
		colInd.add(rank-1);
		rowPtr.add(value.size());

		// Return SparseMatrix
		return new SparseMatrix(value, rowPtr, colInd);
	}

	/**Function: The implementation for constructing Matrix A for the purpose of solving 2D parabolic PDE with forward Eular rule
	 * 			
	 * @param: int rank, double term1, double term2
	 * @return: SparseMatrix*/
	public static SparseMatrix Construct2D(int rank, double term1, double term2) {
		SparseMatrix res = new SparseMatrix(rank);
		if(rank == 1)
			res.matrixSetter(0, 0, term2);
		else {
			// Set diagonal elements:
			for(int i=0; i<rank; i++) {
				res.matrixSetter(i, i, term2);
			}
			// Set tri-diagonal elements:
			double sqRank = Math.sqrt(rank);
			for(int i=0; i<rank-sqRank; i++) {
				res.matrixSetter(i, (int)(i+sqRank), term1);
				res.matrixSetter((int)(i+sqRank), i, term1);
			}
			// Set the other elements:
			if(rank == 4) 
				for(int i=0; i<rank; i+=sqRank) {
					res.matrixSetter(i, i+1, term1);
					res.matrixSetter(i+1, i, term1);
				}
			if(rank == 9) 
				for(int i=0; i<rank; i+=sqRank) {
					res.matrixSetter(i, i+1, term1);
					res.matrixSetter(i+1, i, term1);
					res.matrixSetter(i+1, i+2, term1);
					res.matrixSetter(i+2, i+1, term1);
				}
		}
		return res;
	}

}
