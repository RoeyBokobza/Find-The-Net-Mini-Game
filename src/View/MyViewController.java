package View;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import Model.IModel;
import Model.MyModel;
import ViewModel.*;
import algorithms.search.AState;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.FileChooser.ExtensionFilter;


public class MyViewController implements Initializable,IView, Observer {
    private MyViewModel viewModel;
    private int[][] maze = null;
    private int[][] solution = null;
    public Button solveMaze;
    //public TextField textField_mazeRows;
    //public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

    public MyViewController() { }

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public String getUpdatePlayerRow() {
        return (String)this.updatePlayerRow.get();
    }

    public void setUpdatePlayerRow(int updatePlayerRow) {
        this.updatePlayerRow.set(updatePlayerRow + "");
    }

    public String getUpdatePlayerCol() {
        return (String)this.updatePlayerCol.get();
    }

    public void setUpdatePlayerCol(int updatePlayerCol) {
        this.updatePlayerCol.set(updatePlayerCol + "");
    }

    public void generateMaze(int rows, int cols){
        viewModel.generateMaze(rows,cols);
        this.solveMaze.setDisable(false);
}
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.playerRow.textProperty().bind(this.updatePlayerRow);
        this.playerCol.textProperty().bind(this.updatePlayerCol);
        this.solveMaze.setDisable(true);
    }



    public void solveMaze(ActionEvent actionEvent) {
        viewModel.solveMaze();
    }

    public void openFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open maze");
        fc.getExtensionFilters().add(new ExtensionFilter("Maze files (*.maze)", new String[]{"*.maze"}));
        fc.setInitialDirectory(new File("./resources"));
        File chosen = fc.showOpenDialog((Window)null);
    }

    public void keyPressed(KeyEvent keyEvent) {
        viewModel.updateCharacterLocation(keyEvent.getCode());
        keyEvent.consume();
    }

    public void setPlayerPosition(int row, int col) {
        this.mazeDisplayer.setPlayerPosition(row, col);
        this.setUpdatePlayerRow(row);
        this.setUpdatePlayerCol(col);
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        this.mazeDisplayer.requestFocus();
    }

    public void ZoomIn(ActionEvent event){
        mazeDisplayer.ZoomIn();
    }

    public void ZoomOut(ActionEvent event){
        mazeDisplayer.ZoomOut();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof MyViewModel){
            if(maze == null){ //generate maze
                this.maze = viewModel.getMaze();
                this.mazeDisplayer.setRowCharGoal(viewModel.getRowCharGoal());
                this.mazeDisplayer.setColCharGoal(viewModel.getColCharGoal());
                this.solution = null;
                this.mazeDisplayer.drawMaze(maze);
                this.setPlayerPosition(viewModel.getRowChar(), viewModel.getColChar());
                this.mazeDisplayer.requestFocus();
            }
            else{
                int[][] tmpMaze = viewModel.getMaze();

                if(this.maze == tmpMaze){
                    if(Integer.parseInt(playerRow.getText()) != viewModel.getRowChar() ||Integer.parseInt(playerCol.getText()) != viewModel.getColChar()){
                        setPlayerPosition(viewModel.getRowChar(), viewModel.getColChar());
                    }
                    else{
                        if(viewModel.getSolution() != null) {
                            solution = viewModel.getSolution();
                            mazeDisplayer.drawSolution(solution);
                            this.mazeDisplayer.requestFocus();
                        }
                    }
                }
                else{
                    this.maze = tmpMaze;
                    this.mazeDisplayer.setRowCharGoal(viewModel.getRowCharGoal());
                    this.mazeDisplayer.setColCharGoal(viewModel.getColCharGoal());
                    this.solution = null;
                    this.mazeDisplayer.drawMaze(maze);
                    this.setPlayerPosition(viewModel.getRowChar(), viewModel.getColChar());
                    this.mazeDisplayer.requestFocus();
                }
            }
        }
    }

    public void openNewMazeWindow(ActionEvent actionEvent) throws IOException {
        Stage window = new Stage();
        FXMLLoader fxmlLoader11 = new FXMLLoader(this.getClass().getResource("New_Maze.fxml"));
        Parent root1 = (Parent) fxmlLoader11.load();
        Scene scene = new Scene(root1,300,200);
        window.setScene(scene);
        window.show();
        NewMazeMenuController view = fxmlLoader11.getController();
        view.setMainView(this);
    }
}
