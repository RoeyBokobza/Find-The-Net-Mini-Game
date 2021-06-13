package Model;

import algorithms.search.AState;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Observer;

public interface IModel {
    void generateMaze(int row, int col);

    int[][] getMaze();

    void solveMaze();

    int[][] getSolution();

    void updateCharacterLocation(KeyCode key);

    void updateCharacterLocationMouse(MouseEvent mouseEvent, double locX, double locY, double height, double width);

    int getRowChar();

    int getColChar();

    void saveGame(File file);

    void loadGame(File file);

    void Close();

    void assignObserver(Observer o);

    int getRowCharGoal();

    int getColCharGoal();

    void setProperties(String sol, String gen, String nThreads);
}
