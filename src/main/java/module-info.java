module org.example.sqllearnapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.example.sqllearnapp.main to javafx.fxml;
    opens org.example.sqllearnapp.View to javafx.fxml;
    opens org.example.sqllearnapp.ViewModel to javafx.fxml;

    exports org.example.sqllearnapp.main;
    exports org.example.sqllearnapp.View;
    exports org.example.sqllearnapp.ViewModel;
}