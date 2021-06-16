package Model;

import Client.*;
import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import Server.*;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


public class MyModel extends Observable implements IModel {
    private Server mazeGenerator, mazeSolver;
    private Maze maze;
    private int rowChar, colChar;
    private Solution mazeSolution;
    private Configurations config = Configurations.getInstance();
    //private static Logger genLog = LogManager.getLogger();



    public MyModel() throws IOException {

        mazeGenerator = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        mazeSolver = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        mazeGenerator.start();
        mazeSolver.start();
        maze = null;
        mazeSolution = null;
    }

    @Override
    public int[][] getMaze() {
        return maze.getMatrix();
    }

    @Override
    public int getRowChar() {
        return rowChar;
    }

    @Override
    public int getColChar() {
        return colChar;
    }

    @Override
    public int getRowCharGoal() {
        return maze.getGoalPosition().getRowIndex();
    }

    @Override
    public int getColCharGoal() {
        return maze.getGoalPosition().getColumnIndex();
    }

    public void Reopen(){
        mazeGenerator.setStrategy(new ServerStrategyGenerateMaze());
        mazeSolver.setStrategy(new ServerStrategySolveSearchProblem());
    }

    public void Close(){
        mazeGenerator.stop();
        mazeSolver.stop();
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void saveGame(File chosen){
        try {
// save maze to a file
            OutputStream out = new MyCompressorOutputStream(new
                    FileOutputStream(chosen));
            out.write(maze.toByteArray());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadGame(File chosen){
        try {
            InputStream in = new MyDecompressorInputStream(new
                    FileInputStream(chosen));
            byte[] savedMazeBytes = new byte[2000000];
            in.read(savedMazeBytes);
            in.close();
            maze = new Maze(savedMazeBytes);
            rowChar = maze.getStartPosition().getRowIndex();
            colChar = maze.getStartPosition().getColumnIndex();
            setChanged();
            notifyObservers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
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
                        byte[] decompressedMaze = new byte[(rows*cols)*2 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressedmaze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        maze = new Maze(decompressedMaze);
                        rowChar = maze.getStartPosition().getRowIndex();
                        colChar = maze.getStartPosition().getColumnIndex();
                        mazeSolution = null;
                        setChanged();
                        notifyObservers();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
            //genLog.info("Client accepted :" + InetAddress.getLocalHost().toString() + " request for maze in dimensions [" + rows +","+cols+"]");
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
            try {
                //genLog.info("Client accepted :" + InetAddress.getLocalHost().toString() + " request for maze solution. Solve algorithm: " + config.getPropertyValue("mazeSearchingAlgorithm") + " and the solution length is: " + mazeSolution.getSolutionPath().size());
            }catch (Exception e){}
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int[][] getSolution() {
        if(mazeSolution != null) {
            int[][] solution = new int[maze.getRows()][maze.getColumns()];
            ArrayList<AState> path = mazeSolution.getSolutionPath();
            for (int i = 0; i < path.size(); i++) {
                solution[((Position) path.get(i).getState()).getRowIndex()][((Position) path.get(i).getState()).getColumnIndex()] = 1;
            }
            return solution;
        }
        return null;
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
        int[][] matrix = maze.getMatrix();
        switch(key){
            case NUMPAD8,DIGIT8,UP:
                try{
                    if(matrix[rowChar - 1][colChar] == 0) {
                        rowChar = rowChar - 1;
                    }
                    break;
                }catch (IndexOutOfBoundsException outOfBoundsException){break;}
            case NUMPAD6,DIGIT6,RIGHT:
                try{
                    if(matrix[rowChar][colChar + 1] == 0) {
                        colChar = colChar + 1;
                    }
                    break;
                }catch (IndexOutOfBoundsException outOfBoundsException){break;}
            case NUMPAD2,DIGIT2,DOWN:
                try{
                    if(matrix[rowChar + 1][colChar] == 0) {
                        rowChar = rowChar + 1;
                    }
                    break;
                }catch (IndexOutOfBoundsException outOfBoundsException){break;}
            case NUMPAD4, DIGIT4, LEFT:
                try{
                    if(matrix[rowChar][colChar - 1] == 0) {
                        colChar = colChar - 1;
                    }
                    break;
                }catch (IndexOutOfBoundsException outOfBoundsException){break;}
            case NUMPAD9, DIGIT9:
                try{
                    if(matrix[rowChar - 1][colChar + 1] == 0) {
                        if(matrix[rowChar][colChar + 1] == 0 || matrix[rowChar - 1][colChar] == 0) {
                            rowChar--;
                            colChar++;
                        }
                    }
                    break;
                }catch (IndexOutOfBoundsException outOfBoundsException){break;}
            case NUMPAD3,DIGIT3:
                try{
                    if(matrix[rowChar + 1][colChar + 1] == 0) {
                        if(matrix[rowChar][colChar + 1] == 0 || matrix[rowChar + 1][colChar] == 0) {
                            rowChar++;
                            colChar++;
                        }
                    }
                    break;
                }catch (IndexOutOfBoundsException outOfBoundsException){ break;}
            case NUMPAD1, DIGIT1:
                try{
                    if(matrix[rowChar + 1][colChar - 1] == 0) {
                        if(matrix[rowChar][colChar - 1] == 0 || matrix[rowChar + 1][colChar] == 0) {
                            rowChar++;
                            colChar--;
                        }
                    }
                    break;
                }catch (IndexOutOfBoundsException outOfBoundsException){break;}
            case NUMPAD7,DIGIT7:
                try{
                    if(matrix[rowChar - 1][colChar - 1] == 0) {
                        if(matrix[rowChar][colChar - 1] == 0 || matrix[rowChar - 1][colChar] == 0) {
                            rowChar--;
                            colChar--;
                        }
                    }
                    break;
                }catch (IndexOutOfBoundsException outOfBoundsException){break;}
        }
        setChanged();
        notifyObservers();
    }

    public void updateCharacterLocationMouse(MouseEvent mouseEvent, double locX, double locY, double height, double width){
        int[][] matrix = maze.getMatrix();
        if(matrix != null) {
            int row = rowChar;
            int col = colChar;
            if(!mouseEvent.isControlDown()) {
                if(mouseEvent.getY() < locY && (Math.abs(mouseEvent.getY() - locY) > height)){
                    if(mouseEvent.getX() < locX && Math.abs(mouseEvent.getX()) - locX > width){
                        try {
                            if (matrix[rowChar - 1][colChar - 1] == 0) {
                                if (matrix[rowChar][colChar - 1] == 0 || matrix[rowChar - 1][colChar] == 0) {
                                    rowChar--;
                                    colChar--;
                                }
                            }
                        } catch (IndexOutOfBoundsException outOfBoundsException) {
                        }
                    }
                    else if(mouseEvent.getX() > locX && Math.abs(mouseEvent.getX()) - locX > width){
                        try {
                            if (matrix[rowChar - 1][colChar + 1] == 0) {
                                if (matrix[rowChar][colChar + 1] == 0 || matrix[rowChar - 1][colChar] == 0) {
                                    rowChar--;
                                    colChar++;
                                }
                            }
                        } catch (IndexOutOfBoundsException outOfBoundsException) { }
                    }
                    else{
                        try {
                            if (matrix[rowChar - 1][colChar] == 0) {
                                    rowChar--;
                            }
                        } catch (IndexOutOfBoundsException outOfBoundsException) { }
                    }
                }
                else if(mouseEvent.getY() > locY && Math.abs(mouseEvent.getY() - locY) > height){
                    if(mouseEvent.getX() < locX && Math.abs(mouseEvent.getX() - locX) > width){
                        try {
                            if (matrix[rowChar + 1][colChar - 1] == 0) {
                                if (matrix[rowChar][colChar - 1] == 0 || matrix[rowChar + 1][colChar] == 0) {
                                    rowChar++;
                                    colChar--;
                                }
                            }
                        } catch (IndexOutOfBoundsException outOfBoundsException) { }
                    }
                    else if(mouseEvent.getX() > locX && Math.abs(mouseEvent.getX() - locX) > width){
                        try {
                            if (matrix[rowChar + 1][colChar + 1] == 0) {
                                if (matrix[rowChar][colChar + 1] == 0 || matrix[rowChar + 1][colChar] == 0) {
                                    rowChar++;
                                    colChar++;
                                }
                            }
                        } catch (IndexOutOfBoundsException outOfBoundsException) { }
                    }
                    else{
                        try {
                            if (matrix[rowChar + 1][colChar] == 0) {
                                rowChar++;
                            }
                        } catch (IndexOutOfBoundsException outOfBoundsException) { }
                    }
                }
                else if(mouseEvent.getX() < locX && Math.abs(mouseEvent.getX() - locX) > width){
                    try {
                        if (matrix[rowChar][colChar - 1] == 0) {
                            colChar--;
                        }
                    } catch (IndexOutOfBoundsException outOfBoundsException) { }
                }
                else if(mouseEvent.getX() > locX && Math.abs(mouseEvent.getX() - locX) > width){
                    try {
                        if (matrix[rowChar][colChar + 1] == 0) {
                            colChar++;
                        }
                    } catch (IndexOutOfBoundsException outOfBoundsException) { }
                }
                setChanged();
                notifyObservers();
            }
        }
    }

    public void setProperties(String sol,String gen,String nThreads){
        config.setGenerator(gen);
        config.setAlgorithm(sol);
        config.setThreads(nThreads);
        Reopen();
    }

}
