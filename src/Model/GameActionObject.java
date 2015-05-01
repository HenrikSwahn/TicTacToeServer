package Model;

import java.io.Serializable;

public class GameActionObject implements Serializable {

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

