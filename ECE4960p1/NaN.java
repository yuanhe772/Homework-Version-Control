/** Last Update: 02/15/2018; Author: Yuan He (yh772); Platform: Eclipse, Java8 */


public class NaN {

	static float NaN = FloatINF.INF * 0;
	static int flag3 = 0;

	public static void generateNaN() throws Exception {

		try{
			if(!detectNaN (NaN)) {
				flag3 = 1;
				throw new Exception("3");
			}
		}catch(Exception e){
			if(e.getMessage().toString().equals("3")) {
				System.out.println("\nPlease regenerate NaN");
			}
		}
	}

	private static boolean detectNaN(float f) {
		return f==f ? false:true;
	}

	public static void propagateNaN() {
		if(flag3 == 1)
			return;
		System.out.println("\nNaN + NaN = " + (NaN + NaN) );
		System.out.println("NaN - NaN = " + (NaN-NaN));
		System.out.println("NaN * 2 = " + (NaN*2));
		System.out.println("NaN * NaN = " + (NaN + NaN));
	}

	public static void interactNaN() {
		if(flag3 == 1)
			return;
		System.out.println("\nNaN + NINF = " + (NaN+FloatINF.NINF));
		System.out.println("NaN + INF = " + (NaN-FloatINF.NINF));
		System.out.println("NaN * NINF = " + (NaN*FloatINF.NINF));
		System.out.println("NaN / NINF = " + (NaN/FloatINF.NINF));
		System.out.println("NaN % NINF = " + (NaN%FloatINF.NINF));
		System.out.println("NaN * 0 = " + (NaN*0));
	}


}
