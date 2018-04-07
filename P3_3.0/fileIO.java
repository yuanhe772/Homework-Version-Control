import java.io.*;
import java.util.ArrayList;

/**
 * fileOp.java, ECE4960-P3
 * Created by Yuan He(yh772) on 2018/04/05
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 * 
 * Project 3, file I/O script:
 * For file reading and writing
 */
public class fileIO {

	// The output report's path
	static String reportPath = null;

	/**Function: Read 3 columns of measured NMOS data (Vgs, Vds, Ids) out of a .txt file
	 * Parameter: String path
	 * Return: a 2-d ArrayList, with each column being a separate  of Vgs, Vds, Ids*/
	public static ArrayList<ArrayList<Double>> readMeasure(String path){
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

}
