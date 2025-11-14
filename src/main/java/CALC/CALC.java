package CALC;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CALC extends Application {

    private TextField display = new TextField();
    private double num1 = 0;
    private double memory = 0;
    private String operator = "";
    private boolean startNewNumber = true;

    @Override
    public void start(Stage primaryStage) {
        display.setFont(Font.font(18));
        display.setEditable(false);
        display.setPrefHeight(50);

        GridPane grid = createButtonGrid();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setVgap(7);
        grid.setHgap(7);

        BorderPane root = new BorderPane();
        root.setTop(display);
        BorderPane.setMargin(display, new Insets(10));
        root.setCenter(grid);

        Scene scene = new Scene(root, 370, 480);
        primaryStage.setTitle("CALCULATOR \uD83E\uDDEE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createButtonGrid() {
        GridPane grid = new GridPane();

        String[][] buttons = {
                {"MC", "7", "8", "9", "/", "sqrt"},
                {"MR", "4", "5", "6", "*", "%"},
                {"MS", "1", "2", "3", "-", "1/x"},
                {"M+", "0", "+/-", ".", "+", "="},
                {"Backspace", "CE", "C"}
        };

        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[row].length; col++) {
                String text = buttons[row][col];
                Button btn = new Button(text);
                btn.setPrefSize(60, 40);
                btn.setOnAction(e -> handleButton(text));
                grid.add(btn, col, row);
            }
        }

        return grid;
    }

    private void handleButton(String text) {
        try {
            switch (text) {
                case "C":
                    display.clear();
                    num1 = 0;
                    operator = "";
                    startNewNumber = true;
                    break;
                case "CE":
                    display.clear();
                    startNewNumber = true;
                    break;
                case "Backspace":
                    String currentText = display.getText();
                    if (!currentText.isEmpty()) {
                        display.setText(currentText.substring(0, currentText.length() - 1));
                    }
                    break;
                case "+/-":
                    if (!display.getText().isEmpty()) {
                        double val = Double.parseDouble(display.getText());
                        display.setText(String.valueOf(-val));
                    }
                    break;
                case "+":
                case "-":
                case "*":
                case "/":
                case "%":
                    if (!display.getText().isEmpty()) {
                        num1 = Double.parseDouble(display.getText());
                        operator = text;
                        startNewNumber = true;
                    }
                    break;
                case "=":
                    if (!operator.isEmpty() && !display.getText().isEmpty()) {
                        double num2 = Double.parseDouble(display.getText());
                        double result = switch (operator) {
                            case "+" -> num1 + num2;
                            case "-" -> num1 - num2;
                            case "*" -> num1 * num2;
                            case "/" -> num2 != 0 ? num1 / num2 : 0;
                            case "%" -> num1 % num2;
                            default -> 0;
                        };
                        display.setText(String.valueOf(result));
                        startNewNumber = true;
                        operator = "";
                    }
                    break;
                case "sqrt":
                    if (!display.getText().isEmpty()) {
                        double val = Double.parseDouble(display.getText());
                        display.setText(String.valueOf(Math.sqrt(val)));
                        startNewNumber = true;
                    }
                    break;
                case "1/x":
                    if (!display.getText().isEmpty()) {
                        double val = Double.parseDouble(display.getText());
                        if (val != 0) {
                            display.setText(String.valueOf(1 / val));
                            startNewNumber = true;
                        } else {
                            display.setText("❌ ERROR ❌");
                        }
                    }
                    break;
                case ".":
                    if (startNewNumber) {
                        display.setText("0.");
                        startNewNumber = false;
                    } else if (!display.getText().contains(".")) {
                        display.setText(display.getText() + ".");
                    }
                    break;
                case "MC":
                    memory = 0;
                    break;
                case "MR":
                    display.setText(String.valueOf(memory));
                    startNewNumber = true;
                    break;
                case "MS":
                    if (!display.getText().isEmpty()) {
                        memory = Double.parseDouble(display.getText());
                    }
                    break;
                case "M+":
                    if (!display.getText().isEmpty()) {
                        memory += Double.parseDouble(display.getText());
                    }
                    break;
                default:
                    if (text.matches("[0-9]")) {
                        if (startNewNumber) {
                            display.setText(text);
                            startNewNumber = false;
                        } else {
                            display.setText(display.getText() + text);
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            display.setText("❌ ERROR ❌");
            startNewNumber = true;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}