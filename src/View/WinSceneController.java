package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WinSceneController implements Initializable {
    @FXML
    private Button WinBut;
    private IView myView;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void afterWinButton(ActionEvent actionEvent) throws IOException {
        WinBut.getScene().getWindow().hide();
        this.myView.stopEffect();
        Stage afterWinWindow = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("AfterWinScene.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Scene scene = new Scene(root,400 ,200);
        afterWinWindow.setScene(scene);
        afterWinWindow.show();
    }

    public void setMainView(IView view){
        this.myView = view;
    }
}
