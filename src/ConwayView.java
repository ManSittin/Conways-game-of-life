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
            double mouseX = e.getX();
            double mouseY = e.getY();
            System.out.println("X: " + mouseX + ", Y: " + mouseY);
            if(!started){ // if started don't do anything the animation is playing
                int[] squarePositions = checkMouseGrid(mouseX, mouseY);
                System.out.println(squarePositions[0]);
                System.out.println(squarePositions[1]);
                double widthX = canvas.getWidth()/model.getSizeX();
                double widthY = canvas.getHeight()/model.getSizeY();

                if(model.updateBoardPoint(squarePositions[0], squarePositions[1])){
                    gc.fillRect(widthX*squarePositions[0], widthY*squarePositions[1], widthX, widthY);
                }
                else{
                    gc.clearRect(widthX*squarePositions[0], widthY*squarePositions[1], widthX, widthY);
                }
                drawGrid();
            }
        });

        exitButton.setOnMouseClicked(e-> {
            System.exit(0);
        } );

        startButton.setOnMouseClicked(e-> {
            // start the game
            if(iterations == 0) {
                originalBoard = model.cloneBoard();
            }
            started = true;
        });

        restartButton.setOnMouseClicked(e->{
            model.setBoard(originalBoard);
            iterations = 0;
            started = true;
            updateBoard();
            started = false;
        });

        stopButton.setOnMouseClicked(e-> {
            started = false;
        });

        nextButton.setOnMouseClicked(e-> {
            started = true;
            updateBoard();
            started = false;
        });

        applyButton.setOnMouseClicked(e-> {
            try{
                model.setSizeX(Integer.parseInt(sizeXField.getText()));
                model.setSizeY(Integer.parseInt(sizeXField.getText()));
                timeline.setRate(Integer.parseInt(frametimeField.getText()));

                model.initializeBoard(model.getSizeX(), model.getSizeY());
                clearCanvas();
                drawGrid();
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
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(15);

        System.out.println(canvas.getWidth());
        System.out.println(canvas.getHeight());

        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.show();

    }
    
    private void drawGrid(){
        gc.setStroke(Color.BLACK); // just in case for now, need to go through all drawing commands later to simplify
        gc.strokeRect(0,0, canvas.getWidth(), canvas.getHeight());
        // need to calculate square width
        double widthX = canvas.getWidth()/model.getSizeX();
        double widthY = canvas.getHeight()/model.getSizeY();

        for(int i = 1; i < model.getSizeX(); i++){
            // start drawing vertical lines from the left, on the right side
            // don't need the number of squares, need one less, so it starts at 1
            gc.strokeLine(widthX * i,0, widthX * i, canvas.getHeight());
        }

        for(int j = 1; j < model.getSizeY(); j++){
            gc.strokeLine(0, widthY * j, canvas.getWidth(), widthY * j);
        }
    }

    private void updateBoard(){
        // draw the current board stored in the model, also calculate the next frame
        if(started) {
            clearCanvas();
            // draw the accurate frame
            //drawGrid(); I think this is better after drawing the board
            boolean[][] board = model.getBoard();
            double widthX = canvas.getWidth()/model.getSizeX();
            double widthY = canvas.getHeight()/model.getSizeY();
            
            for(int i = 0; i < model.getSizeX(); i++){
                for(int j = 0; j < model.getSizeY(); j++){
                    if(board[i][j]){
                        gc.fillRect(widthX*i, widthY*j, widthX, widthY);
                    }
                }
            }
            drawGrid();
            // end with:
            iterations++;
            iterationsLabel.setText("Iterations: " + iterations);
            model.updateNextBoard();
            model.confirmBoard();
        }
    }

    private int[] checkMouseGrid(double x, double y){
        double squareWidth = canvas.getWidth()/ model.getSizeX();
        double squareHeight = canvas.getHeight()/model.getSizeY();

        int posX = checkMouseOneD(x, squareWidth, model.getSizeX());
        int posY = checkMouseOneD(y, squareHeight, model.getSizeY());

        int[] pos = {posX, posY};
        return pos;
    }

    private int checkMouseOneD(double point, double intervalSize, int numIntervals){
        for(int i = 1; i <= numIntervals; i++){
            point = point - intervalSize;
            if(point < 0){
                return i - 1;
            }
        }
        return numIntervals - 1;
    }

    private void clearCanvas(){gc.clearRect(0,0, canvas.getWidth(), canvas.getHeight());}

}
