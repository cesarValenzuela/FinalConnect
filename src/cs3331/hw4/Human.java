package cs3331.hw4;

public class Human extends Player {

    public Human(int currPlayer, char symbol) {
        super(currPlayer, symbol);
        super.isReal=true;
    }

    @Override
    public void setMove(int x, int y) {
        super.currX=x;
        super.currY=y;
    }

    @Override
    public int[] getMove() {
        return new int[]{this.currY,this.currX};
    }

    @Override
    public void setMove(int x, int y, Board board) {

    }
}
