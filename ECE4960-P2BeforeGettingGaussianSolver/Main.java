/**
 * Main.java, ECE4960-P2
 * Created by Yuan He(yh772) on 2018-03-04
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 */


public class Main {
	public static void main(String args[]) {
		/** Instantiate a Sparse Matrix:*/
		double value[] = {1,2,3,4,5,6,7,8,9,10,11,12};
		int rowPtr[] = {0,3,6,9,10,12};
		int colInd[] = {0,1,4,0,1,2,1,2,4,3,0,4};
		SparseMatrix sp = new SparseMatrix(value, rowPtr, colInd);

//		for(int i=0; i<sp.rank; i++) {
//			for(int j=0; j<sp.rank; j++) {
//				System.out.println(sp.retrieveElement(i, j));
//			}
//		}
//		System.out.println(sp.rank);
		
		/** Instantiate a Full Matrix:*/
		double[][] full_matrix = {{1,2,0,0,3},{4,5,6,0,0},{0,7,8,0,9},{0,0,0,10,0},{11,0,0,0,12}};
		FullMatrix fm = new FullMatrix(full_matrix);

		/** Instantiate a Vector:*/
		double[] v = {5,4,3,2,1};
		Vector vec = new Vector(v);

		/** Testing of Part 1:*/
		System.out.println("Outputs for testing part 1:\n");
		permutation1(sp,fm);
		scaling1(sp,fm);
		product1(sp,fm,vec);
		
	}
	
	//PART ONE:
		/** Part 1's permutation testing:*/
		public static void permutation1(SparseMatrix sp, FullMatrix fm) {
			if(sp.rowPermute(0, 2)==0 && fm.rowPermute(0, 2)==0) {
				System.out.println("  Part 1: "+ (norm_matrix(sp, fm)==0?"Passed":"Failed")+
						" second-norm checking for permuting row 1&3 !");
			}
			else System.out.println("Permuting method's Error");
			if(sp.rowPermute(0, 4)==0 && fm.rowPermute(0, 4)==0) {
				System.out.println("  Part 1: "+ (norm_matrix(sp, fm)==0?"Passed":"Failed")+
						" second-norm checking for permuting row 1&5 !");
			}
			else System.out.println("Permuting method's Error");
		}


		/** Part 1's scaling testing:*/
		public static void scaling1(SparseMatrix sp, FullMatrix fm) {
			if(sp.rowScale(0, 3, 3.0)==0 && fm.rowScale(0, 3, 3.0)==0) {
				System.out.println("  Part 1: "+ (norm_matrix(sp, fm)==0?"Passed":"Failed")+
						" second-norm checking for 1st time rowScaling!");
			}
			else System.out.println("RowScaling method's Error");
			if(sp.rowScale(4, 1, -4.4)==0 && fm.rowScale(4, 1, -4.4)==0) {
				System.out.println("  Part 1: "+ (norm_matrix(sp, fm)==0?"Passed":"Failed")+
						" second-norm checking for 2nd time rowScaling!");
			}
			else System.out.println("RowScaling method's Error");
		}


		/** Part 1's multiplying testing:*/
		public static void product1(SparseMatrix sp, FullMatrix fm, Vector v) {
			System.out.println("  Part 1: "+ (norm_matrix(sp, fm)==0?"Passed":"Failed")+
					" second-norm checking of multipying a sparse matrix!");
		}


		/** Part 1's second norm test function for matrix:*/
		public static double norm_matrix(SparseMatrix sp, FullMatrix fm){
			double norm = 0;
			for(int i=0; i<fm.rank; i++) {

				for(int j=0; j<fm.rank; j++) {
					norm += Math.pow((sp.retrieveElement(i, j) - fm.full[i][j]), 2);
				}
			}
			return norm;
		}


		/** Part 1's second norm test function for vector:*/
		public static double norm_vector(Vector sp, Vector fm){
			double norm = 0;
			for(int i=0; i<sp.len; i++) {
				norm += Math.pow((sp.v[i]-fm.v[i]),2);
			}
			return norm;
		}


}
