package cs3331.hw4;

import cs3331.hw4.Computer;

public class EasyCompAI extends Computer {

    public EasyCompAI(int currPlayer, char symbol) {
        super(currPlayer, symbol);
    }

    public void setMove(int x, int y) {
//idk
        int[] coordinates= getComputerCoordinates();
        currY=coordinates[0];
        currX=coordinates[1];
    }

    public void setMove() {

        int[] coordinates= getComputerCoordinates();
        currY=coordinates[0];
        currX=coordinates[1];
    }

    @Override
    public int[] getMove() {
        return new int[]{currY,currX};
    }

    @Override
    public void setMove(int x, int y, Board board) {
        boolean validMove=false;
        //here is where we check if the points are good points
        while (!validMove) {
            try {


                setMove(x, y);
                if (board.isValidPosition(currY, currX)) {
                    //accept move
                    validMove = true;
                }
            }catch (ArrayIndexOutOfBoundsException e){}//sop
        }

    }

    public int[] getComputerCoordinates() {
        int[] coordinates=new int[2];
        coordinates[0]=(int)(Math.random()*15);
        coordinates[1]=(int)(Math.random()*15);
        System.out.println(coordinates[0] + " " + coordinates[1]);
        return coordinates;
    }


}