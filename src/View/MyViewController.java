package View;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;


public class MyViewController implements Initializable,IView, Observer {
    private MyViewModel viewModel;
    private int[][] maze = null;
    private int[][] solution = null;
    public MenuItem solveGame;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();
    private String solveSound = "Resources/soundtrack/eyal.mp3";
    private MediaPlayer mediaPlayer1;


    public MyViewController() {}

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

    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.playerRow.textProperty().bind(this.updatePlayerRow);
        this.playerCol.textProperty().bind(this.updatePlayerCol);
        this.solveGame.setDisable(true);
        //playMusic(backgroundSound);
    }


    private void playEffect(String path){
        Media m = new Media(Paths.get(solveSound).toUri().toString());
        mediaPlayer1 = new MediaPlayer(m);
        mediaPlayer1.setOnEndOfMedia(new Runnable() {
            public void run() {
                mediaPlayer1.stop();
            }
        });
        mediaPlayer1.play();
    }

    public void generateMaze(int rows, int cols) {
        viewModel.generateMaze(rows,cols);
        this.solveGame.setDisable(false);
    }

    public void solveGame(ActionEvent actionEvent) {
        viewModel.solveMaze();
    }

    public void saveGame(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Save maze");
        fc.getExtensionFilters().add(new ExtensionFilter("Maze files (*.maze)", new String[]{"*.maze"}));
        fc.setInitialDirectory(new File("./resources"));
        File chosen = fc.showSaveDialog((Window)null);
        viewModel.saveGame(chosen);
    }

    public void openFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open maze");
        fc.getExtensionFilters().add(new ExtensionFilter("Maze files (*.maze)", new String[]{"*.maze"}));
        fc.setInitialDirectory(new File("./resources"));
        File chosen = fc.showOpenDialog((Window)null);
        viewModel.loadGame(chosen);
    }

    public void exitGame(ActionEvent actionEvent) {
        viewModel.exitGame();
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

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof MyViewModel){
            if(maze == null){ //generate maze
                this.maze = viewModel.getMaze();
                this.mazeDisplayer.setRowCharGoal(viewModel.getRowCharGoal());
                this.mazeDisplayer.setColCharGoal(viewModel.getColCharGoal());
                this.solution = null;
                this.mazeDisplayer.drawMaze(maze);
                this.mazeDisplayer.requestFocus();
                this.setPlayerPosition(viewModel.getRowChar(), viewModel.getColChar());
            }
            else{
                int[][] tmpMaze = viewModel.getMaze();

                if(this.maze == tmpMaze){
                    MediaPlayer mediaPlayer1;
                    if(Integer.parseInt(playerRow.getText()) != viewModel.getRowChar() ||Integer.parseInt(playerCol.getText()) != viewModel.getColChar()){
                        int row = viewModel.getRowChar();
                        int col = viewModel.getColChar();
                        if(row == viewModel.getRowCharGoal() && col == viewModel.getColCharGoal()){
                            playEffect(solveSound);
                        }
                        setPlayerPosition(row, col);
                    }
                    else{
                        if(viewModel.getSolution() != null) {
                            this.solution = viewModel.getSolution();
                            this.mazeDisplayer.drawSolution(solution);
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
                    this.mazeDisplayer.requestFocus();
                    this.setPlayerPosition(viewModel.getRowChar(), viewModel.getColChar());
                }
            }
        }
    }

    public void openNewMazeWindow(ActionEvent actionEvent) throws IOException {
        Stage window = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("NewMaze.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Scene scene = new Scene(root1,300,200);
        window.setScene(scene);
        window.show();
        NewMazeMenuController view = fxmlLoader.getController();
        view.setMainView(this);
    }

}
