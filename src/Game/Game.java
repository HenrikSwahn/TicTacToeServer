package Game;

/**
 * Created by Nilj on 28/04/15.
 */
public class Game {

    private GameLogic gLogic;
    private int xScore;
    private int oScore;

    public Game() {

        gLogic = new GameLogic();
        xScore = 0;
        oScore = 0;

    }

    public boolean setMark(int pos, String mark) {

        return gLogic.setMark(pos,mark);

    }

    public boolean checkIfGameOver(String mark) {

        boolean status = gLogic.isGameOver(mark);

        if(status && mark.equals("X"))
            xScore++;
        else if(status && mark.equals("O"))
            oScore++;

        return status;

    }

    public int getxScore() {
        return xScore;
    }

    public int getoScore() {
        return oScore;
    }
}
