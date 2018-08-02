package cs3331.hw4;

/**
 * Contains the model for the Connect Five board. (No GUI elements should placed here.)
 *
 * @author Andrea Torres
 */
public class Board {
    private Square[][] tiles;
    private boolean[][] isFilled;
    private int counter = 0;
    private boolean boardWon=false;
    private int winner = 0;

    /**
     * Defines the size of the board
     */
    private final int size;

    /**
     * Constructor including size of board
     *
     * @param size Board Size
     */
    public Board(int size) {
        this.size = size;
        tiles= new Square[size][size];
        isFilled=new boolean[size][size];
        // Your Code Goes Here!
    }

    /**
     * Adds a disc to the game board.
     *
     * @param x x coordinate of where the disc needs to be placed.
     * @param y y coordinate of where the disc needs to be placed.
     */
    public void addDisc(int x, int y, int player) throws InValidDiskPositionException {
        if (isValidPosition(x, y)) {

            tiles[y][x] = new Square(x, y, player);
            isFilled[y][x] = true;
            counter++;
            if (checkForWin(tiles[y][x])) {
                boardWon=true;
                winner = player;

            }

        } else {
            throw new InValidDiskPositionException();
        }
    }
    public boolean getBoardWon(){
        return boardWon;
    }

    /**
     * Checks if input positions is valid. Checks if valid x-y range. Also checks if position is empty.
     *
     * @param x x input.
     * @param y y input.
     * @return Validity of placement of the disc.
     */
    boolean isValidPosition(int x, int y) {
        // Your Code Goes Here!
        return !isFilled[y][x];
    }

    public Square getTiles(int x,int y){
        try {
            return tiles[y][x];
        }catch(NullPointerException e){
            return new Square(x,y,null);
        }
    }

    /**
     * Returns the size of this board.
     *
     * @return Returns size of board
     */
    public int size() {
        return size;
    }

    /**
     * This method is used to check for a tie
     *
     * @return true if tie, false otherwise
     */
    public boolean isBoardFull() {
        return counter >= Math.pow(size, 2);
    }

    private boolean checkForWin(Square square) {
        if (((checkWinHelper(square.getX(), square.getY(), square.getPlayer(),0,-1) + checkWinHelper(square.getX(), square.getY(),square.getPlayer(),0,1))) >= 6) {
            // System.out.println("Current Points:" + currpoints);
            return true;
        }
        if (checkWinHelper(square.getX(), square.getY(), square.getPlayer(),-1,0) + checkWinHelper(square.getX(), square.getY(), square.getPlayer(),1,0) >= 6)
            return true;
        if (checkWinHelper(square.getX(), square.getY(), square.getPlayer(),-1,-1) + checkWinHelper(square.getX(), square.getY(), square.getPlayer(),1,1) >= 6)
            return true;
        return checkWinHelper(square.getX(), square.getY(), square.getPlayer(),-1,1) + checkWinHelper(square.getX(), square.getY(), square.getPlayer(),1,-1) >= 6;
    }
    private int checkWinHelper(int x, int y,int player,int dx,int dy){
        try {
            if (tiles[y][x].getPlayer() == player) {
                return 1 + checkWinHelper(x+dx, y+dy, player,dx,dy);
            }
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            return 0;
        }
        return 0;
    }

    /**
     * This is used in the AI
     * @param x
     * @param y
     */
    public void removeTile(int x,int y){
        if(!isFilled[y][x]){
            counter--;
            tiles[y][x]=null;
            isFilled[y][x]=false;

        }
    }

    /**
     * THis is used for ai to have a copy of the current board
     * @return
     */
    public Board createDupilicateBoard(){
        Board newBoard= new Board(this.size);
        try {
            for (int i = 0; i < this.size; i++) {
                for (int j = 0; j < this.size; j++) {
                    newBoard.addDisc(j, i, this.tiles[j][j].getPlayer());
                }
            }
        }catch (Exception e){
            System.out.println("Something went wrong with duplicate board fix it quick");
        }
        return newBoard;
    }

    public Square[][] getTiles() {
        return tiles;
    }

    public int getWinner() {
        return winner;
    }
}
