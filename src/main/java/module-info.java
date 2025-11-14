module SNOWFLAKES {
    requires javafx.controls;
    requires javafx.fxml;



    opens SnowFlakes to javafx.fxml;
    exports SnowFlakes;
    opens CALC to javafx.fxml;
    exports CALC;
    opens MINESWEEP to javafx.fxml;
    exports MINESWEEP;
    opens NOTEPAD to javafx.fxml;
    exports NOTEPAD;


}