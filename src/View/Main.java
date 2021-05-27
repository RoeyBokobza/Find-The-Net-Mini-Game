package View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application implements IView{
    Button MyViewController;

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("My First Stage");
        //button=new Button("Click Me");
        MyViewController = new Button();
        MyViewController.setText("Click me");
        StackPane layout = new StackPane();
        layout.getChildren().add(MyViewController);
        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
