package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }
    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        if (getPieceType() == PieceType.KING) {
            KingMovementRule king = new KingMovementRule(board, myPosition);
            return king.possibleMoves();
        }else if ((getPieceType() == PieceType.KNIGHT)){
            KnightMovementRule knight = new KnightMovementRule(board, myPosition);
            return knight.possibleMoves();
        }else if ((getPieceType() == PieceType.PAWN)){
            PawnMovementRule pawn = new PawnMovementRule(board, myPosition);
            return pawn.possibleMoves();
        }else if ((getPieceType() == PieceType.QUEEN)){
            ArrayList<ChessMove> queensMove = (ArrayList<ChessMove>) GeneralChessMoves.straightMoves(board,myPosition);
            queensMove.addAll(GeneralChessMoves.diagnalMoves(board,myPosition));
            return queensMove;
        }else if ((getPieceType() == PieceType.ROOK)){
            return GeneralChessMoves.straightMoves(board,myPosition);
        }else if ((getPieceType() == PieceType.BISHOP)){
            return GeneralChessMoves.diagnalMoves(board,myPosition);
        }else{
            return new ArrayList<>();
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}
