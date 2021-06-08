package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class StartScreenController implements Initializable {
    @FXML
    private Button Start_bu;
    private String backgroundSound = "Resources/soundtrack/background.mp3";
    private MediaPlayer mediaPlayer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playMusic(backgroundSound);
    }

    private void playMusic(String path) {
        Media m = new Media(Paths.get(path).toUri().toString());
        mediaPlayer = new MediaPlayer(m);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
        mediaPlayer.play();
    }

    public void StartGame(ActionEvent event ) throws IOException {
        Start_bu.getScene().getWindow().hide();
        Stage window = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("MyView.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Scene scene = new Scene(root,1000,600);
        window.setScene(scene);
        window.show();
        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController view = fxmlLoader.getController();
        view.setViewModel(viewModel);
        viewModel.addObserver(view);
    }


}
