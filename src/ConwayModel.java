import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.lang.Math;

public class ConwayModel {

    private int sizeX, sizeY;

    private double scale;

    private HashMap<point, Integer> currentTiles, nextTiles, tempTiles, originalTiles;

    private ArrayList<int[]> positions;

    private boolean[][] board, nextBoard;

    public ConwayModel(){
        currentTiles = new HashMap<point, Integer>();
        nextTiles = new HashMap<point, Integer>();
        tempTiles = new HashMap<point, Integer>();
        originalTiles = new HashMap<point, Integer>();
        scale = 10;

        positions = new ArrayList<>();

        for(int i = -1; i < 2; i++){
            for(int j = -1; j <2; j++){
                if(j != 0 || i != 0){
                    int[] currPos = new int[2];
                    currPos[0] = i;
                    currPos[1] = j;
                    positions.add(currPos);
                }
            }
        }
    }

    public void initializeBoard(int x, int y){

        /* PROBABLY WON'T NEED THIS?
        sizeX = x;
        sizeY = y;
        board = new boolean[sizeX][sizeY];
        nextBoard = new boolean[sizeX][sizeY];
        */
    }

    public void updateTile(point p){ // originally returned an int
        // used for starting board
        // ALSO PROBABLY WON'T NEED THIS?
        int i;
        //if (!currentTiles.containsKey(p)){ old method
        if (currentTiles.getOrDefault(p,0) == 0){
            currentTiles.put(p,1);
            //return 1;
        }
        else{
            currentTiles.remove(p);

            // old method changed the point value to 0 instead of removing it
            //i = currentTiles.get(p);
            //i = java.lang.Math.abs(i-1);
            //currentTiles.replace(p,i); // switch the point
            //return i; // return the new value of the point
        }

        //board[x][y] = !board[x][y];
        //return board[x][y];
    }

    public void updateNextBoardPoint(int x, int y){ // NOT USED !!!!!!!!!!!!
        // used for updating frames, might not be needed.
        nextBoard[x][y] = !board[x][y];

    }

    public void updateNextTiles() {
        int i;
        for (point p : currentTiles.keySet()){
            for(int[] pos:positions) {
                point check = new point(p.x + pos[0], p.y + pos[1]);
                i = nextTiles.getOrDefault(check,0);
                nextTiles.put(check, i + 1);
            }
        }

    }

    public void updateCurrTiles() {
        // For drawing the scene, update the currentTiles map with what the next iteration should be
        int i;
        for (point p : nextTiles.keySet()) {
            // IF IT'S A TILE ALREADY -> MUST HAVE 2 <= N <= 3 NEIGHBORS TO BE ALIVE NEXT ITERATION
            // IF IT'S NOT A TILE ALREADY -> MUST HAVE N = 3 NEIGHBORS TO BE ALIVE NEXT ITERATION
            if (currentTiles.containsKey(p)) { // does exist now
                i = nextTiles.getOrDefault(p,0);
                if (2 <= i && i <= 3) tempTiles.put(p, 1);
            } else if (nextTiles.containsKey(p)) { // doesn't exist now but has neighboring tiles
                i = nextTiles.get(p);
                if (i == 3) tempTiles.put(p, 1);
            }

        }
        // AT THIS POINT TEMPTILES SHOULD BE CORRECT,
        // COPY TEMP INTO CURR, THEN CLEAR TEMP AND NEXT
        currentTiles = new HashMap<point, Integer>();
        copyTiles(tempTiles, currentTiles);
        tempTiles = new HashMap<point, Integer>();
        nextTiles = new HashMap<point, Integer>();
    }


    public void copyTiles(HashMap<point, Integer> source, HashMap<point, Integer> dest){
        for(point p : source.keySet()) {
            if (source.get(p) != 0) {
                dest.put(p, 1);
            }
        }
    }

    public void updateNextBoard(){ // huh??
        // update nextboard with all the current points.
        // CURRENT BUG IS THAT WHILE THE ARRAYS ARE DIFFERENT, THE SUB-ARRAYS ARE SHARED SOMEHOW
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                nextBoard[i][j] = checkPoint(i,j);
            }
        }
    }

    public void confirmBoard(){ // huh??
        for(int i = 0; i < board.length; i++){
            board[i] = nextBoard[i].clone();
        }
    }

    public HashMap<point, Integer> getCurrentTiles(){
        return this.currentTiles;
    }

    public HashMap<point, Integer> getNextTiles(){
        return this.nextTiles;
    }

    public HashMap<point, Integer> getTempTiles(){
        return this.tempTiles;
    }

    public HashMap<point, Integer> getOriginalTiles(){
        return this.originalTiles;
    }

    public void setScale(double s){
        this.scale = s;
    }

    public double getScale(){
        return this.scale;
    }

    private boolean checkPoint(int x, int y){ // NOT NEEDED
        // needs to check if the point should be alive
        int neighbors = 0;
        int checkX = 0;
        int checkY = 0;
        for(int[] p:positions){
            // count the number of neighbors
            checkX = x + p[0];
            checkY = y + p[1];
            boolean xinbounds = checkX >= 0 && checkX < sizeX;
            boolean yinbounds = checkY >= 0 && checkY <sizeY;
            if(xinbounds && yinbounds){
                // if the point being checked is inbounds
                if(board[checkX][checkY]){neighbors++;}
                // if it's alive add to neighbors, otherwise nothing
            }
        }
        if(board[x][y]){
            return 1 < neighbors && neighbors < 4;
        }
        else { return neighbors == 3;}
    }

    public boolean[][] cloneBoard() { // NOT NEEDED
        boolean[][] newBoard = new boolean[sizeX][sizeY];
        for(int i = 0; i < board.length; i++){
            newBoard[i] = board[i].clone();
        }
        return newBoard;
    }

    public void setSizeX(int num){ // NOT NEEDED
        sizeX = num;
    }

    public void setSizeY(int num){ // NOT NEEDED
        sizeY = num;
    }

    public int getSizeX(){ // NOT NEEDED
        return sizeX;
    }

    public int getSizeY(){ // NOT NEEDED
        return sizeY;
    }

    public void setBoard(boolean[][] b){ // NOT NEEDED
        board = b.clone();
    }

    public boolean[][] getBoard(){ // NOT NEEDED
        return board;
    }


}
