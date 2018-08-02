package cs3331.hw4;

import java.awt.*;

/**
 * @author Andrea Fernanda Torres
 */
public abstract class Player {
    protected int currX;
    protected int currY;
    protected boolean isReal;
    protected char playerType;
    public boolean getIsReal(){
        return isReal;
    }
    private Color tileColor;
    private int playerName;//will be useful for the future
    private char symbol;// should be 1 or 2 will be useful for the future
   // private boolean isReal;// will be used later on once AI is implemented

    public Player(int currPlayer, char symbol) {
        this.playerName = currPlayer;
        this.symbol = symbol;
       // isReal = true;
    }
    public abstract void setMove(int x,int y);
    public abstract int[] getMove();

    public char getSymbol() {
        return symbol;
    }

    public Color getTileColor(){
        return tileColor;
    }
    public void setTileColor(Color tcolor){
        this.tileColor=tcolor;
    }

    public int getPlayerName() {
        return playerName;
    }
    public int getCurrX(){return currX;}
    public int getCurrY(){return currY;}
    //Eventually we will implement the AI stuff here or related to here that is why we have the irReal

    public char getPlayerType() {
        return playerType;
    }
    public abstract void setMove(int x,int y,Board board);
}
