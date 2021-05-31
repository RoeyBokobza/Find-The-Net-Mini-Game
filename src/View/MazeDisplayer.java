package View;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class MazeDisplayer extends Canvas {
    private int[][] maze;
    private int playerRow = 0;
    private int playerCol = 0;
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();

    public MazeDisplayer() {
    }

    public int getPlayerRow() {
        return this.playerRow;
    }

    public int getPlayerCol() {
        return this.playerCol;
    }

    public void setPlayerPosition(int row, int col) {
        this.playerRow = row;
        this.playerCol = col;
        this.draw();
    }

    public String getImageFileNameWall() {
        return (String)this.imageFileNameWall.get();
    }

    public String imageFileNameWallProperty() {
        return (String)this.imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return (String)this.imageFileNamePlayer.get();
    }

    public String imageFileNamePlayerProperty() {
        return (String)this.imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }

    public void drawMaze(int[][] maze) {
        this.maze = maze;
        this.draw();
    }

    private void draw() {
        if (this.maze != null) {
            double canvasHeight = this.getHeight();
            double canvasWidth = this.getWidth();
            int rows = this.maze.length;
            int cols = this.maze[0].length;
            double cellHeight = canvasHeight / (double)rows;
            double cellWidth = canvasWidth / (double)cols;
            GraphicsContext graphicsContext = this.getGraphicsContext2D();
            graphicsContext.clearRect(0.0D, 0.0D, canvasWidth, canvasHeight);
            this.drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
            this.drawPlayer(graphicsContext, cellHeight, cellWidth);
        }

    }

    private void drawMazeWalls(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
        graphicsContext.setFill(Color.RED);
        Image wallImage = null;

        try {
            wallImage = new Image(new FileInputStream(this.getImageFileNameWall()));
        } catch (FileNotFoundException var15) {
            System.out.println("There is no wall image file");
        }
        for(int i = 0; i < rows; ++i) {
            for(int j = 0; j < cols; ++j) {
                if (maze[i][j] == 1) {
                    double x = (double)j * cellWidth;
                    double y = (double)i * cellHeight;
                    if (wallImage == null) {
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    } else {
                        graphicsContext.drawImage(wallImage, x, y, cellWidth, cellHeight);
                    }
                }
            }
        }

    }

    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = (double)this.getPlayerCol() * cellWidth;
        double y = (double)this.getPlayerRow() * cellHeight;
        graphicsContext.setFill(Color.GREEN);
        Image playerImage = null;

        try {
            playerImage = new Image(new FileInputStream(this.getImageFileNamePlayer()));
        } catch (FileNotFoundException var12) {
            System.out.println("There is no player image file");
        }

        if (playerImage == null) {
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        } else {
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
        }

    }
}
