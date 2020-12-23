package gui;

public interface CalculatorState {

	public void digit(String x);
	public void assign(String op);
	public void binaryOperation(String op);
	public void unaryOperation(String op);
}
