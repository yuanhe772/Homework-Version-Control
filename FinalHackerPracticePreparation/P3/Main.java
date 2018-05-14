import java.util.ArrayList;
import java.util.Arrays;

/**
 * main.java, ECE4960-P3-modeified
 * Created by Yuan He(yh772) on 2018/04/05
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 * 
 * Project 3, the main script:
 * For executing the testings for helper functions, and task 1-7 listed on P3's hand-out, 
 * and auto-generate project 3's .txt-form report
 */

public class Main {

	static final ArrayList<Vector> powMeasure = dataIO.powLawData(10, 10, -0.5);
	static final ArrayList<Vector> NMOSMeasure = dataIO.readNMOS("outputNMOS.txt");

	public static void main(String args[]) {

		/*---------------------------- Power Law ---------------------------*/
		PowerLawModel PowerLaw = new PowerLawModel();
		double paramPL[] = {12, -0.6};
		paraReturn resPL = IterSolver.parameterExtract(new Vector(paramPL), powMeasure, PowerLaw);
		
		System.out.println(Arrays.toString(resPL.para.v));
		System.out.println(resPL.lineConv);
		System.out.println(resPL.QuadConv);
		System.out.println(resPL.count);



//		/*---------------------------- EKV ---------------------------*/
//		EKVModel EKV = new EKVModel();
//		double paramEKV[] = {1.41032e-06 , 0.613581 , 0.291886};
//		IterSolver.parameterExtract(new Vector(paramEKV), NMOSMeasure, EKV);

	}
}