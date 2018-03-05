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
		double[] d = {x,y};
		v =d;
		len = 2;
	}

	/** */
	public boolean equals(Vector V2) {
		if(V2.len != len) {return false;}
		for(int i=0; i<len; i++) {
			if(V2.v[i] != v[i]) {
				return false;
			}
		}
		return true;
	}

	/**Switch row[i] and row[j] for matrix A and Vector x*/
	public void rowPermute(int i, int j) {
		if(i<0 || j<0 || i>=len || j>=len) return;
		double temp = v[i];
		v[i] = v[j];
		v[j] = temp;
		return;
	}

	/**Add a*row[i] to row[j] for Matrix A and Vector x*/
	public void rowScale(int i, int j, double a) {
		if(i<0 || j<0 || i>=len || j>=len) return;
		v[j] += v[i]*a;
		return;
	}
}
