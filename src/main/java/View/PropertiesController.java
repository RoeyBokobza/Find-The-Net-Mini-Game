package View;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

import java.net.URL;
import java.util.ResourceBundle;

public class PropertiesController implements Initializable {
    private IView myView;
    public ChoiceBox choiceSolve;
    public ChoiceBox choiceGenerator;
    public TextField nThreadsField;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> availableChoices = FXCollections.observableArrayList("BestFirstSearch", "BreathFirstSearch","DepthFirstSearch");
        choiceSolve.setItems(availableChoices);
        availableChoices = FXCollections.observableArrayList( "SimpleMazeGenerator","MyMazeGenerator");
        choiceGenerator.setItems(availableChoices);
        nThreadsField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    nThreadsField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }


    public void setMainView(IView view){
        this.myView = view;
    }

    public void setProperties(ActionEvent actionEvent) {
        if(nThreadsField.getText() == "") {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Number of threads must be 1 or above!");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
        else
            myView.setProperties("" + choiceSolve.getValue(),"" + choiceGenerator.getValue(), nThreadsField.getText());
        nThreadsField.getScene().getWindow().hide();
    }
}

