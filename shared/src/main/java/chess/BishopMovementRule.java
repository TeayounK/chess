//package chess;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Objects;
//
//public class BishopMovementRule {
//
//    private ChessBoard board;
//    private ChessPosition position;
//    public BishopMovementRule(ChessBoard board, ChessPosition position){
//        this.board = board;
//        this.position = position;
//    }
//
//    public Collection<ChessMove> possibleMoves(){
//
//        int [][] possibleUpup = {
//                {position.getRow()+1, position.getColumn()+1},
//                {position.getRow()+2, position.getColumn()+2},
//                {position.getRow()+3, position.getColumn()+3},
//                {position.getRow()+4, position.getColumn()+4},
//                {position.getRow()+5, position.getColumn()+5},
//                {position.getRow()+6, position.getColumn()+6},
//                {position.getRow()+7, position.getColumn()+7},
//                {position.getRow()+8, position.getColumn()+8}};
//        int [][] possibleUpdown = {
//                {position.getRow()+1, position.getColumn()-1},
//                {position.getRow()+2, position.getColumn()-2},
//                {position.getRow()+3, position.getColumn()-3},
//                {position.getRow()+4, position.getColumn()-4},
//                {position.getRow()+5, position.getColumn()-5},
//                {position.getRow()+6, position.getColumn()-6},
//                {position.getRow()+7, position.getColumn()-7},
//                {position.getRow()+8, position.getColumn()-8}};
//        int [][] possibleDownup = {
//                {position.getRow()-1, position.getColumn()+1},
//                {position.getRow()-2, position.getColumn()+2},
//                {position.getRow()-3, position.getColumn()+3},
//                {position.getRow()-4, position.getColumn()+4},
//                {position.getRow()-5, position.getColumn()+5},
//                {position.getRow()-6, position.getColumn()+6},
//                {position.getRow()-7, position.getColumn()+7},
//                {position.getRow()-8, position.getColumn()+8}};
//        int [][] possibleDowndown = {
//                {position.getRow()-1, position.getColumn()-1},
//                {position.getRow()-2, position.getColumn()-2},
//                {position.getRow()-3, position.getColumn()-3},
//                {position.getRow()-4, position.getColumn()-4},
//                {position.getRow()-5, position.getColumn()-5},
//                {position.getRow()-6, position.getColumn()-6},
//                {position.getRow()-7, position.getColumn()-7},
//                {position.getRow()-8, position.getColumn()-8}};
//
//
//        int [][][] cases = {possibleUpup,possibleUpdown,possibleDownup,possibleDowndown};
//        Collection<ChessMove> result = new ArrayList<>();
//        for (int j = 0; j < 4; j++){
//            for (int i = 0; i < cases[j].length; i++) {
//                if (cases[j][i][0] < 9 & cases[j][i][1] < 9 & cases[j][i][0] > 0 & cases[j][i][1] > 0) {
//                    ChessPosition endpoint = new ChessPosition(cases[j][i][0], cases[j][i][1]);
//                    if (board.getPiece(endpoint) == null) {
//                        ChessMove move = new ChessMove(position, endpoint, null);
//                        result.add(move);
//                    }else if ((board.getPiece(endpoint)).getTeamColor() == (board.getPiece(position)).getTeamColor()){
//                        break;
//                    }else if ((board.getPiece(endpoint)).getTeamColor() != (board.getPiece(position)).getTeamColor()) {
//                        ChessMove move = new ChessMove(position, endpoint, null);
//                        result.add(move);
//                        break;
//                    }
//                }
//            }
//        }
//        return result;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        BishopMovementRule that = (BishopMovementRule) o;
//        return Objects.equals(board, that.board) && Objects.equals(position, that.position);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(board, position);
//    }
//
//    @Override
//    public String toString() {
//        return "BishopMovementRule{" +
//                "board=" + board +
//                ", position=" + position +
//                '}';
//    }
//}
