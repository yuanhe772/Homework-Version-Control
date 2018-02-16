/** Last Update: 02/15/2018; Author: Yuan He (yh772); Platform: Eclipse, Java8 */


public class FloatINF {

	static float INF = (float) (1.0/0);
	static float NINF = (float) (-1.0/0);
	static int flag1 = 0;
	static int flag2 = 0;

	public static void generateINF() throws Exception {

		try{
			if(! FloatINF.checkINF(INF)) {
				flag1 = 1;
				throw new Exception("1");
			}
			if( ! FloatINF.checkINF(NINF)) {
				flag2 = 1;
				throw new Exception("2");
			}
		}catch(Exception e){
			if(e.getMessage().toString().equals("1")) {
				System.out.println("\nPlease regenerate the INF");
			}
			else if(e.getMessage().toString().equals("2")) {
				System.out.println("\nPlease regenerate the NINF");
			}
			
		}
	}

	private static boolean checkINF(float f) {
		if (f > 0) 
			return 1/f==0 ? true:false;
		else 
			return 1/f==0 ? true:false;
	}

	public static void divideINF(){
		if(flag1 == 1 || flag2 == 1)
			return;
		System.out.println("\n1/INF = " + 1/INF);
		System.out.println("1/NINF = " + 1/NINF);
		return;
	}
	
	public static void sinINF(){
		if(flag1 == 1 || flag2 == 1)
			return;
		System.out.println("\nsin(INF) = " + 1/INF);
		System.out.println("sin(NINF) = " + 1/NINF);
		return;
	}
	
	public static void expINF(){
		if(flag1 == 1 || flag2 == 1)
			return;
		System.out.println("\nexp(INF) = " + Math.exp(INF));
		System.out.println("exp(NINF) = " + Math.exp(NINF));
		return;
	}
	
	public static void propagateINF() {
		if(flag1 == 1 || flag2 == 1)
			return;
		System.out.println("\nINF + INF = " + (INF + INF) );
		System.out.println("INF - INF = " + (INF-INF));
		System.out.println("INF * 2 = " + (INF*2));
		System.out.println("NINF + NINF = " + (NINF + NINF));
		System.out.println("NINF - NINF = " + (NINF-NINF));
		System.out.println("NINF * 2 = " + (NINF*2) );
	}
	
	public static void interactINF() {
		if(flag1 == 1 || flag2 == 1)
			return;
		System.out.println("\nINF + NINF = " + (INF+NINF));
		System.out.println("INF - NINF = " + (INF-NINF));
		System.out.println("NINF + INF = " + (NINF-INF));
		System.out.println("INF * NINF = " + (INF*NINF));
		System.out.println("INF / NINF = " + (INF/NINF));
		System.out.println("INF % NINF = " + (INF%NINF));
		System.out.println("INF * 0 = " + (INF*0));
		System.out.println("0 * NINF = " + (0*NINF));
		
	}
	
}
