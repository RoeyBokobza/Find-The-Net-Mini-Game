package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class NewMazeMenuController implements Initializable {
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public IView myView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setMainView(IView view){
        this.myView = view;
    }

    public void generateMaze(ActionEvent actionEvent) {
        int rows = Integer.valueOf(this.textField_mazeRows.getText());
        int cols = Integer.valueOf(this.textField_mazeColumns.getText());
        myView.generateMaze(rows,cols);
        textField_mazeColumns.getScene().getWindow().hide();

    }
}
