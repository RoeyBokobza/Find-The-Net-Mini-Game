import View.StartScreenController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @FXML
    private Button Start_bu;
    Stage firstStage;
    StartScreenController view;


   /* public Main() {

    }*/

    @Override
    public void stop() throws Exception {
        view.exit();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        firstStage = primaryStage;
        firstStage.setTitle("Find The Net");
        firstStage.getIcons().add(new Image("https://upload.wikimedia.org/wikipedia/en/thumb/4/47/FC_Barcelona_%28crest%29.svg/1200px-FC_Barcelona_%28crest%29.svg.png"));
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/StartScene.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Scene startScene = new Scene(root , 600,375);
        view = fxmlLoader.getController();
        firstStage.setScene(startScene);
        firstStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
