package Model;

import algorithms.search.AState;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.Observer;

public interface IModel {
    void generateMaze(int row, int col);
    int[][] getMaze();
    void solveMaze();
    int[][] getSolution();
    void updateCharacterLocation(KeyCode key);
    int getRowChar();
    int getColChar();
    void saveGame(String fileName);
    void assignObserver(Observer o);
    int getRowCharGoal();
    int getColCharGoal();
}
