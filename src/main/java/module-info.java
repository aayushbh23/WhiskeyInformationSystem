module cqu.wis {
    requires javafx.controls;
    requires javafx.fxml;

    opens cqu.wis to javafx.fxml;
    exports cqu.wis;
}
