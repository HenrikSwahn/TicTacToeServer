package Game;

/**
 * Created by Nilj on 28/04/15.
 */
public class Game {

    private GameLogic gLogic;

    public Game() {

        gLogic = new GameLogic();

    }

    public boolean setMark(int pos, String mark) {

        return gLogic.setMark(pos,mark);

    }
}
