
/**
 * Jacobi.java, ECE4960-P2
 * Created by Yuan He(yh772) on 2018/03/07
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 */
public class Jacobi {

	/**Implement the Jacobi Iterative solver*/
	public static int solver(SparseMatrix A, Vector b) {
		// Decompose matrix sp into the form of L, D, U
		SparseMatrix L = new SparseMatrix(A.rank);
		SparseMatrix DInverse = new SparseMatrix(A.rank);
		SparseMatrix U = new SparseMatrix(A.rank);
		A.JacobiDecom(L, DInverse, U);

		// Norm of difference between last and current iteration's outcome
		double errNorm = 1;

		// Number of iterations:
		int i=0;

		// Pre-calculate (D^-1)*b, and (D^-1)*(L+U)
		Vector Db = DInverse.product(b);
		SparseMatrix DLU = DInverse.leftMult(L.add(U,1));

		// Vector containing (D^−1) * (L+U) * x(k−1)
		Vector DLUX = new Vector(Db.len);

		// Vector for previous and current outcomes, initialized to (D^-1)*b:
		Vector currX = Db;
		Vector prevX = currX;

		while (errNorm > Math.pow(10, -7)) {
			errNorm = 0;
			prevX = currX;
			DLUX = DLU.product(currX);

			// An iteration of the Jacabi iterative solver:
			currX = DLUX.add(Db,1);

			// Calculate the second norm error
			errNorm = conNorm(currX, prevX);

			// Iteration counter increments:
			i+=1;

			// Printing out the outcome of result:
			System.out.println("\nPrecision: ||A*x - b|| norm = "+precision(A,currX,b));
			//			System.out.println("Current X = "+Arrays.toString(currX.v));
		}
		return i;
	}

	/**Calculate the second norm of (delta/v) */
	public static double conNorm(Vector u, Vector v) {
		// Increment the error:
		double errNorm = 0;
		for(int j=0; j<v.len; j++) {
			errNorm += Math.pow((v.v[j] - u.v[j]),2);
		}

		// The norm of v:
		double normV = 0;
		for(int j=0; j<v.len; j++) {
			normV += Math.pow(v.v[j],2);
		}

		// Calculate the current 2-norm error:
		errNorm = Math.pow(errNorm/normV, 0.5);
		return errNorm;
	}

	/**The norm of ||Ax-b|| */
	public static double precision(SparseMatrix A, Vector x, Vector b) {
		// ||A*x-b||:
		Vector err = (A.product(x)).add(b,-1);

		double errNorm = 0;
		for(int j=0; j<x.len; j++) {
			errNorm += Math.pow(err.v[j],2);
		}

		// Calculate the current 2-norm error:
		errNorm = Math.pow(errNorm, 0.5);
		return errNorm;
	}
}
