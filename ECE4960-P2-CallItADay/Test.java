import java.io.*;
import java.util.ArrayList;

/**
 * Test.java, ECE4960-P2
 * Created by Yuan He(yh772) on 2018/03/06
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 */
public class Test {

	/**Test mat1 with the Jacobi solver*/
	public static void mat1() {
		// Record the start time for time-counting this method
		long startTime= System.nanoTime();

		// Initiate the Sparse Matrix A from csv files
		String filePath[] = {"value.csv", "rowPtr.csv", "colInd.csv"};
		SparseMatrix sp = Test.readMatrix(filePath);

		// Initiate vector b
		double bb1[] = new double[sp.rank];
		double bb2[] = new double[sp.rank];
		double bb3[] = new double[sp.rank];
		for(int i=0; i<sp.rank; i++) {
			bb1[i] = 0.0;
			bb2[i] = 0.0;
			bb3[i] = 1.0;
		}
		bb1[0] = 1.0;
		bb2[4] = 1.0;
		Vector b1 = new Vector(bb1);
		Vector b2 = new Vector(bb2);
		Vector b3 = new Vector(bb3);
		//		System.out.println("\nTo converge, it took iteration times of "+Jacobi.solver(sp, b1));
		//		System.out.println("\nTo converge, it took iteration times of "+Jacobi.solver(sp, b2));
		System.out.println("\nTo converge, it took iteration times of "+Jacobi.solver(sp, b3));

		// Record the end time for time-counting this method
		long endTime= System.nanoTime();
		System.out.println("\nComputational Time for soving mat1 with Jacobi Iterative Solver: "+(endTime-startTime)+" ns");

	}

	/** Writing "content" into "filePath", for writing multi-level reports' purpose*/
	public static void output(String filePath, String content) {
		try {
			FileWriter fw = new FileWriter(filePath,true);
			fw.write(content);
			fw.close();
		} catch (IOException e) {
			System.out.println("writing failure" + e);
		}
	}


	/** Read the three .csv files, return a Sparse Matrix built from them*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static SparseMatrix readMatrix(String[] filePath) {

		// Create data structures for Sparse Matrix, being "value", "rowPtr", "colInd" respectively
		ArrayList<ArrayList> L = new ArrayList<ArrayList>();
		L.add(new ArrayList<Double>());
		L.add(new ArrayList<Integer>());
		L.add(new ArrayList<Integer>());

		try {
			// Each element in filePath[] corresponds to "value", "rowPtr", "colInd" respectively
			for(int i=0; i<L.size(); i++) {
				File file = new File(filePath[i]);
				if(file.isFile() && file.exists()) { 
					BufferedReader br = new BufferedReader(new FileReader(file));
					String line = null;

					// Iff current file is "value"
					if(i==0) 
						while ((line = br.readLine()) != null) {L.get(i).add(Double.valueOf(line));}

					// Iff current file is "rowPtr", increment by 1 to apply to class SparseMatrix
					else if(i==1) 
						while ((line = br.readLine()) != null) {L.get(i).add(Integer.valueOf(line)-1);}

					// Iff current file is "colInd", increment by 1 to apply to class SparseMatrix
					else 
						while ((line = br.readLine()) != null) {L.get(i).add(Integer.valueOf(line)-1);}

					br.close();
				} else System.out.println("File doesn't exist!");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return new SparseMatrix(L.get(0), L.get(1), L.get(2));
	}
}
