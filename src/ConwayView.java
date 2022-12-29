// --module-path "C:\Program Files\Java\javafx-sdk-19\lib" --add-modules javafx.controls,javafx.fxml
import javafx.animation.KeyFrame;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.canvas.*;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.sql.Time;


public class ConwayView {

    Stage stage;

    ConwayModel model;

    Button startButton, restartButton, stopButton, nextButton, applyButton, exitButton;

    TextField sizeXField, sizeYField, frametimeField;

    Label iterationsLabel, frametimeLabel, widthLabel, heightLabel, errorLabel;

    Canvas canvas;

    GraphicsContext gc;
    boolean started; // used to allow/disallow the user to draw on squares

    boolean[][] originalBoard;
    double frametime; // used to wait between frames

    int iterations = 0;

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
        errorLabel = new Label("Error in settings");
        errorLabel.setVisible(false);

        startButton = new Button("Start");
        restartButton = new Button("Restart");
        stopButton = new Button("Stop");
        nextButton = new Button("Next Iteration");
        applyButton = new Button("Apply settings");
        exitButton = new Button("Exit");

        sizeXField = new TextField("squares");
        sizeYField = new TextField("squares");
        frametimeField = new TextField("seconds");

        HBox frametimeBox = new HBox(frametimeLabel, frametimeField);
        frametimeBox.setSpacing(10);
        HBox widthBox = new HBox(widthLabel, sizeXField);
        widthBox.setSpacing(10);
        HBox heightBox = new HBox(heightLabel, sizeYField);
        heightBox.setSpacing(10);

        canvas = new Canvas(600,600); // might need to change size later
        gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.RED);

        timeline = new Timeline(new KeyFrame(Duration.seconds(frametime), e -> updateBoard()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        canvas.setOnMouseClicked(e-> {
            // need to see what they clicked on
        });

        startButton.setOnMouseClicked(e-> {
            // start the game
            if(iterations == 0) {
                originalBoard = model.getBoard();
            }
            started = true;
        });

        restartButton.setOnMouseClicked(e->{
            model.setBoard(originalBoard);
            iterations = 0;
            updateBoard();
        });

        stopButton.setOnMouseClicked(e-> {
            started = false;
        });

        nextButton.setOnMouseClicked(e-> {
            started = true;
            updateBoard();
        });

        applyButton.setOnMouseClicked(e-> {
            try{
                model.setSizeX(Integer.parseInt(sizeXField.getText()));
                model.setSizeY(Integer.parseInt(sizeXField.getText()));
                timeline.setRate(Integer.parseInt(frametimeField.getText()));
            }catch(NumberFormatException exc){
                errorLabel.setVisible(true);
            }
        });

        HBox topControls = new HBox(frametimeBox, widthBox, heightBox, applyButton, exitButton);
        topControls.setSpacing(20);
        topControls.setAlignment(Pos.CENTER);

        HBox bottomControls = new HBox(nextButton, startButton, stopButton, restartButton);
        bottomControls.setSpacing(20);
        bottomControls.setAlignment(Pos.CENTER);

        VBox layout = new VBox(topControls, canvas, bottomControls);
        layout.setSpacing(15);

        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.show();

    }
    
    private void drawGrid(boolean empty){
        if(!empty){

        }
    }

    private void updateBoard(){
        // draw the current board stored in the model, also calculate the next frame
        if(started) {
            // draw the accurate frame
            // board is 500x500
            
            for(int i = 0; i < model.getSizeX(); i++){
                for(int j = 0; j < model.getSizeY();){

                }
            }

            // end with:
            iterations++;
            iterationsLabel.setText("Iterations: " + iterations);
            model.updateNextBoard();
        }
    }
}
