package chess;

import java.util.Collection;
import java.util.Objects;

public class KingMovementRule {

    private ChessBoard board;
    private ChessPosition position;
    public KingMovementRule(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> possibleMoves(){
        int [][] possible = {{position.getRow(), position.getColumn()-1},
                {position.getRow(), position.getColumn()+1},
                {position.getRow()+1, position.getColumn()},
                {position.getRow()+1, position.getColumn()-1},
                {position.getRow()+1, position.getColumn()+1},
                {position.getRow()-1, position.getColumn()},
                {position.getRow()-1, position.getColumn()-1},
                {position.getRow()-1, position.getColumn()+1}};

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
        KingMovementRule that = (KingMovementRule) o;
        return Objects.equals(board, that.board) && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, position);
    }
}
