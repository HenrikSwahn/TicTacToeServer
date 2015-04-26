package Model;

import java.io.Serializable;

public class GameActionObject implements Serializable {

    private int id;

    public GameActionObject(int id) {

        this.id = id;

    }

    public int getId() {

        return id;

    }
}