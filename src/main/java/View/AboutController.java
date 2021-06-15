package View;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {
    public Button closeAboutButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void close(ActionEvent actionEvent) {
        closeAboutButton.getScene().getWindow().hide();
    }
}
