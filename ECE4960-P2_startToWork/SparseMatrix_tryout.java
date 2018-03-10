import java.util.ArrayList;

/**
 * SparseMatrix.java, ECE4960-P2
 * Created by Yuan He(yh772) on 2018-03-04
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 */

public class SparseMatrix {

	// Data structures for keeping the Sparse Matrix
	ArrayList<Double> value = new ArrayList<Double>();
	ArrayList<Integer> rowPtr = new ArrayList<Integer>();
	ArrayList<Integer> colInd = new ArrayList<Integer>();
	int rank;

	/**Constructor, pass in ArrayLists of value, row pointer, and column index.*/
	public SparseMatrix(ArrayList<Double> value, ArrayList<Integer> rowPtr, ArrayList<Integer> colInd) {
		// Update sparse matrix's rank
		rank = rowPtr.size()-1;

		// Update sparse matrix's data structures
		this.value = value;
		this.rowPtr = rowPtr;
		this.colInd = colInd;
	}

	/**Constructor2, pass in arrays of value, row pointer, and column index.*/
	public SparseMatrix(double[] value, int[] rowPtr, int[] colInd) {
		// Update sparse matrix's rank
		rank = rowPtr.length-1;

		// Update sparse matrix's data structures
		for(int i=0; i<value.length; i++) {this.value.add(value[i]);}
		for(int i=0; i<rowPtr.length; i++) {this.rowPtr.add(rowPtr[i]);}
		for(int i=0; i<colInd.length; i++) {this.colInd.add(colInd[i]);}
	}

	/**Constructor3, Create an empty sparse matrix by only indicating its rank*/
	public SparseMatrix(int rank) {
		// Update sparse matrix's rank
		this.rank = rank;

		// Create empty data structures for this sparse empty matrix
		for(int i=0; i<rank+1; i++) {rowPtr.add(0);}
	}

	/**Return the product of Matrix * Vector */
	public Vector product(Vector vec) {
		// Keep function invariants true
		assert rank == vec.len : "Unmatching matrix and vector size for matrix addition!";

		// Create data structure for containing the result vector
		double[] r = new double[vec.len];

		// Iteratively assign each element of the new vector to be the sum of row*vec
		for(int i=0; i < rank; i++) {
			for(int j=0; j < rank; j++) {
				r[i] += retrieveElement(i, j)[1] * vec.v[j]; 
			}
		}	
		return new Vector(r);
	}

	/**Return the product of this matrix(diagonal) left-multipying matrix sp */
	public SparseMatrix leftMult(SparseMatrix sp) {
		// Create data structure for storing the left-multiplication result
		SparseMatrix r = new SparseMatrix(sp.value, sp.rowPtr,sp.colInd);

		// Diagonal matrix's element(i,i), and right matrix's element(i,j)
		double ii = 0;
		double ij[] = new double[2];

		for(int i=0; i<rank; i++) {
			ii=retrieveElement(i,i)[1];
			for(int j=0; j<rank; j++) {
				ij = sp.retrieveElement(i, j);

				// Set matrix's value(Faster than calling matrixSetter() in this case), knowing no new 0 generated 
				if(ii*ij[1]!=0) {r.value.set((int) ij[0], ii*ij[1]);}
			}
		}
		return sp;
	}

	/**Return the sum of this matrix and matrix m*scale */
	public SparseMatrix add(SparseMatrix m, double d) {
		// Keep function invariants true
		assert m.rank == rank : "Unmatching matrix size for matrix addition!";

		// Create data structure for storing the addition result
		SparseMatrix sum = new SparseMatrix(rank);

		// Iteratively add two matrixes' (this matrix and matrix m) corresponding values into the sum
		for(int i=0; i<rank; i++) {
			for(int j=0; j<rank; j++) {
				sum.matrixSetter(i, j, retrieveElement(i,j)[1] + (m.retrieveElement(i, j)[1]*d));
			}
		}
		return sum;
	}

	//	/**Retrieve element (i,j) from Sparse Matrix*/
	//	public double retrieveElement(int i, int j) {
	//		// Keep function invariant true
	//		assertInd(i,j);
	//
	//		// Start and end of row i
	//		int start = rowPtr.get(i);
	//		int end = rowPtr.get(i+1);
	//
	//		// If find column j, return
	//		for (int k=start; k<end; k++) {
	//			if(j == colInd.get(k)) 
	//				return value.get(k);
	//		}
	//		return 0;
	//	}

	/**Retrieve element (i,j), along with its index in value(colInd) array. Return [element's index, element's value]*/
	public double[] retrieveElement(int i, int j) {
		// Keep function invariant true
		assertInd(i,j);

		// Data structure for keeping the result
		double r[] = new double[2];

		// Start and end of row i
		int start = rowPtr.get(i);
		int end = rowPtr.get(i+1);

		// If find column j, return
		for (int k=start; k<end; k++) {
			if(j == colInd.get(k)) {
				r[0] = k;
				r[1] = value.get(k);
				return r;
			}
		}
		return r;
	}


	// Helper function:
	//		/**Set element(i,j)'s value to v*/
	//		public void matrixSetter(int i, int j, double v) {
	//			// Keep function invariant true
	//			assertInd(i,j);
	//	
	//			try {
	//				// The range of value(colInd) = [start, end]
	//				int start = rowPtr.get(i);
	//				int end = rowPtr.get(j);
	//	
	//				// Flag: containing this value: 1; vice: 0
	//				boolean found = false;
	//	
	//				// Value(colInd) array's index for the to-be-set element
	//				int index = 0;
	//				for(int k=start; k<end; k++) {
	//					if(j == colInd.get(k)) {
	//						index = k;
	//						found = true;
	//						break;
	//					}
	//				}
	//	
	//				/* Update element(i, j), iff its original value != 0 (found in this sparse matrix): */
	//				if(found) {
	//					// Update element(i,j) with value v, when v != 0
	//					if(v != 0) {value.set(index, v);}
	//	
	//					// Set element(i,j) to 0, and update matrix's data structures, when v == 0
	//					else {
	//						//Delete from value[], and colInd[]
	//						value.remove(index);
	//						colInd.remove(index);
	//	
	//						//Decrement what's after rowPtr[i+1] by 1
	//						for(int k=i+1; i<rowPtr.size(); k++) {rowPtr.set(k, rowPtr.get(k) - 1);}
	//					}
	//				}
	//	
	//				/* Insert new non-zero element(i,j), iff original value == 0 (not found)：*/
	//				else {
	//					if(v!=0) {
	//						//Insert into value[], colInd:
	//						value.add(rowPtr.get(i), v);
	//						colInd.add(rowPtr.get(i), j);
	//	
	//						//Increment rowPtr[i+1] and the elements after it:
	//						for(int k=i+1; k<rowPtr.size(); k++) {rowPtr.set(k, rowPtr.get(k)+1);}
	//					}
	//				}
	//	
	//			}catch(IndexOutOfBoundsException e) {
	//				// Insert new element into empty Sparse Matrix
	//				if(v!=0) {
	//					//Insert into value[], colInd[]:
	//					value.add(v);
	//					colInd.add(j);
	//	
	//					//Increment rowPtr[r+1] and the elements after it:
	//					for(int k=i+1; k<rowPtr.size(); k++) {rowPtr.set(k, rowPtr.get(k)+1);}
	//				}
	//			}
	//			return;
	//		}

	//	/**Set element(i,j)'s value to v*/
	//	public void matrixSetter(int i, int j, double v) {
	//		// Keep function invariant true
	//		assertInd(i,j);
	//
	//		try {
	//			// The range of value(colInd) = [start, end]
	//			int start = rowPtr.get(i);
	//			int end = rowPtr.get(j);
	//
	//			// Flag: containing this value: 1; vice: 0
	//			boolean found = false;
	//
	//			// Value(colInd) array's index for the to-be-set element
	//			for(int k=start; k<end; k++) {
	//				if(j == colInd.get(k)) {
	//					// Update element(i,j) with value v, when v != 0
	//					if(v != 0) {value.set(k, v);}
	//
	//					// Set element(i,j) to 0, and update matrix's data structures, when v == 0
	//					else {
	//						//Delete from value[], and colInd[]
	//						value.remove(k);
	//						colInd.remove(k);
	//
	//						//Decrement what's after rowPtr[i+1] by 1
	//						for(int kk=i+1;kk<rowPtr.size(); kk++) {rowPtr.set(kk, rowPtr.get(kk) - 1);}
	//					}
	//					break;
	//				}
	//			}
	//			
	//			if(v!=0) {
	//				//Insert into value[], colInd:
	//				value.add(rowPtr.get(i), v);
	//				colInd.add(rowPtr.get(i), j);
	//
	//				//Increment rowPtr[i+1] and the elements after it:
	//				for(int k=i+1; k<rowPtr.size(); k++) {rowPtr.set(k, rowPtr.get(k)+1);}
	//			}
	//		}catch(IndexOutOfBoundsException e) {
	//			// Insert new element into empty Sparse Matrix
	//			if(v!=0) {
	//				//Insert into value[], colInd[]:
	//				value.add(v);
	//				colInd.add(j);
	//
	//				//Increment rowPtr[r+1] and the elements after it:
	//				for(int k=i+1; k<rowPtr.size(); k++) {rowPtr.set(k, rowPtr.get(k)+1);}
	//			}
	//		}
	//		return;
	//	}


	/**Set element(i,j)'s value to v*/
	public void matrixSetter(int i, int j, double v) {
		// Keep function invariant true
		assertInd(i,j);

		try {
			// The range of value(colInd) = [start, end]
			int start = rowPtr.get(i);
			int end = rowPtr.get(j);

			// Iff value!=0 (no new zero brought into the matrix)
			if(v!=0) {
				for(int k=start; k<end; k++) {
					// Iff position (i,j) originally non-zero, directly set the value
					if(j == colInd.get(k)) {
						value.set(k, v);
						return;
					}
				}

				// Iff position (i,j) originally zero; Insert into value[], colInd[]
				value.add(rowPtr.get(i), v);
				colInd.add(rowPtr.get(i), j);
				// Increment what's after rowPtr[i+1] by 1:
				for(int k=i+1; k<rowPtr.size(); k++) {rowPtr.set(k, rowPtr.get(k)+1);}
			}

			// Iff value==0 (new zeros brought into the matrix)
			else {	
				for(int k=start; k<end; k++) {

					// Iff position (i,j) originally non-zero
					if(j == colInd.get(k)) {

						// Directly delete elements in value[] and coInd[]
						value.remove(k);
						colInd.remove(k);
						// Decrement what's after rowPtr[r+1] by 1
						for(int kk=i+1; kk<rowPtr.size(); kk++) {rowPtr.set(kk, rowPtr.get(kk) - 1);}
						return;
					}
				}
			}

		}catch(IndexOutOfBoundsException e) {
			// Insert new element into and empty Sparse Matrix
			if(v!=0) {
				//Insert into value[], colInd[]:
				value.add(v);
				colInd.add(j);

				//Increment rowPtr[r+1] and the elements after it:
				for(int k=i+1; k<rowPtr.size(); k++) {rowPtr.set(k, rowPtr.get(k)+1);}
			}
		}
		return;
	}


	/** Decompose this matrix into the form of L, D-inverse, U for the operation of Jacobi iterative solver*/
	public void JacobiDecom(SparseMatrix L, SparseMatrix D, SparseMatrix U) {
		// Keep the function invariants true
		assert L.rank+D.rank+U.rank == 3*rank : "Wrong input matrix for Jacobi Decomposation!";

		// Create D matrix, by assigning the reciprocal of matrix sp's diagonal to its diagonal
		for(int i=0; i<rank; i++) {
			D.matrixSetter(i,i,1/retrieveElement(i, i)[1]);
		}

		// Create L & U matrix, by assigning this matrix's upper and lower diagonal values to them
		for(int i=0; i<rank; i++) {
			for(int j=i+1; j<rank; j++) {
				U.matrixSetter(i, j, (-1)*retrieveElement(i, j)[1]);
				L.matrixSetter(j, i, (-1)*retrieveElement(j, i)[1]);
			}
		}
	}

	//	/** Decompose this matrix into the form of L, D-inverse, U for the operation of Jacobi iterative solver*/
	//	public void JacobiDecom(SparseMatrix L, Vector D, SparseMatrix U) {
	//		// Keep the function invariants true
	//		assert L.rank+D.len+U.rank == 3*rank : "Wrong input matrix for Jacobi Decomposation!";
	//
	//		// Create D matrix, by assigning the reciprocal of matrix sp's diagonal to its diagonal
	//		for(int i=0; i<rank; i++) {
	//			D.v[i] = 1/retrieveElement(i, i)[1];
	//		}
	//
	//		//		double help[] = new double[2];
	//		// Create L & U matrix, by assigning matrix sp's upper and lower diagonal values to them
	//		for(int i=0; i<rank; i++) {
	//			for(int j=i+1; j<rank; j++) {
	//				//				help = retrieve(i, j)
	//				//				U.value.set(index, element);
	//				U.matrixSetter(i, j, (-1)*retrieveElement(i, j)[1]);
	//				L.matrixSetter(j, i, (-1)*retrieveElement(j, i)[1]);
	//			}
	//		}
	//	}

	/**Keep function invariants true: 0 <= index < size*/
	public void assertInd(int i, int j) {
		assert i>=0 && i<rank : "IndexOutOfBound!";
		assert j>=0 && j<rank : "IndexOutOfBound!";
	}
}
