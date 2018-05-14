
/**
 * ModelClass.java, ECE4960-P3-modified
 * Created by Yuan He(yh772) on 2018/05/13
 * Platform: Java 8, Eclipse, MacOS
 * Copyright © 2018 Yuan He. All rights reserved.
 * 
 * Provide interfaces for pass in method handler(pointer).
 * For the purpose of top-down programming
 */

/**@Interface: provide method handler for difference models*/
interface ModelInterface {
	public double error(Vector para, Vector x);
}

/**@Protected_Class: EKVModel*/
class EKVModel implements ModelInterface {
	static final double VT = 0.026;
	
	/**Function: calculate a single value of Y_model, with given parameters, and a pair of (Vgs, Vds)
	 * Parameters: Vector para = [Is, kappa, Vth], double Vgs, double Vds
	 * Return: double res, ekvModel's output*/
	@Override
	public double error(Vector para, Vector x) {
		double Vgs = x.v[0];
		double Vds = x.v[1];
		double Ids = x.v[2];
		
		double expo1 = Math.exp((para.v[1]*(Vgs - para.v[2])) / (2*VT));
		double expo2 = Math.exp((para.v[1]*(Vgs - para.v[2]) - Vds) / (2*VT));
		double Ids_calculated = para.v[0] * (Math.log(1+expo1) * Math.log(1+expo1) - Math.log(1+expo2) * Math.log(1+expo2));
		
		// return the second norm error
		return Math.pow(Ids - Ids_calculated,2);
	}

}

/**@Protected_Class: EKVModel*/
class PowerLawModel implements ModelInterface {
	
	@Override
	public double error(Vector para, Vector x) {
		double y_calculated = para.v[0]*Math.pow(x.v[0], para.v[1]);
		double y = x.v[1];
		return Math.pow(y - y_calculated,2);
	}

}

/**@ProtectedClass: constructing the return type for */
class paraReturn{
	Vector para;
	int lineConv;
	int QuadConv;
	int count;
	public paraReturn(Vector para, int lineConv, int QuadConv, int count) {
		this.para = para;
		this.lineConv = lineConv;
		this.QuadConv = QuadConv;
		this.count = count;
	}
}

