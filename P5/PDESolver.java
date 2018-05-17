import java.util.ArrayList;
import java.util.Arrays;

/**
 * Solvers.java, ECE4960-P5
 * Created by Yuan He(yh772) on 2018/05/16
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 */
public class PDESolver {

	public static void solve(Vector init, double h, double D, double dt, String BC, ruleType rule, int startT, int endT) {

		// Compute A1, A2, B1, B2
		double[] AB = rule.A1A2B1B2(h, D, dt);

		// Construct matrix A, B
		int rank = init.len;
		SparseMatrix A = Construct(rank, AB[0], AB[1], BC, "A");
		SparseMatrix B = Construct(rank, AB[2], AB[3], BC, "B");

		// Solve step-wisely
		Vector b = B.productVec(init);
		for(int i=startT; i<=endT;i++) {
			b = B.productVec(init);
			// The initial condition (boundary condition comes from previous solutions)
			init = Jacobi.solver(A,b);
			System.out.println("    When t = "+i+": \n        [n1, n2, n3] = "+Arrays.toString(init.v));
		}
	}


	/**Function: The implementation for constructing Matrix A and B
	 * @param: int rank, double term1, double term2, String Boundary condition
	 * @return: SparseMatrix*/
	public static SparseMatrix Construct(int rank, double term1, double term2, String BC, String AB) {
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

}
