package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AfterWinController {
    @FXML
    private Button NewMaze;
    public void OpenMyView(ActionEvent actionEvent) throws IOException {
        NewMaze.getScene().getWindow().hide();

    }
}
