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
import java.util.HashMap;


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

    point position;
    double frametime, offsetX, offsetY; // used to wait between frames

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

        offsetX = 0;
        offsetY = 0;

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
            double mouseX = e.getX();
            double mouseY = e.getY();
            System.out.println("X: " + mouseX + ", Y: " + mouseY);
            if (!started){
                point p = mouseToPoint(mouseX, mouseY);
                System.out.println("X: " + p.x + ", Y: " + p.y);
                model.updateTile(p);
                started = true;
                updateBoard();
                drawGrid();
                started = false;
            }

        });

        /*
        canvas.setOnMouseClicked(e-> {
            // need to see what they clicked on
            double mouseX = e.getX();
            double mouseY = e.getY();
            System.out.println("X: " + mouseX + ", Y: " + mouseY);
            if(!started){ // if started we're moving
                int[] squarePositions = checkMouseGrid(mouseX, mouseY);
                System.out.println(squarePositions[0]);
                System.out.println(squarePositions[1]);
                double widthX = canvas.getWidth()/model.getSizeX();
                double widthY = canvas.getHeight()/model.getSizeY();

                if(model.updateBoardPoint(squarePositions[0], squarePositions[1])==1){
                    gc.fillRect(widthX*squarePositions[0], widthY*squarePositions[1], widthX, widthY);
                }
                else{
                    gc.clearRect(widthX*squarePositions[0], widthY*squarePositions[1], widthX, widthY);
                }
                drawGrid();
            }
        });

        canvas.setOnMouseDragEntered(e-> {



        });

        canvas.setOnScrollStarted(e-> {
            System.out.println(e.getDeltaY());
            System.out.println(e.getMultiplierY());


        });
        */

        exitButton.setOnMouseClicked(e-> {
            System.exit(0);
        } );

        startButton.setOnMouseClicked(e-> {
            // start the game
            if(iterations == 0) {
                //originalBoard = model.cloneBoard();
                model.copyTiles(model.getCurrentTiles(), model.getOriginalTiles());
            }
            started = true;
            iterationsLabel.setVisible(true);
        });

        restartButton.setOnMouseClicked(e->{
            //model.setBoard(originalBoard);
            model.copyTiles(model.getOriginalTiles(), model.getCurrentTiles());
            started = true;
            updateBoard();
            started = false;
            iterations = 0;
            iterationsLabel.setText("Iterations: 0");

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
                //model.setSizeX(Integer.parseInt(sizeXField.getText()));
                //model.setSizeY(Integer.parseInt(sizeXField.getText()));
                model.setScale(Double.parseDouble(sizeXField.getText()));
                timeline.setRate(Integer.parseInt(frametimeField.getText()));
                model.copyTiles(model.getOriginalTiles(),model.getCurrentTiles());

                //model.initializeBoard(model.getSizeX(), model.getSizeY());
                //originalBoard = model.cloneBoard();
                clearCanvas();
                drawGrid();
                iterations = 0;
                iterationsLabel.setText("Iterations: " + iterations);

            }catch(NumberFormatException exc){
                errorLabel.setVisible(true);
            }

        });

        HBox topControls = new HBox(frametimeBox, widthBox, heightBox, applyButton, exitButton);
        topControls.setSpacing(20);
        topControls.setAlignment(Pos.CENTER);

        HBox bottomControls = new HBox(iterationsLabel, nextButton, startButton, stopButton, restartButton);
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
        double scale = model.getScale();

        // need to calculate square size, can be done with either width or height, doesn't matter very much which one
        double squareSize = getSquareSize();
        double lineOffsetX = offsetX % scale;
        double lineOffsetY = offsetY % scale;

        for(int i = 1; i < scale; i++){
            // start drawing vertical lines from the left, to the right side
            // don't need the number of squares, need one less, so it starts at 1
            gc.strokeLine(squareSize * i ,0, squareSize * i, canvas.getHeight());
        }

        for(int j = 1; j < scale; j++){
            gc.strokeLine(0, squareSize * j, canvas.getWidth(), squareSize * j);
        }
        gc.strokeRect(0,0, canvas.getWidth(), canvas.getHeight()); // outline the canvas
    }

    private double getSquareSize(){
        return canvas.getWidth()/model.getScale();
    }

    private point mouseToPoint(double mouseX, double mouseY){
        int x = (int) ((mouseX + offsetX)/getSquareSize());
        int y = (int) ((mouseY + offsetY)/getSquareSize());
        return new point(x, y);
    }

    private void updateBoard(){
        // Draw the current state of the Tiles, update nextTiles
        // draw the current board stored in the model, also calculate the next frame
        if(started) {
            clearCanvas();
            double squareSize = getSquareSize();
            HashMap<point,Integer> currTiles = model.getCurrentTiles();
            for (point p : currTiles.keySet()){
                if (currTiles.get(p) > 0){ // If the point should be drawn,
                    double posX = p.x * squareSize + offsetX; // should be left side of square
                    double posY = p.y * squareSize + offsetY; // should be top of square
                    //if () // If the point is in view, draw it red
                    gc.fillRect(posX, posY, squareSize,squareSize);
                }
            }
            // draw the accurate frame
            /*
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
             */

            drawGrid();
            // end with:
            iterations++;
            iterationsLabel.setText("Iterations: " + iterations);
            //model.updateNextTiles();
            //model.updateCurrTiles();
            //model.updateNextBoard();
            //model.confirmBoard();
        }
    }

    private int[] checkMouseGrid(double x, double y){
        // This function checks where the mouse is, and returns which point on the grid the mouse is on.
        double squareWidth = canvas.getWidth()/ model.getSizeX();
        double squareHeight = canvas.getHeight()/model.getSizeY();

        int posX = checkMouseOneDim(x, squareWidth, model.getSizeX());
        int posY = checkMouseOneDim(y, squareHeight, model.getSizeY());

        int[] pos = {posX, posY};
        return pos;
    }

    private int checkMouseOneDim(double p, double intervalSize, int numIntervals){
        for(int i = 1; i <= numIntervals; i++){
            p = p - intervalSize;
            if(p < 0){
                return i - 1;
            }
        }
        return numIntervals - 1;
    }

    private void clearCanvas(){gc.clearRect(0,0, canvas.getWidth(), canvas.getHeight());}

}
