package chess;

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

        return GeneralChessMoves.getChessMoves(possible,board,position);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
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
