/** Last Update: 02/15/2018; Author: Yuan He (yh772); Platform: Eclipse, Java8 */


public class Gradual {

	static float normal = Float.MIN_NORMAL;
	static float x = normal/2;
	static float y = normal;

	public static void generateXY() {
		while ( x>0 && y>0 && x-y!=0 && y/x!=1) {
			System.out.println(x+"    "+y +"   " +Float.MIN_NORMAL);
			x = x/10;
			y = y/10;
		}
		//要往report里面写gradual underflow detected
	}

	public static void XsubY() {
		System.out.println("X - Y = " + (x-y));
	}

	public static void XdividedY() {
		System.out.println("X / Y = " + (x/y));
	}

	public static void sinx_x(){
		x = normal;
		float prev = (float) (Math.sin(1.23456789012345*x)/x);
		float prevfloat = 0;
		float curr = (float) (Math.sin(1.23456789012345*x)/x);
		while ( x>0 ) {
			prevfloat = x;
			prev = (float) (Math.sin(1.23456789012345*x)/x);
			if(prev!=curr) {
				System.out.println("single-precision float's precision"
						+ " decreases at value =" + x);
			}

			System.out.println(x+"    "
					+ Math.sin(1.23456789012345*x)/x);

			x = x/10;
			curr = (float) (Math.sin(1.23456789012345*x)/x);
		}

		System.out.println("When normalized smallest single-precision float divided by 10^n, denormalized bottom is " + prevfloat);

	}


}
