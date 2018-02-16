/** Last Update: 02/14/2018; Author: Yuan He (yh772); Platform: Eclipse, Java8 */


public class IntOverflow {

	public static void factorial() {
		try { 
			int i = 1, factorial = 1;
			while(IntOverflow.help_fact(i, factorial) ==0 ) {
				i++;
				factorial = factorial * i;
			}
			System.out.println("For Integer overflow check, "
					+ "with factorial calculation, overflow occurs at: n="+i+"\n");
		}catch(Exception e) {
			System.out.println("Exception caught: " + e.getMessage());
		}

	}

	private static int help_fact(int i, int factorial) {
		double f = (double) factorial;
		while(f!=1) {
			f = f/i;
			i--;
			if((int)f==0) {
				return i;// meaning overflows here
			}
		}
		return 0;// meaning does not overflow
	}

}
