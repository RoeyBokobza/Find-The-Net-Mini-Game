//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public Main() {
    }

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("MyView.fxml"));

        Parent root = (Parent)fxmlLoader.load();
        primaryStage.setTitle("Maze Application");
        primaryStage.setScene(new Scene(root, 1000.0D, 700.0D));
        primaryStage.show();

        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController view = fxmlLoader.getController();
        view.setViewModel(viewModel);
        viewModel.addObserver(view);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
