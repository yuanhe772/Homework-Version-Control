import java.util.ArrayList;
import java.util.Arrays;

/**
 * Main.java, ECE4960-P2
 * Created by Yuan He(yh772) on 2018-03-04
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 */


public class Main {
	
	/**Implement the Jacobi Iterative solver*/
	public static int Jacobi(SparseMatrix A, Vector b) {
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
			errNorm = norm2Err(currX, prevX);

			// Iteration counter increments:
			i+=1;

			// Printing out the outcome of result:
			System.out.println("\nPrecision: ||A*x - b|| norm = "+precision(A,currX,b));
			System.out.println("Current X = "+Arrays.toString(currX.v));
		}
		return i;
	}

	/**Calculate the second norm of (delta/v) */
	public static double norm2Err(Vector u, Vector v) {
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
	
	
	public static void main(String args[]) {
		
		
		
//		SparseMatrix sp = new SparseMatrix(5000);
//		
//		String filePath[] = {"value.csv", "rowPtr.csv", "colInd.csv"};
//		
//		sp = fileIO.readMatrix(filePath);
//		
//		
//
//		double bb1[] = new double[sp.rank];
//		double bb2[] = new double[sp.rank];
//		double bb3[] = new double[sp.rank];
//		
//		for(int i=0; i<sp.rank; i++) {
//			bb1[i] = 0.0;
//			bb2[i] = 0.0;
//			bb3[i] = 0.0;
//		}
//		
//		bb1[0] = 1.0;
//		bb2[4] = 1.0;
//		
//		Vector b1 = new Vector(bb1);
//		Vector b2 = new Vector(bb2);
//		Vector b3 = new Vector(bb3);
//		
//		long startTime= System.nanoTime();
//		System.out.println("\nTo converge, it took iteration times of "+Jacobi(sp, b1));
//		long endTime= System.nanoTime();
//		Runtime runtime = Runtime.getRuntime();
//		System.out.println("\nSpent time: "+(endTime-startTime)+" ns");
		
		double value[] = {-4,1,1,4,-4,1,1,-4,1,1,-4,1,1,1,-4};
		int rowPtr[] = {0,3,6,9,12,15};
		int colInd[] = {0,1,4,0,1,2,1,2,3,2,3,4,0,3,4};
		SparseMatrix A = new SparseMatrix(value, rowPtr, colInd);

		double bb[] = {1,0,0,0,0};
		Vector b = new Vector(bb);
		
		
		long startTime= System.nanoTime();
		
		System.out.println("\nTo converge, it took iteration times of "+Jacobi(A, b));
		
		long endTime= System.nanoTime();
		Runtime runtime = Runtime.getRuntime();
		System.out.println("\nSpent time: "+(endTime-startTime)+" ns");

		
		
		//		System.out.println("  Part 2: permutations cost memory: "+(runtime.totalMemory()-runtime.freeMemory())+" bytes");
		
		
		
		
		
//		Jacobi(sp, b2);
//		Jacobi(sp, b3);
		
		
		
//		System.out.println(sp.rowPtr);
//		System.out.println(sp.rowPtr.size());
		
//		System.out.println(sp.value);
//		System.out.println(sp.value.size());
//		
//		System.out.println(sp.colInd);
//		System.out.println(sp.colInd.size());
//		

//		
		
		
		
	}
	
	
	
//	public static <E> void readInto(String filePath, ArrayList<E> arr) {
//		try {
//			
//			
////			Type[] parameters = ((ParameterizedType)genericParameterTypes[i]).getActualTypeArguments();
//			File file = new File(filePath);
//			if(file.isFile() && file.exists()) {
//				InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
//				BufferedReader br = new BufferedReader(isr);
//				String line = null;
//				while ((line = br.readLine()) != null) {
//					
//					if(1==1) {
////					arr.add(Integer.parseInt(line),null);
//					}
//				}
//				br.close();
//			} else {
//				System.out.println("File doesn't exist!");
//			}
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//	}
}
