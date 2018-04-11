import java.util.ArrayList;

/**
 * Test.java, ECE4960-P3
 * Created by Yuan He(yh772) on 2018/04/10
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 * 
 * Project 3, Testing program:
 * For task 1, 2, 7's validation 
 * And modular testing for helper functions
 */
public class Test {

	/* Class Variants: The file inputs, and DEFINE values*/
	// The measured data for power law, and NMOS parameter extraction
	static final ArrayList<ArrayList<Double>> powMeasure = dataIO.powLawData(10, 10, -0.5);
	static final ArrayList<ArrayList<Double>> NMOSMeasure = dataIO.readNMOS("outputNMOS.txt");
	static final double VT = 0.026;
	static final int NORM = 3;
	static final int UNNORM = 4;

	/* Class Methods: The validation functions*/
	/**Function: Validation for Vgs < Vth, Y(model) should be an expo() of Vgs with κ < 1 and nearly insensitive to VDS;
	 * 			 For Vgs > Vth and Vds > V_Dsat, Y(model) should be quadratic to Vgs and insensitive to VDS;
	 * 			 For Vgs > Vth and Vds < V_Dsat, Y(model) should be quadratic to Vds;
	 * 			 And check for the accumulated 2nd-norm error of ||Y(model) - Y(approximate)||
	 * 			 Assume ||Y(model) - Y(approximate)|| < 10^-4 as success, vice versa as failure
	 * Parameters: Vector para
	 * Return: None */
	public static void testQuasiNewton(Vector para) {
		int dataSize = NMOSMeasure.get(0).size();
		double yModel = 0;
		double yApprox = 0;
		double V_Dsat = 0;
		double errorNorm = 0;

		for(int i=0; i<dataSize; i++) {
			V_Dsat = para.v[1] * (NMOSMeasure.get(0).get(i)-para.v[2]);
			yModel = iterativeSolver.yEKVModel(para, NMOSMeasure.get(0).get(i), NMOSMeasure.get(1).get(i));
			// Vgs < Vth, and Kappa < 1
			if(NMOSMeasure.get(0).get(i) < para.v[2] && para.v[1]<1) 
				yApprox = approximate1(para, NMOSMeasure.get(0).get(i), NMOSMeasure.get(1).get(i));
			// Vgs > Vth, and Vds > V_Dsat
			if(NMOSMeasure.get(0).get(i) > para.v[2] && NMOSMeasure.get(1).get(i) > V_Dsat) 
				yApprox = approximate2(para, NMOSMeasure.get(0).get(i), NMOSMeasure.get(1).get(i));
			// Vgs > Vth, and Vds < V_Dsat
			if(NMOSMeasure.get(0).get(i) > para.v[2] && NMOSMeasure.get(1).get(i) < V_Dsat)
				yApprox = approximate3(para, NMOSMeasure.get(0).get(i), NMOSMeasure.get(1).get(i));

			errorNorm += Math.pow(yModel - yApprox, 2);
		}
		errorNorm = Math.pow(errorNorm, 0.5);

		// Output whether the product is correct (by telling whether ||Y(model) - Y(approximate)|| < 10^-4)
		dataIO.output("\n	"+ (errorNorm < Math.pow(10, -4)? "PASSED" : "FAILED") +
				": Quasi-Newton's approximation validation test! By checking ||Y(model) - Y(approximate)|| < 10^-4");
	}

	/**Function: approximation for Vgs < Vth, x1 = Kappa*(Vgs-Vth) / VT, x2 = (Kappa*(Vgs-Vth)-Vds) / VT,
	 * 			log^2(1+x1) and log^2(1+x2) could be approximated with e^x1 and e^x2
	 * Parameters: Vector para
	 * Return: double approximation */
	public static double approximate1(Vector para, double Vgs, double Vds) {
		double x1= para.v[1] * (Vgs - para.v[2]) / VT;
		double x2= (para.v[1] * (Vgs - para.v[2]) - Vds) / VT;
		return para.v[0] * (Math.exp(x1) - Math.exp(x2));
	}

	/**Function: approximation for Vgs > Vth, and Vds > V_Dsat,
	 * 			log^2(1+x1) and log^2(1+x2) could be approximated with e^x1 and (x2/2)^2
	 * Parameters: Vector para
	 * Return: double approximation */
	public static double approximate2(Vector para, double Vgs, double Vds) {
		double x1= para.v[1] * (Vgs - para.v[2]) / VT;
		double x2= (para.v[1] * (Vgs-para.v[2])-Vds) / VT;
		return para.v[0] * (Math.pow(x1/2, 2) - Math.exp(x2));
	}

	/**Function: approximation for Vgs > Vth, and Vds < V_Dsat,
	 * 			log^2(1+x1) and log^2(1+x2) could be approximated with (x1/2)^2 and (x2/2)^2
	 * Parameters: Vector para
	 * Return: double approximation */
	public static double approximate3(Vector para, double Vgs, double Vds) {
		double x1= para.v[1] * (Vgs - para.v[2]) / VT;
		double x2= (para.v[1] * (Vgs-para.v[2])-Vds) / VT;
		return para.v[0] * (Math.pow(x1/2, 2) - Math.pow(x2/2, 2));
	}


}
