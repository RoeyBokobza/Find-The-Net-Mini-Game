package ViewModel;


import Model.*;
import javafx.scene.input.KeyCode;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private IModel model;
    private int[][] maze;
    private int rowChar,colChar;

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

    public void generateMaze(int row, int col) {
        model.generateMaze(row, col);
        rowChar = model.getRowChar();
        colChar = model.getColChar();
    }
    public void solveMaze(){
        model.solveMaze();
    }

    public int[][] getMaze() {
        return maze;
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
            }
            else{
                int[][] tmpMaze = model.getMaze();

                if(tmpMaze == this.maze) { //move character
                    int rowChar = model.getRowChar();
                    int colChar = model.getColChar();
                    if(this.rowChar == rowChar && this.colChar == colChar){ //solve maze
                        //get solution
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
                }
            }

            setChanged();
            notifyObservers();
        }
    }

}
