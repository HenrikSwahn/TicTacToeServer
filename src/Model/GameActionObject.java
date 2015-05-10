package Model;

import java.io.Serializable;

public class GameActionObject implements Serializable {

    /*Action:
    0 = ask to start new game
    1 = yes
    2 = no
    3 = click, also uses id
    4 = player is X
    5 = player is O
     */
    private int action;
    private int id;

    public GameActionObject(int action, int id) {

        this.action = action;
        this.id = id;

    }

    public int getId() {

        return id;

    }

    public int getAction() {

        return action;

    }
}

