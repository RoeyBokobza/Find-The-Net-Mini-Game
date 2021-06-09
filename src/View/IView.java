package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;

import java.util.Observer;

public interface IView extends Observer {
    void setViewModel(MyViewModel viewModel);
    void generateMaze(int rows, int cols);
    void setProperties(String sol, String gen, String nThreads);
    void stopEffect();
    void exitGame(ActionEvent actionEvent);
}

