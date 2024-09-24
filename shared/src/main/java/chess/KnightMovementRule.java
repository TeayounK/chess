package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class KnightMovementRule {

    private ChessBoard board;
    private ChessPosition position;
    public KnightMovementRule(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
    }
    public Collection<ChessMove> possibleMoves(){

        int [][] possible = {{position.getRow()+2, position.getColumn()-1},
                {position.getRow()+2, position.getColumn()+1},
                {position.getRow()-2, position.getColumn()-1},
                {position.getRow()-2, position.getColumn()+1},
                {position.getRow()+1, position.getColumn()+2},
                {position.getRow()-1, position.getColumn()+2},
                {position.getRow()+1, position.getColumn()-2},
                {position.getRow()-1, position.getColumn()-2}};

        Collection<ChessMove> result = new ArrayList<>();
        for (int i = 0; i < possible.length; i++) {
            if (possible[i][0] < 9 & possible[i][1] < 9 & possible[i][0] > 0 & possible[i][1] > 0) {
                ChessPosition endpoint = new ChessPosition(possible[i][0], possible[i][1]);
                if (board.getPiece(endpoint) == null) {
                    ChessMove move = new ChessMove(position, endpoint, null);
                    result.add(move);
                } else if ((board.getPiece(endpoint)).getTeamColor() != (board.getPiece(position)).getTeamColor()) {
                    ChessMove move = new ChessMove(position, endpoint, null);
                    result.add(move);
                }
            }

        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnightMovementRule that = (KnightMovementRule) o;
        return Objects.equals(board, that.board) && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, position);
    }

    @Override
    public String toString() {
        return "KnightMovementRule{" +
                "board=" + board +
                ", position=" + position +
                '}';
    }
}
