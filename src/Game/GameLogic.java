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

    public boolean setMark(int pos, String mark) {

        if(checkIfValid(pos)) {

            board[pos] = mark;
            return true;

        }
        return false;
    }
}
