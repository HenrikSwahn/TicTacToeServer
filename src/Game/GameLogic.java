package Game;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Created by Nilj on 28/04/15.
 */
public class GameLogic {

    private int board[][];

    public GameLogic() {

        board = new int[3][3];
        IntStream.range(0,3)
            .forEach(i -> IntStream.range(0,3)
                .forEach(j -> {
                    board[i][j] = 0;
                })
            );
    }


}
