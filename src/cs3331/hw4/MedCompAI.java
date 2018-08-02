package cs3331.hw4;

public class MedCompAI extends Computer {

    public MedCompAI(int currPlayer, char symbol) {
        super(currPlayer, symbol);
        super.playerType='m';
    }

    @Override
    public void setMove(int x, int y) {

    }

    @Override
    public void setMove() {

    }


    public void setMove(int x, int y,Board board) {
        setMove(board);
    }


    public void setMove(Board board) {
        int[] point=makeBestMove(board);
        currY=point[0];
        currX=point[1];
    }

    @Override
    public int[] getMove() {
        return new int[]{currY,currX};
    }

    // need to make this medium difficulty
    public int[] getComputerCoordinates(Board board) {
        return makeBestMove(board);//brian's supercool code

    }
    public int minimax(Board board, int depth, boolean isMax) {
        Board temp = board.createDupilicateBoard();
        int best;
        if(temp.isBoardFull())
            return 0;

        if (isMax) {
            best = -100;
            for (int i = 0; i < temp.size(); i++) {
                for (int j = 0; j < temp.size(); j++) {
                    if (temp.getTiles(i, j) == null) {
                        // make the move
                        try {
                            temp.addDisc(i, j, 2);
                        } catch (InValidDiskPositionException e) {
                            e.printStackTrace();
                        }
                        //call minimax Recursively and choose the max values
                        best = Math.max(best, minimax(temp, depth + 1, !isMax));

                        // undo the move
                        temp.removeTile(i,j);
                    }
                }
            }
        } else {
            best = 100;
            for (int i = 0; i < temp.size(); i++) {
                for (int j = 0; j < temp.size(); j++) {
                    if (temp.getTiles(i, j) == null) { // if board [i][j] == empty
                        // make the move
                        try {
                            temp.addDisc(i, j, 2);
                        } catch (InValidDiskPositionException e) {
                            e.printStackTrace();
                        }
                        //call minimax Recursively and choose the max values
                        best = Math.min(best, minimax(temp, depth + 1, !isMax));
                        // undo the move
                        temp.removeTile(i,j);
                    }
                }
            }

        }

        return best;
    }

    public int[] makeBestMove(Board board) {
        Board temp = board.createDupilicateBoard();
        int bestVal = -100;
        int[] coordinates = new int[2];

        for (int x = 0; x < temp.size(); x++) {
            for (int j = 0; j < temp.size(); j++) {
                if (temp.isValidPosition(x,j)) {

                    // make the move
                    try {
                        temp.addDisc(x, j, 2);
                    } catch (InValidDiskPositionException e) {
                        e.printStackTrace();
                    } if (temp.getBoardWon()) {
                        int moveVal = minimax(temp, 0, false);

                        // undo
                        temp.removeTile(x,j);

                        // if val of current move is > best val then update
                        if (moveVal > bestVal) {
                            coordinates[0] = x;
                            coordinates[1] = j;
                        }
                    }
                }
            }
        }
        return coordinates;
    }

}


