
package View;

import Model.*;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    @FXML
    private Button Start_bu;
    Stage firstStage;


    public Main() {

    }

    public void start(Stage primaryStage) throws Exception {
        firstStage = primaryStage;
        firstStage.setTitle("Find The Net");
        firstStage.getIcons().add(new Image("https://upload.wikimedia.org/wikipedia/en/thumb/4/47/FC_Barcelona_%28crest%29.svg/1200px-FC_Barcelona_%28crest%29.svg.png"));
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("StartScene.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Scene startScene = new Scene(root , 600,375);
        firstStage.setScene(startScene);
        //FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("MyView.fxml"));
        //Parent root = (Parent) fxmlLoader.load();
        //  primaryStage.setScene(new Scene(root, 1000, 600));
        firstStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }


}
