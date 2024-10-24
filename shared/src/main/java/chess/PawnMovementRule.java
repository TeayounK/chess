package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class PawnMovementRule {

    private final ChessBoard board;
    private final ChessPosition position;
    public PawnMovementRule(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;

    }

    public Collection<ChessMove> possibleMoves(){
        // For White Team
        int [][] possible1 = {{position.getRow()+1, position.getColumn()+1},
                {position.getRow()+1, position.getColumn()-1},
                {position.getRow()+1, position.getColumn()},
                {position.getRow()+2, position.getColumn()}};

        int [][] possible2 = {{position.getRow()+1, position.getColumn()+1},
                {position.getRow()+1, position.getColumn()-1},
                {position.getRow()+1, position.getColumn()}};

        // For Black Team
        int [][] possible3 = {{position.getRow()-1, position.getColumn()+1},
                {position.getRow()-1, position.getColumn()-1},
                {position.getRow()-1, position.getColumn()},
                {position.getRow()-2, position.getColumn()}};

        int [][] possible4 = {{position.getRow()-1, position.getColumn()+1},
                {position.getRow()-1, position.getColumn()-1},
                {position.getRow()-1, position.getColumn()}};

        boolean first = false;
        Collection<ChessPiece.PieceType> types = new ArrayList<>();
        types.add(ChessPiece.PieceType.BISHOP);
        types.add(ChessPiece.PieceType.QUEEN);
        types.add(ChessPiece.PieceType.ROOK);
        types.add(ChessPiece.PieceType.KNIGHT);

        Collection<ChessMove> result = new ArrayList<>();
        // For White team
        if ((board.getPiece(position)).getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (position.getRow() == 2) {
                for (int i = 0; i < possible1.length; i++) {
                    if (possible1[i][0] < 9 & possible1[i][1] < 9 & possible1[i][0] > 0 & possible1[i][1] > 0) {
                        ChessPosition endpoint = new ChessPosition(possible1[i][0], possible1[i][1]);
                        if (board.getPiece(endpoint) == null & endpoint.getColumn() == position.getColumn()
                                & (endpoint.getRow() - position.getRow() == 1 || endpoint.getRow() - position.getRow() == -1)) {
                            if (endpoint.getRow() == 8) {
                                ChessMove move = new ChessMove(position, endpoint, ChessPiece.PieceType.QUEEN);
                                result.add(move);
                                ChessMove move1 = new ChessMove(position, endpoint, ChessPiece.PieceType.BISHOP);
                                result.add(move1);
                                ChessMove move2 = new ChessMove(position, endpoint, ChessPiece.PieceType.KNIGHT);
                                result.add(move2);
                                ChessMove move3 = new ChessMove(position, endpoint, ChessPiece.PieceType.ROOK);
                                result.add(move3);
                                first = true;
                            } else {
                                ChessMove move = new ChessMove(position, endpoint, null);
                                result.add(move);
                                first = true;
                            }
                        } else if (board.getPiece(endpoint) == null & endpoint.getColumn() == position.getColumn()
                                & (endpoint.getRow() - position.getRow() == 2 || endpoint.getRow() - position.getRow() == -2)
                                & first) {
                            ChessMove move = new ChessMove(position, endpoint, null);
                            result.add(move);
                        } else if (board.getPiece(endpoint) != null) {
                            if ((board.getPiece(endpoint)).getTeamColor() != (board.getPiece(position)).getTeamColor()
                                    & endpoint.getColumn() != position.getColumn()) {
                                ChessMove move = new ChessMove(position, endpoint, ChessPiece.PieceType.QUEEN);
                                result.add(move);
                                ChessMove move1 = new ChessMove(position, endpoint, ChessPiece.PieceType.BISHOP);
                                result.add(move1);
                                ChessMove move2 = new ChessMove(position, endpoint, ChessPiece.PieceType.KNIGHT);
                                result.add(move2);
                                ChessMove move3 = new ChessMove(position, endpoint, ChessPiece.PieceType.ROOK);
                                result.add(move3);
                            }
                        }
                    }
                }
            } else {
                for (int i = 0; i < possible2.length; i++) {
                    if (possible2[i][0] < 9 & possible2[i][1] < 9 & possible2[i][0] > 0 & possible2[i][1] > 0) {
                        ChessPosition endpoint = new ChessPosition(possible2[i][0], possible2[i][1]);
                        if (board.getPiece(endpoint) == null & endpoint.getColumn() == position.getColumn()
                                & (endpoint.getRow() - position.getRow() == 1 || endpoint.getRow() - position.getRow() == -1)) {
                            if (endpoint.getRow() == 8) {
                                ChessMove move = new ChessMove(position, endpoint, ChessPiece.PieceType.QUEEN);
                                result.add(move);
                                ChessMove move1 = new ChessMove(position, endpoint, ChessPiece.PieceType.BISHOP);
                                result.add(move1);
                                ChessMove move2 = new ChessMove(position, endpoint, ChessPiece.PieceType.KNIGHT);
                                result.add(move2);
                                ChessMove move3 = new ChessMove(position, endpoint, ChessPiece.PieceType.ROOK);
                                result.add(move3);
                            } else {
                                ChessMove move = new ChessMove(position, endpoint, null);
                                result.add(move);
                            }
                        } else if (board.getPiece(endpoint) != null) {
                            if ((board.getPiece(endpoint)).getTeamColor() != (board.getPiece(position)).getTeamColor()
                                & endpoint.getColumn() != position.getColumn()) {
                                if (endpoint.getRow() == 8){
                                    ChessMove move = new ChessMove(position, endpoint, ChessPiece.PieceType.QUEEN);
                                    result.add(move);
                                    ChessMove move1 = new ChessMove(position, endpoint, ChessPiece.PieceType.BISHOP);
                                    result.add(move1);
                                    ChessMove move2 = new ChessMove(position, endpoint, ChessPiece.PieceType.KNIGHT);
                                    result.add(move2);
                                    ChessMove move3 = new ChessMove(position, endpoint, ChessPiece.PieceType.ROOK);
                                    result.add(move3);
                                }else {
                                    ChessMove move = new ChessMove(position, endpoint, null);
                                    result.add(move);
                                }
                            }
                        }
                    }
                }
            }
        // For black team
        }else{
            if (position.getRow() == 7) {
                for (int i = 0; i < possible3.length; i++) {
                    if (possible3[i][0] < 9 & possible3[i][1] < 9 & possible3[i][0] > 0 & possible3[i][1] > 0) {
                        ChessPosition endpoint = new ChessPosition(possible3[i][0], possible3[i][1]);
                        if (board.getPiece(endpoint) == null & endpoint.getColumn() == position.getColumn()
                                & (endpoint.getRow() - position.getRow() == 1 || endpoint.getRow() - position.getRow() == -1)) {
                            if (endpoint.getColumn() == 1) {
                                ChessMove move = new ChessMove(position, endpoint, ChessPiece.PieceType.QUEEN);
                                result.add(move);
                                ChessMove move1 = new ChessMove(position, endpoint, ChessPiece.PieceType.BISHOP);
                                result.add(move1);
                                ChessMove move2 = new ChessMove(position, endpoint, ChessPiece.PieceType.KNIGHT);
                                result.add(move2);
                                ChessMove move3 = new ChessMove(position, endpoint, ChessPiece.PieceType.ROOK);
                                result.add(move3);
                                first = true;
                            } else {
                                ChessMove move = new ChessMove(position, endpoint, null);
                                result.add(move);
                                first = true;
                            }
                        } else if (board.getPiece(endpoint) == null & endpoint.getColumn() == position.getColumn()
                                & (endpoint.getRow() - position.getRow() == 2 || endpoint.getRow() - position.getRow() == -2)
                                & first) {
                            ChessMove move = new ChessMove(position, endpoint, null);
                            result.add(move);
                        } else if (board.getPiece(endpoint) != null) {
                            if ((board.getPiece(endpoint)).getTeamColor() != (board.getPiece(position)).getTeamColor()
                                    & endpoint.getColumn() != position.getColumn()) {
                                ChessMove move = new ChessMove(position, endpoint, null);
                                result.add(move);
                            }
                        }
                    }
                }
            } else {
                for (int i = 0; i < possible4.length; i++) {
                    if (possible4[i][0] < 9 & possible4[i][1] < 9 & possible4[i][0] > 0 & possible4[i][1] > 0) {
                        ChessPosition endpoint = new ChessPosition(possible4[i][0], possible4[i][1]);
                        if (board.getPiece(endpoint) == null & endpoint.getColumn() == position.getColumn()
                                & (endpoint.getRow() - position.getRow() == 1 || endpoint.getRow() - position.getRow() == -1)) {
                            if (endpoint.getRow() == 1) {
                                ChessMove move = new ChessMove(position, endpoint, ChessPiece.PieceType.QUEEN);
                                result.add(move);
                                ChessMove move1 = new ChessMove(position, endpoint, ChessPiece.PieceType.BISHOP);
                                result.add(move1);
                                ChessMove move2 = new ChessMove(position, endpoint, ChessPiece.PieceType.KNIGHT);
                                result.add(move2);
                                ChessMove move3 = new ChessMove(position, endpoint, ChessPiece.PieceType.ROOK);
                                result.add(move3);
                            } else {
                                ChessMove move = new ChessMove(position, endpoint, null);
                                result.add(move);
                            }
                        } else if (board.getPiece(endpoint) != null) {
                            if ((board.getPiece(endpoint)).getTeamColor() != (board.getPiece(position)).getTeamColor()
                                    & endpoint.getColumn() != position.getColumn()) {
                                if (endpoint.getRow() == 1) {
                                    for (ChessPiece.PieceType pieceType : types){
                                        ChessMove move = new ChessMove(position, endpoint, pieceType);
                                        result.add(move);
                                    }
                                }else {
                                    ChessMove move = new ChessMove(position, endpoint, null);
                                    result.add(move);
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PawnMovementRule that = (PawnMovementRule) o;
        return Objects.equals(board, that.board) && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, position);
    }

    @Override
    public String toString() {
        return "PawnMovementRule{" +
                "board=" + board +
                ", position=" + position +
                '}';
    }
}


