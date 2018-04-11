import java.io.*;
import java.util.ArrayList;

/**
 * fileOp.java, ECE4960-P3
 * Created by Yuan He(yh772) on 2018/04/05
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 * 
 * Project 3:
 * Program for generating measured data, and file I/O.
 * Including reading data from external files, and generating data with noise for power law validation
 * Also for the purpose of auto-report generating
 */
public class dataIO {

	/* Class invariant:*/
	// The output report's path
	static String reportPath = null;
	
	// For measuring memory usage and computational time
	static Runtime runtime = Runtime.getRuntime();

	
	/* Class methods:*/
	/**Function: Generate measure data for Power Law's parameter extraction solver's validation
	 * 			 y = c * x^m.
	 * 			 The measured data has random error of (0%, 20%), generated with the rule of: 
	 * 			 When random() returns in (0, 0.1), subtract 20% , (0.1, 0.2), subtract 16%...
	 * 			 and in (0.9, 1.0), add 20%
	 * Parameters: int length, the number of data points needed for power law
	 * 			   double a, double m
	 * Return: a 2-d Arraylist, with one column being the x, and the other one being the Y_measured*/
	public static ArrayList<ArrayList<Double>> powLawData(int length, double c, double m) {
		// Structures for storing measured data
		ArrayList<Double> x = new ArrayList<Double>();
		ArrayList<Double> y = new ArrayList<Double>();
		double xi = 0;
		double yi = 0;

		// Creating measure data with random error
		for(int i=0; i<length; i++) {
			xi = (double)(i+1)*(i+1);
			yi = c*Math.pow(xi,m);
			yi *= (1 + (Math.random() - 0.5)/2.5);
			x.add(xi);
			y.add(yi);
		}

		// Return measure data
		ArrayList<ArrayList<Double>> Measured = new ArrayList<ArrayList<Double>>();
		Measured.add(x);
		Measured.add(y);
		return Measured;
	}

	/**Function: Read measured NMOS data (Vgs, Vds, Ids) out of a outputNMOS.txt file
	 * Parameter: String path
	 * Return: a 2-d ArrayList, with each column being the measured Vgs, Vds, Ids*/
	public static ArrayList<ArrayList<Double>> readNMOS(String path){
		//Construct structures for restoring the file inputs
		ArrayList<Double> Vgs = new ArrayList<Double>();
		ArrayList<Double> Vds = new ArrayList<Double>();
		ArrayList<Double> Ids = new ArrayList<Double>();

		try {
			File file = new File(path);
			if(file.isFile() && file.exists()) { 
				// Create reading buffer
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = null;

				// Consumes first line in .txt, and ignore it
				br.readLine();

				// Split each line into [Vgs, Vds, Ids], and append them respectively to ArrayLists
				String subStr[] = new String[3];
				while((line = br.readLine()) != null) {
					subStr = line.split("\t");
					Vgs.add(Double.valueOf(subStr[0]));
					Vds.add(Double.valueOf(subStr[1]));
					Ids.add(Double.valueOf(subStr[2]));
				}
				br.close();
			} else {
				System.out.println("File doesn't exist!");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		// Construct the return value, a 2d ArrayList
		ArrayList<ArrayList<Double>> Measured = new ArrayList<ArrayList<Double>>();
		Measured.add(Vgs);
		Measured.add(Vds);
		Measured.add(Ids);
		return Measured;
	}

	/**Function: Create report
	 * Parameters: String filePath, String content
	 * Return: None*/
	public static void createReport(String filePath, String content) {
		reportPath = filePath;
		try {
			FileWriter fw = new FileWriter(filePath,true);
			fw.write(content);
			fw.close();
		} catch (IOException e) {
			System.out.println("writing failure" + e);
		}
	}

	/**Function: Writing "content" into "filePath", for the purpose of generating reports'
	 * Parameter: String content
	 * Return: None*/
	public static void output(String content) {
		try {
			FileWriter fw = new FileWriter(reportPath,true);
			fw.write(content);
			fw.close();
		} catch (IOException e) {
			System.out.println("writing failure" + e);
		}
	}

	/**Function: statistics for computational cost: computational time per operation, and measured memory usage
	 * Parameters: String eNUM(the equation for operation estimation), int eNum, long mtime
	 * Return: None*/
	public static void cost(String eNUM, int eNum, long mtime) {
		output("\n		<STATISTICS>: \n		"+mtime/eNum + " ns/operation, " + "with Measured time = " + mtime+
				"ns, Estimated # of operations = "+eNUM + " = " + eNum + ".\n		Current MEM usage = "+ 
				(runtime.totalMemory()-runtime.freeMemory()) +" bytes");
	}

}
