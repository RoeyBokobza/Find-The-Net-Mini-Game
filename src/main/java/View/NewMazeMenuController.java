package View;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

import java.net.URL;
import java.util.ResourceBundle;

public class NewMazeMenuController implements Initializable {
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public IView myView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textField_mazeRows.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textField_mazeRows.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        textField_mazeColumns.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textField_mazeColumns.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    public void setMainView(IView view){
        this.myView = view;
    }

    public void generateMaze(ActionEvent actionEvent) {
        if(!textField_mazeColumns.getText().equals("") && !textField_mazeRows.getText().equals("")) {
            int rows = Integer.valueOf(this.textField_mazeRows.getText());
            int cols = Integer.valueOf(this.textField_mazeColumns.getText());
            if(rows > 1 && cols > 1){
                myView.generateMaze(rows, cols);
                textField_mazeColumns.getScene().getWindow().hide();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Number of Rows/Columns must be 2 or above!");
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.show();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Null argument not accepted!");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
    }
}
