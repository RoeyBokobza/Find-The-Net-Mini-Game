package View;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class HelpController implements Initializable {
    public Button closeHelpButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void close(ActionEvent actionEvent) {
        closeHelpButton.getScene().getWindow().hide();
    }
}
