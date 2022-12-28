// --module-path "C:\Program Files\Java\javafx-sdk-19\lib" --add-modules javafx.controls,javafx.fxml
import javafx.animation.KeyFrame;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.canvas.*;
import javafx.animation.Timeline;
import javafx.util.Duration;


public class ConwayView {

    Stage stage;

    ConwayModel model;

    Button startButton, restartButton, stopButton, nextButton, applyButton;

    TextField sizeXField, sizeYField, frametimeField;

    Label iterationsLabel, frametimeLabel, widthLabel, heightLabel;

    Canvas canvas;

    boolean started; // used to allow/disallow the user to draw on squares

    double frametime; // used to wait between frames

    Timeline timeline; // don't think I'll use this

    public ConwayView(Stage s, ConwayModel m){
        stage = s;
        model = m;
        frametime = 0.5;
        started = false;
        InitUI();
    }

    private void InitUI(){
        iterationsLabel = new Label("Iterations: 0");
        frametimeLabel = new Label("Frame time: ");
        widthLabel = new Label("Width: ");
        heightLabel = new Label("Height: ");

        startButton = new Button("Start");
        restartButton = new Button("Restart");
        stopButton = new Button("Stop");
        nextButton = new Button("Next Iteration");
        applyButton = new Button("Apply settings");

        sizeXField = new TextField("squares");
        sizeYField = new TextField("squares");
        frametimeField = new TextField("seconds");

        canvas = new Canvas(600,600); // might need to change size later

    }

    private void drawBoard(){
        // draw the current board stored in the model, also calculate the next frame



        // end with:
        model.updateFrame();
    }
}
