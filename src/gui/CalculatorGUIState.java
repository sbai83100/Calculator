package gui;

import model.Compute;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.*;

public class CalculatorGUIState extends Application {

	private TextField screen;
	private boolean clearScreen;
	
	private String num1, operator, num2;

	private CalculatorState READFIRSTOPERAND = new ReadFirstOperand();
	private CalculatorState READSECONDOPERAND = new ReadSecondOperand();
	private CalculatorState currentState = READFIRSTOPERAND;
	
	@Override
	public void start(Stage primaryStage) {

		num1 = "";
		operator = "";
		num2 = "";

		int sceneWidth = 250, sceneHeight = 350;

		// the main pane of the GUI
		BorderPane root = new BorderPane();

		screen = new TextField();
		screen.setPrefHeight(sceneHeight / 4);
		screen.setEditable(false);
		screen.setFont(Font.font("Verdana", 20));
		root.setTop(screen);

		GridPane keypad = new GridPane();

		Button[] numbers = new Button[10];
		for (int i = 1; i < 10; i++) {
			numbers[i] = new Button("" + i);
			numbers[i].setOnAction(e -> {
				currentState.digit(((Button) e.getSource()).getText());
			});
			numbers[i].setPrefSize(sceneWidth / 4, sceneHeight / 8);

			keypad.add(numbers[i], (i-1)%3, 4 - (i-1)/3);
		}

		numbers[0] = new Button("" + 0);
		numbers[0].setOnAction(e -> {
			currentState.digit(((Button) e.getSource()).getText());
		});
		numbers[0].setPrefSize(sceneWidth / 4, sceneHeight / 8);
		keypad.add(numbers[0], 1, 5);

		Button percent = new Button("%");
		percent.setOnAction(e -> {
			currentState.unaryOperation("%");
		});
		
		percent.setPrefSize(sceneWidth / 4, sceneHeight / 8);
		keypad.add(percent, 0, 0);


		

		Button[] operations = {new Button("+"), 
				new Button("-"), new Button("*"), new Button("/")};
		for (int i = 0; i < 4; i++) {
			operations[i].setOnAction(e -> {
				currentState.binaryOperation(((Button) e.getSource()).getText());
			});
			
			operations[i].setPrefSize(sceneWidth / 4, sceneHeight / 8);
		}
		keypad.add(operations[0], 4, 4);
		keypad.add(operations[1], 4, 3);
		keypad.add(operations[2], 4, 2);
		keypad.add(operations[3], 4, 1);

		// 5 + 5 = + 5
		// 5 + 5 = 5 + 4
		// 5 + 5 + 5 =
		Button equal = new Button("=");
		equal.setOnAction(e -> {
			currentState.assign(operator);
		});

		equal.setPrefSize(sceneWidth / 4, sceneHeight / 8);
		keypad.add(equal, 4, 5);

		
		Button allClear = new Button("AC");
		allClear.setOnAction(e -> {
			screen.clear();
			num1 = "";
			operator = "";
			num2 = "";
			currentState = READFIRSTOPERAND;
		});
		allClear.setPrefSize(sceneWidth / 4, sceneHeight / 8);
		keypad.add(allClear, 1, 1);
		
		
		root.setCenter(keypad);


		Scene scene = new Scene(root, sceneWidth, sceneHeight);

		primaryStage.setTitle("Calculator");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	private class ReadFirstOperand implements CalculatorState {

		@Override
		public void digit(String x) {
			if (clearScreen) {
				screen.clear();
				num1 = "";
				clearScreen = false;
			}
			num1 += x;
			screen.appendText(x);
		}

		@Override
		public void assign(String op) {
			READSECONDOPERAND.assign(op);
		}

		@Override
		public void binaryOperation(String op) {
			operator = op;
			num2 = "";
			clearScreen = true;
			
			currentState = READSECONDOPERAND;
		}

		@Override
		public void unaryOperation(String op) {
			
			if (op.equals("%")) {
				screen.clear();
				num1 = "" + Double.parseDouble(num1) / 100.0;
				screen.appendText(num1);
			}
		}
	}

	private class ReadSecondOperand implements CalculatorState {

		@Override
		public void digit(String x) {
			if (clearScreen) {
				screen.clear();
				clearScreen = false;
			}
			num2 += x;
			screen.appendText(x);
		}

		@Override
		public void assign(String op) {
			screen.clear();

			String result = "" + Compute.getAnswer(Double.parseDouble(num1), 
					operator, Double.parseDouble(num2));
			
			
			num1 = result;
			
			screen.appendText(num1);
			clearScreen = true;
			
			currentState = READFIRSTOPERAND;
		}

		@Override
		public void binaryOperation(String op) {
			if (clearScreen) {
				screen.clear();
				clearScreen = false;
			}

			if (num1.equals("") || operator.equals("") || num2.equals("")) {
				return;
			}
			String result = "" + Compute.getAnswer(Integer.parseInt(num1), 
					operator, Integer.parseInt(num2));
	
			num1 = result;
			num2 = "";
			
			screen.appendText(num1);
			clearScreen = true;
		}

		@Override
		public void unaryOperation(String op) {
			
			if (op.equals("%")) {
				screen.clear();
				num2 = "" + Double.parseDouble(num2) / 100.0;
				screen.appendText(num2);
			}
		}
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
