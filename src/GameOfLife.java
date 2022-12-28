import javafx.application.Application;
import javafx.stage.Stage;

public class GameOfLife extends Application {

    ConwayModel model;

    ConwayView view;

    public void start(Stage stage){
        model = new ConwayModel();
        view = new ConwayView(stage, model);
    }
}
