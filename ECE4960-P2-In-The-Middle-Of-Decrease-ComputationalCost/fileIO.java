import java.io.*;
import java.util.ArrayList;

/**
 * fileIO.java, ECE4960-P2
 * Created by Yuan He(yh772) on 2018/03/06
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 */
public class fileIO {

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
