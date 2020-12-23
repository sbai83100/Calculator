package gui;

import model.Compute;

import java.text.DecimalFormat;

import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class CalculatorGUI extends Application {

	private TextField screen;
	private String num1;
	private String operator;
	private String num2;
	private String answer;

	@Override
	public void start(Stage primaryStage) {

		num1 = "";
		operator = "";
		num2 = "";
		answer = "";

		int sceneWidth = 250, sceneHeight = 350;

		// the main pane of the GUI
		BorderPane root = new BorderPane();

		screen = new TextField();
		screen.setPrefHeight(sceneHeight / 4);
		screen.setEditable(false);
		root.setTop(screen);

		GridPane keypad = new GridPane();

		Button[] numbers = new Button[10];
		for (int i = 1; i < 10; i++) {
			numbers[i] = new Button("" + i);
			numbers[i].setOnAction(new DigitButtonHandler());
			numbers[i].setPrefSize(sceneWidth / 4, sceneHeight / 8);

			keypad.add(numbers[i], (i-1)%3, 4 - (i-1)/3);
		}

		numbers[0] = new Button("" + 0);
		numbers[0].setPrefSize(sceneWidth / 4, sceneHeight / 8);
		keypad.add(numbers[0], 1, 5);

		Button[] operations = {new Button("+"), 
				new Button("-"), new Button("*"), new Button("/")};
		for (int i = 0; i < 4; i++) {
			operations[i].setOnAction(new OperatorButtonHandler());
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
			if (num1.equals("") || num2.equals("") || operator.equals("")) {
				return;
			}

			DecimalFormat format = new DecimalFormat();
			format.setDecimalSeparatorAlwaysShown(false);

			String result = "" + Compute.getAnswer(Double.parseDouble(num1), 
					operator, Double.parseDouble(num2));
			result = format.format(Double.parseDouble(result));
			answer = result;
			screen.clear();
			screen.appendText(result);

			num1 = "";
			operator = "";
			num2 = "";
		});

		equal.setPrefSize(sceneWidth / 4, sceneHeight / 8);
		keypad.add(equal, 4, 5);

		Button percent = new Button("%");
		percent.setOnAction(e -> {
			
		});
		
		percent.setPrefSize(sceneWidth / 4, sceneHeight / 8);
		keypad.add(percent, 0, 0);


		root.setCenter(keypad);


		Scene scene = new Scene(root, sceneWidth, sceneHeight);

		primaryStage.setTitle("Calculator");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	// 5 + 5 = + 5
	// 5 + 5 = 5 + 4
	// 5 + 5 + 5
	private class OperatorButtonHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {

			if (num1.equals("")) {
				if (!answer.equals("")) {
					num1 = answer;
					answer = "";
				}
				else {
					return;
				}
			}

			if (!num2.equals("")) {

				if (num1.equals("") || operator.equals("")) {
					return;
				}

				DecimalFormat format = new DecimalFormat();
				format.setDecimalSeparatorAlwaysShown(false);

				String result = "" + Compute.getAnswer(Integer.parseInt(num1), 
						operator, Integer.parseInt(num2));
				result = format.format(Double.parseDouble(result));
				screen.clear();
				screen.appendText(result);

				num1 = result;
				num2 = "";
			}

			operator = ((Button) e.getSource()).getText();
			System.out.println("Operator Entered");
		}
	}

	// 5 + 5 = + 5
	// 5 + 5 = 5 + 4
	// 5 + 5 + 5
	private class DigitButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			String number = ((Button) e.getSource()).getText();

			if (!answer.equals("") || answer.equals("") && !operator.equals(""))
				screen.clear();

			if (operator.equals(""))
				num1 += number;
			else
				num2 += number;

			screen.appendText(number);
		}
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
