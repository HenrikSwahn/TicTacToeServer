package Game;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Created by Nilj on 28/04/15.
 */
public class GameLogic {

    private String board[];

    public GameLogic() {

        board = new String[9];
        IntStream.range(0,9)
            .forEach(i -> {
                board[i] = "-";
            });
    }

    private boolean checkIfValid(int pos) {

        return board[pos].equals("-");

    }

    private boolean checkRow(int f, int s, int t, String m) {

        return board[f] == m
                && board[s] == m
                && board[t] == m;

    }

    public boolean setMark(int pos, String mark) {

        if(checkIfValid(pos)) {

            board[pos] = mark;
            return true;

        }
        return false;
    }

    public boolean isGameOver(String mark) {

        if(checkRow(0,1,2,mark))
            return true;

        if(checkRow(3,4,5,mark))
            return true;

        if(checkRow(6,7,8,mark))
            return true;

        if(checkRow(0,3,6,mark))
            return true;

        if(checkRow(1,4,7,mark))
            return true;

        if(checkRow(2,5,8,mark))
            return true;

        if(checkRow(0,4,8,mark))
            return true;

        if(checkRow(2,4,6,mark))
            return true;

        return false;
    }


}
