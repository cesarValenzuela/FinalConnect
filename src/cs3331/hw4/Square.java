package cs3331.hw4;

/**
 * @author Andrea Torres
 */
public class Square {
    private int x, y;
    private Integer currPlayer;

    Square(int x, int y, Integer player) {
        this.x = x;
        this.y = y;
        this.currPlayer = player;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPlayer() {
        return currPlayer;
    }
}
