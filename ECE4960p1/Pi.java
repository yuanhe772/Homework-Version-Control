/** Last Update: 02/16/2018; Author: Yuan He (yh772); Platform: Eclipse, Java8 */


/**The formula for calculating pi is 
 * pi = pi + (1/Math.pow(16, n)*(4/(8*n+1) - 2/(8*n+4) - 1/(8*n+5) - 1/(8*n+6))),
 * credit to David H. Bailey, Peter Borwein and Simon Plouffe.*/

public class Pi {

	static double pi = 0;
	static double prev = 1;
	static double pi2 = 0;
	static double prev2 = 1;
	static String former = null;
	static String latter = null;

	public static String pi() {
		for(double n=0; n<10000; n++) {
			if(prev != pi) {
				System.out.println("n = " + n + "   pi = " + pi);
			}
			else {
				former = Double.toString(prev);
				System.out.println("former = " + former);
				return Pi.latter(n);
			}
			prev = pi;
			pi = pi + (1/Math.pow(16, n)*(4/(8*n+1) - 2/(8*n+4) - 1/(8*n+5) - 1/(8*n+6)));
		}
		return former;
	}

	private static String latter(double n) {
		for(double m = n; m<10000; m++) {
			if(prev2!=pi2) {
				System.out.println("m = " + m + "   latter = " + pi2);
			}
			else {
				latter = Double.toString(prev2);
				System.out.println("latter = " + latter);
				return Pi.concatenate();
			}
			prev2 = pi2;
			pi2 = pi2 + (1/Math.pow(16, m)*(4/(8*m+1) - 2/(8*m+4) - 1/(8*m+5) - 1/(8*m+6)));
		}
		return former;
	}

	public static String concatenate() {
		int exponent = Integer.parseInt(latter.split("-")[1]);
		int former_point_length = (former.split("\\.")[1]).length();

		String zero = "";
		for(int i = 0; i<exponent - former_point_length - 1; i++) {
			zero = zero + "0";
		}
		String parseLatter = latter.replace(".", "").split("E")[0];
		String pi = former + zero + parseLatter;

		return pi;
	}

}
