/** Last Update: 02/14/2018; Author: Yuan He (yh772); Platform: Eclipse, Java8 */


public class IntDivided0 {

	public static void intDivided0() {
		int x = 1;
		try { 
			x = x/(1-1);
		}catch(ArithmeticException e) {

			System.out.println("Exception caught: " + e.getMessage());
			Main.fileChaseFW("first-level.txt", "\n2. When integer divided by 0, "
					+ "ArithmeticException: "+e.getMessage()+" detected");
		}
	}

}
