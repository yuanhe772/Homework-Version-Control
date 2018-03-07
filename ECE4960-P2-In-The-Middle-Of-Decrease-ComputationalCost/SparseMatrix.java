/**
 * SparseMatrix.java, ECE4960-P2
 * Created by Yuan He(yh772) on 2018-03-04
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 */
import java.util.ArrayList;
import java.util.List;


public class SparseMatrix {

	// Data structures for keeping the Sparse Matrix
	ArrayList<Double> value = new ArrayList<Double>();
	ArrayList<Integer> rowPtr = new ArrayList<Integer>();
	ArrayList<Integer> colInd = new ArrayList<Integer>();
	int rank;

	/**Constructor, pass in ArrayLists of value, row pointer, and column index.*/
	public SparseMatrix(ArrayList<Double> value, ArrayList<Integer> rowPtr, ArrayList<Integer> colInd) {
		// Update matrix's rank
		rank = rowPtr.size()-1;

		// Update matrix's data structures
		this.value = value;
		this.rowPtr = rowPtr;
		this.colInd = colInd;
	}

	/**Constructor2, pass in arrays of value, row pointer, and column index.*/
	public SparseMatrix(double[] value, int[] rowPtr, int[] colInd) {
		// Update matrix's rank
		rank = rowPtr.length-1;

		// Update matrix's data structures
		for(int i=0; i<value.length; i++) {this.value.add(value[i]);}
		for(int i=0; i<rowPtr.length; i++) {this.rowPtr.add(rowPtr[i]);}
		for(int i=0; i<colInd.length; i++) {this.colInd.add(colInd[i]);}
	}

	/**Constructor3, Create an empty sparse matrix by only indicating its rank*/
	public SparseMatrix(int rank) {
		// Update matrix's rank
		this.rank = rank;

		//Create empty data structures for this empty matrix
		for(int i=0; i<rank+1; i++) {rowPtr.add(0);}
	}
//
//	/**Switch row[i] and row[j] for Sparse Matrix A. (Return 0 on success, 1 on failures)*/
//	public int rowPermute(int i, int j) {
//		try {
//			// Keep function invariants true
//			assertInd(i ,j);
//
//			// Create helper data structures for row permuting
//			ArrayList<Double> value_new = new ArrayList<Double>();
//			ArrayList<Integer> colInd_new = new ArrayList<Integer>();
//			ArrayList<Integer> rowPtr_new = new ArrayList<Integer>();
//
//			// Ensure that in the following implementations, i < j
//			if(i>j) {
//				int temp = i;
//				i = j;
//				j = temp;
//			}
//
//			/*Update value and colInd: (Utilizing ArrayList's high efficiency in search-by-index)*/ 
//			// Re-arrange corresponding values and column indexes, by separating the arrays to be
//			// [before i], [i,i+1], [between i & j], [j,j+1], [After j]
//			// Only switch [i,i+1], [j,j+1], with the other sections untouched
//			value_new.addAll(value.subList(rowPtr.get(0), rowPtr.get(i)));
//			value_new.addAll(value.subList(rowPtr.get(j), rowPtr.get(j+1)));
//			value_new.addAll(value.subList(rowPtr.get(i+1), rowPtr.get(j)));
//			value_new.addAll(value.subList(rowPtr.get(i), rowPtr.get(i+1)));
//			value_new.addAll(value.subList(rowPtr.get(j+1), value.size()));
//			colInd_new.addAll(colInd.subList(rowPtr.get(0), rowPtr.get(i)));
//			colInd_new.addAll(colInd.subList(rowPtr.get(j), rowPtr.get(j+1)));
//			colInd_new.addAll(colInd.subList(rowPtr.get(i+1), rowPtr.get(j)));
//			colInd_new.addAll(colInd.subList(rowPtr.get(i), rowPtr.get(i+1)));
//			colInd_new.addAll(colInd.subList(rowPtr.get(j+1), colInd.size()));
//
//			/*Update rowPtr: */
//			// Respectively representing non-zero elements of row i and row j
//			int r1_l = rowPtr.get(i+1) - rowPtr.get(i);
//			int r2_l = rowPtr.get(j+1) - rowPtr.get(j);
//
//			// Iff row i and j's non-zero elements are of the same amount, then make no change to rowPtr
//			if(r1_l == r2_l) {
//				rowPtr_new = rowPtr;
//			}
//
//			// Iff row i and j's non-zero elements are of different amounts
//			// The rowPtr could be separated into sections of [before i], [between i & j], [After j]
//			// Only need to update [between i & j], the head and tail don't need updates
//			else {
//				// Add [before i] of rowPtr to the new rowPtr
//				rowPtr_new.addAll(rowPtr.subList(0, i+1));
//
//				// The amount difference between row i and j
//				int gap = r2_l-r1_l;
//
//				// Increment every element in [between i & j] by (r2_l - r1_l)
//				for(int k=i+1; k<j+1; k++) {
//					rowPtr_new.add(rowPtr.get(k) + gap);
//				}
//
//				// Add [After j] of rowPtr to the new rowPtr
//				rowPtr_new.addAll(rowPtr.subList(j+1, rowPtr.size()));
//			}
//
//			// Update the object's data structures with the new values.
//			value = value_new;
//			rowPtr = rowPtr_new;
//			colInd = colInd_new;
//
//			// Release the space for helper data structures, left the objects to be garbage collected automatically
//			value_new = null;
//			rowPtr_new = null;
//			colInd_new = null;
//
//			return 0;
//		}catch(Exception e){
//			System.out.println(e.getMessage());
//			return 1;
//		}
//	}
//
//	/**Add a*row[i] to row[j] for Sparse Matrix A. (Return 0 on success, 1 on failures)*/
//	public int rowScale(int i, int j, double a) {
//		try {
//			// Keep function invariants true
//			assertInd(i ,j);
//
//			// Iteratively set row(j)'s element to be a*row[i]+row[j], setter is implemented as helper function below
//			for(int k=0; k<rank; k++) {
//				matrixSetter(j, k, retrieveElement(i, k)*a + retrieveElement(j, k));
//			}
//			return 0;
//		}catch(Exception e) {
//			System.out.println(e.getMessage());
//			return 1;
//		}
//	}

	/**Return the product of Ax = b*/
	public Vector product(Vector vec) {
		// Create data structure for containing the result vector
		double[] r = new double[vec.len];

		// Iteratively assign each element of the new vector to be the sum of row*vec
		for(int i=0; i < rank; i++) {
			for(int j=0; j < rank; j++) {
				r[i] += retrieveElement(i, j) * vec.v[j]; 
			}
		}
		return new Vector(r);
	}

	/**Return the product of this matrix(diagonal) left-multipying matrix sp */
	public SparseMatrix leftMult(SparseMatrix sp) {
		// Create data structure for storing the left-multiplication result
		SparseMatrix r = new SparseMatrix(rank);

		// Extract element(i,i) in first-level loop below to reduce computational cost
		double ii = 0;

		// Left-multiplication: in the result, element(i,j) = sum(k) of (left(i,k)*right(k,j))
		for(int i=0; i<rank; i++) {
			ii=retrieveElement(i,i);
			for(int j=0; j<rank; j++) {
				r.matrixSetter(i, j, ii*sp.retrieveElement(i, j));
			}
		}
		return r;
	}


	/**Return the sum of this matrix and matrix m*scale */
	public SparseMatrix add(SparseMatrix m, double d) {
		// Keep function invariants true
		assert m.rank == rank;

		// Create data structure for storing the addition result
		SparseMatrix sum = new SparseMatrix(rank);

		// Iteratively add two matrixes' (this matrix and matrix m) corresponding values into the sum
		for(int i=0; i<rank; i++) {
			for(int j=0; j<rank; j++) {
				sum.matrixSetter(i, j, retrieveElement(i,j) + (m.retrieveElement(i, j)*d));
			}
		}
		return sum;
	}

	/**Retrieve element (i,j) from Sparse Matrix*/
	public double retrieveElement(int i, int j) {
		// Start and end of row i
		int start = rowPtr.get(i);
		int end = rowPtr.get(i+1);

		// If find column j, return
		for (int k=start; k<end; k++) {
			if(j == colInd.get(k)) 
				return value.get(k);
		}
		return 0;
	}

	// Helper function:
	/**Set element(i,j)'s value to v*/
	public void matrixSetter(int i, int j, double v) {
		try {
			int start = rowPtr.get(i);
			int end = rowPtr.get(j);

			// Flag: containing this value: 1; vice: 0
			boolean found = false;

			// Value(colInd) array's index for the to-be-set element
			int index = 0;
			for(int k=start; k<end; k++) {
				if(j == colInd.get(k)) {
					index = k;
					found = true;
					break;
				}
			}
			
			/* Update element(i, j), iff its original value != 0 (found in this sparse matrix): */
			if(found) {
				// Update element(i,j) with value v, when v != 0
				if(v != 0) {value.set(index, v);}
				
				// Set element(i,j) to 0, and update matrix's data structures, when v == 0
				else {
					//Delete from value[], and colInd[]
					value.remove(index);
					colInd.remove(index);
					
					//Decrement what's after rowPtr[i+1] by 1
					for(int k=i+1; i<rowPtr.size(); k++) {rowPtr.set(k, rowPtr.get(k) - 1);}
				}
			}
			
			/* Insert new non-zero element(i,j), iff original value == 0 (not found)：*/
			else {
				if(v!=0) {
					//Insert into value[], colInd:
					value.add(rowPtr.get(i), v);
					colInd.add(rowPtr.get(i), j);

					//Increment rowPtr[i+1] and the elements after it:
					for(int k=i+1; k<rowPtr.size(); k++) {rowPtr.set(k, rowPtr.get(k)+1);}
				}
			}

		}catch(IndexOutOfBoundsException e) {
			// Insert new element into empty Sparse Matrix
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

	/** Decompose matrix into the form of L, D-inverse, U for the operation of Jacobi iterative solver*/
	public void JacobiDecom(SparseMatrix L, SparseMatrix D, SparseMatrix U) {

		// Keep the funtion invariants true
		assert rank*L.rank*D.rank*U.rank == Math.pow(rank,4);

		// Create D matrix, by assigning the reciprocal of matrix sp's diagonal to its diagonal
		for(int i=0; i<rank; i++) {
			D.matrixSetter(i, i, 1/retrieveElement(i, i));
		}

		// Create L & U matrix, by assigning matrix sp's upper and lower diagonal values to them
		for(int i=0; i<rank; i++) {
			for(int j=i+1; j<rank; j++) {
				U.matrixSetter(i, j, (-1)*retrieveElement(i, j));
				L.matrixSetter(j, i, (-1)*retrieveElement(j, i));
			}
		}
	}

	/**Keep function invariants true: 0 <= index < size*/
	public void assertInd(int i, int j) {
		assert i>=0 && i<rank;
		assert j>=0 && j<rank;
	}
	
	

	/**Switch row[i] and row[j] for Sparse Matrix A. (Return 0 on success, 1 on failures)*/
	public int rowPermute(int i, int j) {
		try {
			// Keep function invariants true
			assertInd(i ,j);

			// Create helper data structures for row permuting
			ArrayList<Double> value_new = new ArrayList<Double>();
			ArrayList<Integer> colInd_new = new ArrayList<Integer>();
			ArrayList<Integer> rowPtr_new = new ArrayList<Integer>();

			// Ensure that in the following implementations, i < j
			if(i>j) {
				int temp = i;
				i = j;
				j = temp;
			}

			/*Update value and colInd: (Utilizing ArrayList's high efficiency in search-by-index)*/ 
			// Re-arrange corresponding values and column indexes, by separating the arrays to be
			// [before i], [i,i+1], [between i & j], [j,j+1], [After j]
			// Only switch [i,i+1], [j,j+1], with the other sections untouched
			value_new.addAll(value.subList(rowPtr.get(0), rowPtr.get(i)));
			value_new.addAll(value.subList(rowPtr.get(j), rowPtr.get(j+1)));
			value_new.addAll(value.subList(rowPtr.get(i+1), rowPtr.get(j)));
			value_new.addAll(value.subList(rowPtr.get(i), rowPtr.get(i+1)));
			value_new.addAll(value.subList(rowPtr.get(j+1), value.size()));
			colInd_new.addAll(colInd.subList(rowPtr.get(0), rowPtr.get(i)));
			colInd_new.addAll(colInd.subList(rowPtr.get(j), rowPtr.get(j+1)));
			colInd_new.addAll(colInd.subList(rowPtr.get(i+1), rowPtr.get(j)));
			colInd_new.addAll(colInd.subList(rowPtr.get(i), rowPtr.get(i+1)));
			colInd_new.addAll(colInd.subList(rowPtr.get(j+1), colInd.size()));

			/*Update rowPtr: */
			// Respectively representing non-zero elements of row i and row j
			int r1_l = rowPtr.get(i+1) - rowPtr.get(i);
			int r2_l = rowPtr.get(j+1) - rowPtr.get(j);

			// Iff row i and j's non-zero elements are of the same amount, then make no change to rowPtr
			if(r1_l == r2_l) {
				rowPtr_new = rowPtr;
			}

			// Iff row i and j's non-zero elements are of different amounts
			// The rowPtr could be separated into sections of [before i], [between i & j], [After j]
			// Only need to update [between i & j], the head and tail don't need updates
			else {
				// Add [before i] of rowPtr to the new rowPtr
				rowPtr_new.addAll(rowPtr.subList(0, i+1));

				// The amount difference between row i and j
				int gap = r2_l-r1_l;

				// Increment every element in [between i & j] by (r2_l - r1_l)
				for(int k=i+1; k<j+1; k++) {
					rowPtr_new.add(rowPtr.get(k) + gap);
				}

				// Add [After j] of rowPtr to the new rowPtr
				rowPtr_new.addAll(rowPtr.subList(j+1, rowPtr.size()));
			}

			// Update the object's data structures with the new values.
			value = value_new;
			rowPtr = rowPtr_new;
			colInd = colInd_new;

			// Release the space for helper data structures, left the objects to be garbage collected automatically
			value_new = null;
			rowPtr_new = null;
			colInd_new = null;

			return 0;
		}catch(Exception e){
			System.out.println(e.getMessage());
			return 1;
		}
	}

	/**Add a*row[i] to row[j] for Sparse Matrix A. (Return 0 on success, 1 on failures)*/
	public int rowScale(int i, int j, double a) {
		try {
			// Keep function invariants true
			assertInd(i ,j);

			// Iteratively set row(j)'s element to be a*row[i]+row[j], setter is implemented as helper function below
			for(int k=0; k<rank; k++) {
				matrixSetter(j, k, retrieveElement(i, k)*a + retrieveElement(j, k));
			}
			return 0;
		}catch(Exception e) {
			System.out.println(e.getMessage());
			return 1;
		}
	}
}