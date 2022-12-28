import java.util.ArrayList;

public class ConwayModel {

    private int sizeX, sizeY;

    private ArrayList<int[]> positions;

    private boolean[][] board, nextBoard;

    public ConwayModel(){
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
        sizeX = x;
        sizeY = y;
        board = new boolean[sizeX][sizeY];
    }

    public void updateBoardPoint(int x, int y){
        // used for starting board
        board[x][y] = !board[x][y];
    }

    public void updateNextBoardPoint(int x, int y){
        // used for updating frames, might not be needed.
        nextBoard[x][y] = !board[x][y];
    }

    public void updateFrame(){
        // update nextboard with all the current points. FUCK YOU AUTOCORRECT
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                nextBoard[i][j] = checkPoint(i,j);
            }
        }
        // when done update the current board
        board = nextBoard;
    }

    private boolean checkPoint(int x, int y){
        // needs to check if the point should be alive
        int neighbors = 0;
        int checkX = 0;
        int checkY = 0;
        for(int[] p:positions){
            // count the number of neighbors
            checkX = x + p[0];
            checkY = y + p[1];
            boolean xinbounds = checkX > 0 && checkX < sizeX;
            boolean yinbounds = checkY > 0 && checkY <sizeY;
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

    public void setSizeX(int num){
        sizeX = num;
    }

    public void setSizeY(int num){
        sizeY = num;
    }

    public int getSizeX(){
        return sizeX;
    }

    public int getSizeY(){
        return sizeY;
    }


}
