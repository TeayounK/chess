package chess;

import java.util.ArrayList;
import java.util.Collection;

public class GeneralChessMoves {


    public static Collection<ChessMove> straightMoves(ChessBoard board, ChessPosition position){


        int [][] possibleRight = {
                {position.getRow()+1, position.getColumn()},
                {position.getRow()+2, position.getColumn()},
                {position.getRow()+3, position.getColumn()},
                {position.getRow()+4, position.getColumn()},
                {position.getRow()+5, position.getColumn()},
                {position.getRow()+6, position.getColumn()},
                {position.getRow()+7, position.getColumn()},
                {position.getRow()+8, position.getColumn()}};
        int [][] possibleLeft = {
                {position.getRow()-1, position.getColumn()},
                {position.getRow()-2, position.getColumn()},
                {position.getRow()-3, position.getColumn()},
                {position.getRow()-4, position.getColumn()},
                {position.getRow()-5, position.getColumn()},
                {position.getRow()-6, position.getColumn()},
                {position.getRow()-7, position.getColumn()},
                {position.getRow()-8, position.getColumn()}};
        int [][] possibleUp = {
                {position.getRow(), position.getColumn()+1},
                {position.getRow(), position.getColumn()+2},
                {position.getRow(), position.getColumn()+3},
                {position.getRow(), position.getColumn()+4},
                {position.getRow(), position.getColumn()+5},
                {position.getRow(), position.getColumn()+6},
                {position.getRow(), position.getColumn()+7},
                {position.getRow(), position.getColumn()+8}};
        int [][] possibleDown = {
                {position.getRow(), position.getColumn()-1},
                {position.getRow(), position.getColumn()-2},
                {position.getRow(), position.getColumn()-3},
                {position.getRow(), position.getColumn()-4},
                {position.getRow(), position.getColumn()-5},
                {position.getRow(), position.getColumn()-6},
                {position.getRow(), position.getColumn()-7},
                {position.getRow(), position.getColumn()-8}};

        int [][][] cases = {possibleRight,possibleLeft,possibleUp,possibleDown};
        return getChessMovesHelper(board, position, cases);
    }

    private static Collection<ChessMove> getChessMovesHelper(ChessBoard board, ChessPosition position, int[][][] cases) {
        Collection<ChessMove> result = new ArrayList<>();
        for (int j = 0; j < 4; j++){
            for (int i = 0; i < cases[j].length; i++){
                if (checkHelper(board, position, cases, j, i, result)) {break;}
            }
        }
        return result;
    }


    public static Collection<ChessMove> diagnalMoves(ChessBoard board, ChessPosition position){

        int [][] possibleUpup = {
                {position.getRow()+1, position.getColumn()+1},
                {position.getRow()+2, position.getColumn()+2},
                {position.getRow()+3, position.getColumn()+3},
                {position.getRow()+4, position.getColumn()+4},
                {position.getRow()+5, position.getColumn()+5},
                {position.getRow()+6, position.getColumn()+6},
                {position.getRow()+7, position.getColumn()+7},
                {position.getRow()+8, position.getColumn()+8}};
        int [][] possibleUpdown = {
                {position.getRow()+1, position.getColumn()-1},
                {position.getRow()+2, position.getColumn()-2},
                {position.getRow()+3, position.getColumn()-3},
                {position.getRow()+4, position.getColumn()-4},
                {position.getRow()+5, position.getColumn()-5},
                {position.getRow()+6, position.getColumn()-6},
                {position.getRow()+7, position.getColumn()-7},
                {position.getRow()+8, position.getColumn()-8}};
        int [][] possibleDownup = {
                {position.getRow()-1, position.getColumn()+1},
                {position.getRow()-2, position.getColumn()+2},
                {position.getRow()-3, position.getColumn()+3},
                {position.getRow()-4, position.getColumn()+4},
                {position.getRow()-5, position.getColumn()+5},
                {position.getRow()-6, position.getColumn()+6},
                {position.getRow()-7, position.getColumn()+7},
                {position.getRow()-8, position.getColumn()+8}};
        int [][] possibleDowndown = {
                {position.getRow()-1, position.getColumn()-1},
                {position.getRow()-2, position.getColumn()-2},
                {position.getRow()-3, position.getColumn()-3},
                {position.getRow()-4, position.getColumn()-4},
                {position.getRow()-5, position.getColumn()-5},
                {position.getRow()-6, position.getColumn()-6},
                {position.getRow()-7, position.getColumn()-7},
                {position.getRow()-8, position.getColumn()-8}};


        int [][][] cases = {possibleUpup,possibleUpdown,possibleDownup,possibleDowndown};
        return getChessMovesHelper(board, position, cases);
    }

    private static boolean checkHelper(ChessBoard board, ChessPosition position, int[][][] cases, int j, int i, Collection<ChessMove> result) {
        if (cases[j][i][0] < 9 & cases[j][i][1] < 9 & cases[j][i][0] > 0 & cases[j][i][1] > 0) {
            ChessPosition endpoint = new ChessPosition(cases[j][i][0], cases[j][i][1]);
            if (board.getPiece(endpoint) == null) {
                ChessMove move = new ChessMove(position, endpoint, null);
                result.add(move);
            }else if ((board.getPiece(endpoint)).getTeamColor() == (board.getPiece(position)).getTeamColor()){
                return true;
            }else if ((board.getPiece(endpoint)).getTeamColor() != (board.getPiece(position)).getTeamColor()) {
                ChessMove move = new ChessMove(position, endpoint, null);
                result.add(move);
                return true;
            }
        }
        return false;
    }

    static Collection<ChessMove> getChessMoves(int[][] possible, ChessBoard board, ChessPosition position) {
        Collection<ChessMove> result = new ArrayList<>();
        for (int[] ints : possible) {
            if (ints[0] < 9 & ints[1] < 9 & ints[0] > 0 & ints[1] > 0) {
                ChessPosition endpoint = new ChessPosition(ints[0], ints[1]);
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
}
