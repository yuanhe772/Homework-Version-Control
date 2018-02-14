import java.math.BigDecimal;

/** Last Update: 02/14/2018; Author: Yuan He (yh772); Platform: Eclipse, Java8 */

public class Main {

	public static void main(String args[]) {

//		//1. Integer Overflow:
//		Print.String("1. Integer Overflow:");
//		IntOverflow.intOverflow();
//
//		//2. Divide by 0:
//		Print.String("2. Divide by 0:");
//		try{Divide0.divide0();}
//		catch(ArithmeticException e) {
//			Print.String("Exception caught: divided by 0");}
//
//		//3. Floating point overflows:
//		Print.String("3. Floating point Overflow:");
//		FloatOverflow.floatOverflow();
//
//		//4. INF and NINF for float point:
//		Print.String("4. Floating point INF & NINF:");
//		FloatINF.floatINF();
//
//		//5. NaN for float point:
//		Print.String("5. Floating point NaN:");
//		FloatNaN.floatNaN();
//
//		//6. Signed 0:
//		Print.String("6. Signed 0:");
//		Signed0.signed0();
//
//		//7. Floating point Gradual underflow:
//		Print.String("7. Floating point Gradual underflow:");
//		Gradual.gradual();
		
		Print.String("Application - QuadPrecision:"); // exceeding 16 digits would be rounded up to last digit
//		

		double a = 1.23456789012345E10;
		double b = 6.789012345678901E-5;
//		
		Print.Double(Quad.add(a,b)[0]);
		Print.Double(Quad.add(a,b)[1]);
		
		Print.Double(Quad.sub(a,b)[0]);
		Print.Double(Quad.sub(a,b)[1]);
		
//		new Quad("9.99999999999999999999999999999999999E35");
//		new Quad("0.12345678901234567890123456789012345678901234E-30");
		
		System.out.println(new BigDecimal("999999999999999999999999999999999999"));
		
	}

}
