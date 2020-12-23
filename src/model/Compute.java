package model;

public class Compute {

	public static double getAnswer(double num1, String operator, double num2) {
		
		if (operator.equals("+")) {
			return num1 + num2;
		}
		else if (operator.equals("-")) {
			return num1 - num2;
		}
		else if (operator.equals("*")) {
			return num1 * num2;
		}
		else if (operator.equals("/")) {
			return num1 / num2;
		}
		
		return 0;
	}
}
