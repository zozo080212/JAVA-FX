package MINESWEEP;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MINESWEEP extends Application {

    private static final int SIZE = 10;
    private static final int TILE_SIZE = 40;
    private Cell[][] grid = new Cell[SIZE][SIZE];
    private VBox root = new VBox(10);
    private Button resetButton = new Button("FAIL — Try Again????\uD83E\uDD14");
    private boolean gameOver = false;

    @Override
    public void start(Stage stage) {
        resetButton.setStyle("-fx-background-color: black; -fx-text-fill: red;-fx-font-size: 18px;");
        resetButton.setVisible(false);
        resetButton.setOnAction(e -> resetGame());

        root.getChildren().addAll(createGrid(), resetButton);
        stage.setTitle("Minesweeper ⛏\uFE0F");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private GridPane createGrid() {
        GridPane pane = new GridPane();

        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                Cell cell = new Cell(x, y);
                grid[x][y] = cell;
                pane.add(cell, x, y);
            }
        }
        int mines = 10;
        while (mines > 0) {
            int x = (int) (Math.random() * SIZE);
            int y = (int) (Math.random() * SIZE);
            if (!grid[x][y].MINE) {
                grid[x][y].MINE = true;
                mines--;
            }
        }

        return pane;
    }

    private void resetGame() {
        root.getChildren().clear();
        grid = new Cell[SIZE][SIZE];
        gameOver = false;
        resetButton.setVisible(false);
        root.getChildren().addAll(createGrid(), resetButton);
    }

    private void gameOver() {
        gameOver = true;
        for (int y = 0; y < SIZE; y++)
            for (int x = 0; x < SIZE; x++)
                grid[x][y].reveal(true);
        resetButton.setVisible(true);
    }

    private class Cell extends StackPane {
        int x, y;
        boolean MINE = false;
        boolean SHOWYOURSELF = false;
        boolean FLAGGED = false;

        Rectangle border = new Rectangle(TILE_SIZE, TILE_SIZE);
        Text text = new Text();

        Cell(int x, int y) {
            this.x = x;
            this.y = y;

            border.setFill(Color.GREEN);
            border.setStroke(Color.BLACK);
            text.setVisible(false);

            getChildren().addAll(border, text);

            setOnMouseClicked(e -> {
                if (gameOver) return;

                if (e.getButton() == MouseButton.SECONDARY) {
                    if (!SHOWYOURSELF) {
                        FLAGGED = !FLAGGED;
                        text.setText(FLAGGED ? "🚩" : "");
                        text.setVisible(FLAGGED);
                    }
                } else if (e.getButton() == MouseButton.PRIMARY) {
                    if (FLAGGED || SHOWYOURSELF) return;
                    reveal(false);
                    if (MINE) {
                        border.setFill(Color.RED);
                        text.setText("\uD83C\uDFAF");
                        text.setVisible(true);
                        gameOver();
                    }
                }
            });
        }

        void reveal(boolean force) {
            if (SHOWYOURSELF && !force) return;
            SHOWYOURSELF = true;
            border.setFill(Color.WHITE);
            text.setVisible(true);

            if (MINE) {
                text.setText("💣");
            } else {
                int mines = countAdjacentMines();
                if (mines > 0) {
                    text.setText(String.valueOf(mines));
                    text.setFill(Color.BLUE);
                } else {
                    text.setText("");
                }
            }
        }

        int countAdjacentMines() {
            int count = 0;
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int nx = x + dx, ny = y + dy;
                    if (nx >= 0 && ny >= 0 && nx < SIZE && ny < SIZE && grid[nx][ny].MINE) {
                        count++;
                    }
                }
            }
            return count;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
