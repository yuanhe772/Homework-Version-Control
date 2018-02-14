/** Last Update: 02/14/2018; Author: Yuan He (yh772); Platform: Eclipse, Java8 */

import java.math.BigDecimal;
import java.math.MathContext;


//正负都要检查哟

// By observation, primitive type Double starts to lose precision with more than 16 mantissa digits
// therefore the ceiling for overflowing would be no more than 16 digits

public class Quad {

	// Quad has 32 digits of mantissa, therefore  
	// Assume 1 bit for sign, 110 mantissa bits, and 17 exponent bits (1 for sign and 16 for exponent)

	public static double a;
	public static double b;
	public static BigDecimal BD;
	public static String s; // String form for instantiate a Quad type number

	public static MathContext mc = new MathContext(32);

	public Quad(String str) {

		// Quad overflow, Infinity:
		if((new BigDecimal(str).
				subtract(new BigDecimal((2 - Math.pow(2,-110))*Math.pow(10, 16)))).doubleValue() >= 0 ){
			s = "Infinity";
			Print.String(s);
		}

		//Quad underflow: (how to represent 2^-110?)
		if((new BigDecimal(str).
				subtract(new BigDecimal((Math.pow(2,-110))*Math.pow(10, -14)))).doubleValue() <= 0) {
			s = "0";
			Print.String(s);
		}

		//Round up into 32-digits
		/**BigDecimal round up is not precise with "9.99999999999999999999999999999999999E35"
		 *  - 1000000000000000042420637374017961984
		 *  with string rounding up , the error is within 1/(10^32)*/
//		int mantLen = 0;
//		if(str.contains("E")) {
//			mantLen = str.split("E")[0].length();
//			if(str.contains(".")) 
//				mantLen -= 1;
//		}
//		else {
//			mantLen = str.length();
//			if(str.contains(".")) 
//				mantLen -= 1;
//		}
//		if(mantLen>=32) {
//			BigDecimal b = new BigDecimal(str);
//			BigDecimal b2 = b.round(mc);
//			str = b2.toString();
//		}
		
		BigDecimal b = new BigDecimal(str);
		BigDecimal b2 = b.round(mc);
		str = b2.toString();
		
		s = str;
	}

	public static double[] add(double x, double y){
		BigDecimal B1 = new BigDecimal(Double.toString(x));
		BigDecimal B2 = new BigDecimal(Double.toString(y));
		BigDecimal O = B1.add(B2);
		Quad q = new Quad(O.toString()); // instantiate a new Quad and check for form
		return Quad.parse(q.normal(O));
	}

	public static double[] sub(double x, double y){
		BigDecimal B1 = new BigDecimal(Double.toString(x));
		BigDecimal B2 = new BigDecimal(Double.toString(y));
		BigDecimal O = B1.subtract(B2);
		Quad q = new Quad(O.toString());
		return Quad.parse(q.normal(O));
	}

	// Parse a String into Quad
	public static double[] parse(String s) {
		String x = null; //former substring
		String y = null; // latter substring

		String[] p = s.split("E");
		String mant = p[0]; // QuadPrecision's Mantissa
		String exp; // QuadPrecision's exponent
		int mantLen = mant.length();

		if(p.length == 2) {
			exp = p[1];
			if(mantLen>16) {
				x = mant.substring(0,mantLen - 16) + "E" + exp;
				y = mant.substring(mantLen - 16, mantLen - 15) + "." + 
						mant.substring(mantLen - 15) + "E" + 
						Integer.toString((-1)*(mantLen + 1 - 16 - 2 - Integer.parseInt(exp)));}}
		else {
			if(mantLen<16) {
				x = "0";
				y = mant;}
			else {
				int formerSize = 0;
				if(s.contains(".")) {formerSize = mant.split(".")[0].length();}
				formerSize = mant.length();
				String zero = "0";
				for(int i=0; i< formerSize-17; i++) {
					zero = zero + "0";
				}
				x = mant.substring(0, 16) + zero;
				y = mant.substring(16);
			}
		}

		a = Double.parseDouble(x);
		b = Double.parseDouble(y);

		double[] o = {a,b};
		return o;
	}

	// transfer into normalized number:
	public String normal(BigDecimal b) {
		int n = Integer.toString(b.intValue()).length();
		Quad q = new Quad(b.toString());
		//s = b.toString();
		s = s.replace(".", "");
		s = s.substring(0,1) + "." + s.substring(1) + "E" + Integer.toString(n);
		return s;
	}


}
