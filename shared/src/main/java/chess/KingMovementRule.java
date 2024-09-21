package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovementRule {

    private ChessBoard board;
    private ChessPosition position;
    public KingMovementRule(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;

    }
    public void possibleMoves(){
        int [][] possible = {{position.getRow(), position.getColumn()-1},
                            {position.getRow(), position.getColumn()+1},
                            {position.getRow()+1, position.getColumn()},
                            {position.getRow()+1, position.getColumn()-1},
                            {position.getRow()+1, position.getColumn()+1},
                            {position.getRow()-1, position.getColumn()},
                            {position.getRow()-1, position.getColumn()-1},
                            {position.getRow()-1, position.getColumn()+1}};

        Collection<ChessMove> result = new Collection<ChessMove>();
        for (int i = 0 ; i < possible.length ; i++ ){
            ChessPosition temp = new ChessPosition(possible[i][0],possible[i][1]);
            if (board.getPiece(temp) == null){
                result.add(possible[i]);
            }
        }
    }

}
