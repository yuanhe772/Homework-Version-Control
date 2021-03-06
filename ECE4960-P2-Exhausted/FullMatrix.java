/**
 * FullMatrix.java, ECE4960-P2
 * Created by Yuan He(yh772) on 2018-03-04
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 */


public class FullMatrix {

	// Data structures for keeping the Vector
	double[][] full;
	int rank;


	/**Constructor*/
	public FullMatrix(double[][] m) {
		// Update the data structures
		full = m.clone();
		rank = m.length;
	}

	/**Return the product of Ax = b*/
	public Vector product(Vector vec) {
		if(vec.len != rank) 
			throw new ArithmeticException ("Matrix*Vector unmatching size error: rank != length");
		double[] r = new double[vec.len];
		// Iteratively multiply each element in Full Matrix by vector vec
		for(int i=0; i<rank; i++) {
			for(int j=0; j<rank; j++) {
				r[i] += full[i][j]*vec.v[j];
			}
		}		
		return new Vector(r);
	}
	
	/**Keep function invariants true: 0 <= index < size*/
	public void assertInd(int i, int j) {
		assert i>=0 && i<rank;
		assert j>=0 && j<rank;
	}
}
