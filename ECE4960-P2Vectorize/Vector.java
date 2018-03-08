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

	/** Constructor2, pass in any one-dimensional, length==2 array*/
	public Vector(double x, double y) {
		// Update the data structures
		double[] d = {x,y};
		v =d;
		len = 2;
	}

	/** Constructor3, Create a new vector by indicating its length*/
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

	/**Return the product of a diagonal matrix(but represented as vector) with Vector vec*/
	public Vector productDiag(Vector vec) {
		// Keep function invariants true
		assert vec.len == len;
		
		System.out.println("hahahaha11111");

		// Create data structure for storing the multiplication result
		Vector prod = new Vector(len);

		// Iteratively add two vectors' corresponding values into the sum
		for(int i=0; i<len; i++) {
			prod.v[i] = v[i] * vec.v[i];
		}
		
		System.out.println("hahahaha222222");
		return prod;
	}

	/**Return the product of a diagonal matrix(but represented as vector) with Vector vec*/
	public SparseMatrix leftMult(SparseMatrix sp) {
		System.out.println("hahahahahahahah333333");
		
		// Keep function invariants true
		assert sp.rank == len;
		
		

		// Create data structure for storing the left-multiplication result
		SparseMatrix r = new SparseMatrix(sp.rank);

		// Left-multiplication: in the result, element(i,j) = sum(k) of (left(i,k)*right(k,j))
		for(int i=0; i<sp.rank; i++) {
			for(int j=0; j<sp.rank; j++) {
				r.matrixSetter(i, j, v[i]*sp.retrieveElement(i, j));
			}
		}
		
		
		System.out.println("hahahaha444444");
		return r;
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
