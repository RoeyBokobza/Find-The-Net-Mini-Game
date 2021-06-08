package ViewModel;


import Model.*;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private IModel model;
    private int[][] maze;
    private int[][] solution;
    private int rowChar,colChar;
    private int rowCharGoal, colCharGoal;

    public MyViewModel(IModel model){
        this.model = model;
        this.model.assignObserver(this);
        this.maze = null;
        this.rowChar = 0;
        this.colChar = 0;
    }

    public int getRowChar() {
        return rowChar;
    }

    public int getColChar() {
        return colChar;
    }

    public int getRowCharGoal() {
        return rowCharGoal;
    }

    public int getColCharGoal() {
        return colCharGoal;
    }

    public void generateMaze(int row, int col) {
        model.generateMaze(row, col);
    }
    public void solveMaze(){
        model.solveMaze();
    }

    public int[][] getMaze() {
        return maze;
    }

    public int[][] getSolution() {
        return solution;
    }

    public void updateCharacterLocation(KeyCode key){
        model.updateCharacterLocation(key);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof IModel){
            if(maze == null){//generate maze
                this.maze = model.getMaze();
                this.rowChar = model.getRowChar();
                this.colChar = model.getColChar();
                this.rowCharGoal = model.getRowCharGoal();
                this.colCharGoal = model.getColCharGoal();
                this.solution = null;

            }
            else{
                int[][] tmpMaze = model.getMaze();

                if(tmpMaze == this.maze) { //move character
                    int rowChar = model.getRowChar();
                    int colChar = model.getColChar();
                    if(this.rowChar == rowChar && this.colChar == colChar){ //solve maze
                        if(model.getSolution() != null)
                            this.solution = model.getSolution();
                    }
                    else{
                        this.rowChar = rowChar;
                        this.colChar = colChar;
                    }
                }
                else{
                    this.maze = tmpMaze;
                    this.rowChar = model.getRowChar();
                    this.colChar = model.getColChar();
                    this.rowCharGoal = model.getRowCharGoal();
                    this.colCharGoal = model.getColCharGoal();
                    this.solution = null;
                }
            }

            setChanged();
            notifyObservers();
        }
    }

    public void saveGame(File chosen){
        model.saveGame(chosen);
    }

    public void exitGame(){
        model.Close();
    }

    public void loadGame(File chosen){
        model.loadGame(chosen);
    }

    public void setProperties(String sol, String gen, String nThreads){
        model.setProperties(sol,gen,nThreads);
    }
}
