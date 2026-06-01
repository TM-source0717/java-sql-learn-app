package org.example.sqllearnapp.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.sqllearnapp.Model.SQLController;
import org.example.sqllearnapp.Model.SQLLearnIF;
import org.example.sqllearnapp.View.SQLLearnView;
import org.example.sqllearnapp.ViewModel.SQLLearnViewModel;

import java.io.IOException;

public class ApplicationMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        // 共通インスタンスの生成
        SQLController sql_controller = new SQLController();
        SQLLearnIF sql_learn_if = new SQLLearnIF(sql_controller);
        SQLLearnViewModel sql_learn_vm = new SQLLearnViewModel(sql_learn_if);

        // fxmlのLead
        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationMain.class.getResource("/org/example/sqllearnapp/hello-view.fxml"));

        fxmlLoader.setControllerFactory(requestedClass -> {
            if (requestedClass == SQLLearnView.class) {
                return new SQLLearnView(sql_learn_vm);
            }
            try {
                return requestedClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        stage.setTitle("SQL Learning App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}