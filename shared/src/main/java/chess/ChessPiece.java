package chess;

import java.sql.Array;
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
            KingMovementRule King = new KingMovementRule(board, myPosition);
            return King.possibleMoves();
        }else if ((getPieceType() == PieceType.KNIGHT)){
            KnightMovementRule Kight = new KnightMovementRule(board, myPosition);
            return Kight.possibleMoves();
        }else if ((getPieceType() == PieceType.PAWN)){
            PawnMovementRule Pawn = new PawnMovementRule(board, myPosition);
            return Pawn.possibleMoves();
        }else if ((getPieceType() == PieceType.QUEEN)){
            QueenMovementRule Queen = new QueenMovementRule(board, myPosition);
            return Queen.possibleMoves();
        }else if ((getPieceType() == PieceType.ROOK)){
            RookMovementRule Rook = new RookMovementRule(board, myPosition);
            return Rook.possibleMoves();
        }else if ((getPieceType() == PieceType.BISHOP)){
            BishopMovementRule Bishop = new BishopMovementRule(board, myPosition);
            return Bishop.possibleMoves();
        }else{
            return new ArrayList<>();
        }


        //        Rule rule = switch(getPieceType()) {
//            case BISHOP -> new Rule(true, new int[][]{{1,-1},{-1,1},{-1,-1},{1,1}});
//            case ROOK   -> new Rule(true, new int[][]{{1,0},{-1,0},{0,1},{0,-1}});
//            case KNIGHT -> new Rule(false, new int[][]{{2,1},{2,-1},{-2,1}});
//            case QUEEN  -> new Rule(true, new int[][]{{1,-1},{-1,1},{-1,-1},{1,1}});
//            case KING   -> new Rule(false, new int[][]{{1,-1},{-1,1},{-1,-1},{1,1}});
//            default     -> null;
//        };
//        return rule.getMoves(board,myPosition);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
