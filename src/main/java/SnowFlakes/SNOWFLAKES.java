package SnowFlakes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class SNOWFLAKES extends Application {

    private Canvas canvas = new Canvas(800, 700);
    private TextField iterationField = new TextField("1");

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        Label label = new Label("Iterations (1–9 PLEASE \uD83D\uDE0A):");
        CheckBox autoRefresh = new CheckBox("Auto Refresh\uD83D\uDD04");
        autoRefresh.setSelected(true);

        Button redraw = new Button("Redraw \uD83D\uDDBC\uFE0F");
        redraw.setDefaultButton(true);
        redraw.setStyle("-fx-background-color: #ADD8E6; -fx-text-fill:#000000;");

        HBox controlPanel = new HBox(10, label, iterationField, autoRefresh, redraw);
        controlPanel.setAlignment(Pos.CENTER);
        controlPanel.setStyle("-fx-padding: 10px; -fx-background-color: #ADEBB3;");

        root.setTop(controlPanel);
        root.setCenter(canvas);

        iterationField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (autoRefresh.isSelected()) drawSnowflake();
        });

        iterationField.setOnAction(e -> drawSnowflake());
        redraw.setOnAction(e -> drawSnowflake());

        drawSnowflake();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Koch Snowflake ❄\uFE0F");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void drawSnowflake() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GHOSTWHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.NAVY);
        gc.setLineWidth(5);

        int level;
        try {
            level = Integer.parseInt(iterationField.getText());
            if (level < 1) level = 1;
            if (level > 9) level = 9;
        } catch (NumberFormatException e) {
            level = 1;
        }

        double size = 400;
        double height = Math.sqrt(3) / 2 * size;

        double x1 = 200, y1 = 500;
        double x2 = x1 + size, y2 = 500;
        double x3 = x1 + size / 2, y3 = 500 - height;

        drawKoch(gc, x1, y1, x2, y2, level);
        drawKoch(gc, x2, y2, x3, y3, level);
        drawKoch(gc, x3, y3, x1, y1, level);
    }

    private void drawKoch(GraphicsContext gc, double x1, double y1, double x5, double y5, int level) {
        if (level == 1) {
            gc.strokeLine(x1, y1, x5, y5);
        } else {
            double dx = (x5 - x1) / 3;
            double dy = (y5 - y1) / 3;

            double x2 = x1 + dx;
            double y2 = y1 + dy;

            double x4 = x1 + 2 * dx;
            double y4 = y1 + 2 * dy;

            double heightFactor = Math.sqrt(3) / 6;
            double x3 = (x1 + x5) / 2 + heightFactor * (y1 - y5);
            double y3 = (y1 + y5) / 2 + heightFactor * (x5 - x1);

            drawKoch(gc, x1, y1, x2, y2, level - 1);
            drawKoch(gc, x2, y2, x3, y3, level - 1);
            drawKoch(gc, x3, y3, x4, y4, level - 1);
            drawKoch(gc, x4, y4, x5, y5, level - 1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}