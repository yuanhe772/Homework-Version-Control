/**
 * Vector.java, ECE4960-P2
 * Created by Yuan He(yh772) on 2018-03-04
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 */


public class Vector {

	// Data structures for keeping the Vector
	double[] v;
	int len;

	/** Constructor, pass in any one-dimensional array*/
	public Vector(double[] a) {
		// Update the data structures
		v = a.clone();
		len = v.length;
	}

	/** Constructor2, Create a new vector by indicating its length*/
	public Vector(int len) {
		// Update the data structures
		v = new double[len];
		this.len = len;
	}

	/** Check iff this == V2, return true on equal, false on inequality */
	public boolean equals(Vector V2) {
		// First compare length
		if(V2.len != len) {return false;}

		//Then compare value
		for(int i=0; i<len; i++) {
			if(V2.v[i] != v[i]) {
				return false;
			}
		}
		return true;
	}

	/**Return the sum of this vector and vector v*/
	public Vector add(Vector vec, double d) {
		// Keep function invariants true
		assert vec.len == len;

		// Create data structure for storing the addition result
		Vector sum = new Vector(len);

		// Iteratively add two vectors' corresponding values into the sum
		for(int i=0; i<len; i++) {
			sum.v[i] = v[i] + d*vec.v[i];
		}
		return sum;
	}

	/**Keep function invariants true: 0 <= index < size*/
	public void assertInd(int i, int j) {
		assert i>=0 && i<len;
		assert j>=0 && j<len;
	}
}
