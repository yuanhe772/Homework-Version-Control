/** Last Update: 02/12/2018; Author: Yuan He (yh772); Platform: Eclipse, Java8 */


public class IntOverflow {
	//Java doesn't do anything with integer overflow for either int or long primitive types and 
	//ignores overflow with positive and negative integers.
	//这里！需要report java没有catch到integer overflow 的exception！

	//Overflow了以后会正负抖动，然后
	//Writing the code in a natural way using repeated multiplication on a computer with
	//32 bit integer arithmetic, the factorials up to 12! are all correctly evaluated, but 13! gets
	//the wrong value and 17! becomes negative

	//对于overflow，它无动于衷
	public static void intOverflow() {
		Print.String("(1) Integer factorial overflow");
		int i = 1, intFactorial = 1;
		for (i = 2; i < 40; i++) {
			intFactorial *= i;
			Print.Int(intFactorial);
			if(i>=12) 
				Timer.timer(100);}


		Print.String("(2) Integer incrementation overflow ceiling is 2147483647\n"
				+ ",then jumps to -2147483648");
		for(int j = 1147483646; j<=Integer.MAX_VALUE; j+=100000000) {
			if(j > 2147483639) {
				for(int y = j; y<=Integer.MAX_VALUE; y+=1) {
					Timer.timer(10); 
					Print.Int(y); 
					if( y == -2147483645) 
						return;
				}
			}
		}
	}
}
