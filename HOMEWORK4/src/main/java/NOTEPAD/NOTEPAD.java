package NOTEPAD;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class NOTEPAD extends Application {
    private TextArea TEXT = new TextArea();
    private File CURRENT = null;

    @Override
    public void start(Stage primaryStage) {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem newFile = new MenuItem("New File");
        MenuItem openFile = new MenuItem("Open");
        MenuItem saveFile = new MenuItem("Save");
        MenuItem saveAsFile = new MenuItem("Save As");
        MenuItem exitApp = new MenuItem("Close");
        fileMenu.getItems().addAll(newFile, openFile, saveFile, saveAsFile, new SeparatorMenuItem(), exitApp);
        menuBar.getMenus().add(fileMenu);
        newFile.setOnAction(e -> newFile());
        openFile.setOnAction(e -> openFile(primaryStage));
        saveFile.setOnAction(e -> saveFile(primaryStage));
        saveAsFile.setOnAction(e -> saveFileAs(primaryStage));
        exitApp.setOnAction(e -> primaryStage.close());
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(TEXT);

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("Notepad \uD83D\uDDD2\uFE0F");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void newFile() {
        TEXT.clear();
        CURRENT = null;
    }

    private void openFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            CURRENT = file;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                TEXT.clear();
                String line;
                while ((line = reader.readLine()) != null) {
                    TEXT.appendText(line + "\n");
                }
            } catch (IOException e) {
                showError("❌ ERROR ERROR ❌");
            }
        }
    }

    private void saveFile(Stage stage) {
        if (CURRENT != null) writeToFile(CURRENT);
        else saveFileAs(stage);
    }
    private void saveFileAs(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            CURRENT = file;
            writeToFile(file);
        }
    }

    private void writeToFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(TEXT.getText());
        } catch (IOException e) {
            showError("❌ ERROR ERROR ❌");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("❌ ERROR ❌");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}