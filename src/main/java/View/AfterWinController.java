package View;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;

public class AfterWinController {
    @FXML
    private Button NewMaze;
    private IView myView;

    public void OpenMyView(ActionEvent actionEvent) throws IOException {
        NewMaze.getScene().getWindow().hide();
    }

    public void setMainView(IView view){
        myView = view;
    }

    public void Exit(ActionEvent actionEvent) {
        myView.exitGame(actionEvent);
    }
}
