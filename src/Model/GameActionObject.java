package Model;

import java.io.Serializable;

public class GameActionObject implements Serializable {

    /*Action:
    0 = ask to start new game
    1 = yes
    2 = no
    3 = click, also uses val
    4 = player is X
    5 = player is O
    6 = valid move, also uses val
    7 = invalid move, also uses val
     */
    private int action;
    private int val;

    public GameActionObject(int action, int val) {

        this.action = action;
        this.val = val;

    }

    public int getVal() {

        return val;

    }

    public int getAction() {

        return action;

    }
}

