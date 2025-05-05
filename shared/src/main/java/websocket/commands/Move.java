package websocket.commands;

import chess.ChessMove;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 *
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class Move {

    private final ChessMove move;

    public Move(ChessMove move) {
        this.move = move;
    }

    public ChessMove getMove() {
        return move;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Move move1 = (Move) o;
        return Objects.equals(move, move1.move);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(move);
    }
}



