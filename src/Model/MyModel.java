package Model;

import Client.*;
import IO.MyDecompressorInputStream;
import Server.*;
import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


public class MyModel extends Observable implements IModel {
    private Server mazeGenerator, mazeSolver;
    private int[][] maze;
    private int rowChar, colChar;
    private Solution mazeSolution;


    public MyModel(){
        mazeGenerator = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        mazeSolver = new Server(5400, 1000, new ServerStrategySolveSearchProblem());
        mazeGenerator.start();
        mazeSolver.start();
        maze = null;
        mazeSolution = null;
    }

    @Override
    public int[][] getMaze() {
        return maze;
    }

    @Override
    public int getRowChar() {
        return rowChar;
    }

    @Override
    public int getColChar() {
        return colChar;
    }

    public void Reopen(){
        mazeGenerator = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        mazeSolver = new Server(5400, 1000, new ServerStrategySolveSearchProblem());
        mazeGenerator.start();
        mazeSolver.stop();
    }

    public void Close(){
        mazeGenerator.stop();
        mazeSolver.stop();
    }

    @Override
    public void saveGame(String fileName){
        //need to save the game to fileName location.
        //suffix of .Maze
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    public void ChangeSolveAlgorithm(String algName){
        //change the solving algorithm in the config file and reopen the server
    }

    public void ChangeGenerateAlgorithm(String genName){
        //change the generating algorithm in the config file and reopen the server
    }

    @Override
    public void generateMaze(int rows, int cols){
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer,
                                           OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new
                                ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new
                                ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rows, cols};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[(rows*cols)/10 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressedmaze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        Maze tmp = new Maze(decompressedMaze);
                        maze = tmp.getMatrix();
                        rowChar = tmp.getStartPosition().getRowIndex();
                        colChar = tmp.getGoalPosition().getColumnIndex();
                        setChanged();
                        notifyObservers();
//execute maze on view object
//print on the canvas
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void solveMaze(){
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        toServer.flush();
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        setChanged();
                        notifyObservers();
//Print Maze Solution retrieved from the server
//execute solution on view object
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<AState> getSolution() {
        return mazeSolution.getSolutionPath();
    }

    @Override
    public void updateCharacterLocation(KeyCode key){
        /*
            key = 8 => Up
            key = 6 => Right
            key = 2 => Down
            key = 4 => Left
            key = 9 => Up,Right / Right,Up
            key = 3 => Down,Right / Right,Down
            key = 1 => Down,Left / Left,Down
            key = 7 => Up,Left / Left,Up
         */
        switch(key){
            case NUMPAD8:
                try{
                    if(maze[rowChar - 1][colChar] == 0) {
                        rowChar = rowChar - 1;
                    }
                    break;
                }catch (IndexOutOfBoundsException outOfBoundsException){break;}
            case NUMPAD6:
                try{
                    if(maze[rowChar][colChar + 1] == 0) {
                        colChar = colChar + 1;
                    }
                    break;
                }catch (IndexOutOfBoundsException outOfBoundsException){break;}
            case NUMPAD2:
                try{
                    if(maze[rowChar + 1][colChar] == 0) {
                        rowChar = rowChar + 1;
                    }
                    break;
                }catch (IndexOutOfBoundsException outOfBoundsException){break;}
            case NUMPAD4:
                try{
                    if(maze[rowChar][colChar - 1] == 0) {
                        colChar = colChar - 1;
                    }
                    break;
                }catch (IndexOutOfBoundsException outOfBoundsException){break;}
            case NUMPAD9:
                try{
                    if(maze[rowChar - 1][colChar + 1] == 0) {
                        if(maze[rowChar][colChar + 1] == 0 || maze[rowChar - 1][colChar] == 0) {
                            rowChar = rowChar - 1;
                        }
                    }
                    break;
                }catch (IndexOutOfBoundsException outOfBoundsException){break;}
            case NUMPAD3:
                try{
                    if(maze[rowChar + 1][colChar + 1] == 0) {
                        if(maze[rowChar][colChar + 1] == 0 || maze[rowChar + 1][colChar] == 0) {
                            rowChar = rowChar - 1;
                        }
                    }
                    break;
                }catch (IndexOutOfBoundsException outOfBoundsException){ break;}
            case NUMPAD1:
                try{
                    if(maze[rowChar + 1][colChar - 1] == 0) {
                        if(maze[rowChar][colChar - 1] == 0 || maze[rowChar + 1][colChar] == 0) {
                            rowChar = rowChar - 1;
                        }
                    }
                    break;
                }catch (IndexOutOfBoundsException outOfBoundsException){break;}
            case NUMPAD7:
                try{
                    if(maze[rowChar - 1][colChar - 1] == 0) {
                        if(maze[rowChar][colChar - 1] == 0 || maze[rowChar - 1][colChar] == 0) {
                            rowChar = rowChar - 1;
                        }
                    }
                    break;
                }catch (IndexOutOfBoundsException outOfBoundsException){break;}
        }
        setChanged();
        notifyObservers();
    }

}
