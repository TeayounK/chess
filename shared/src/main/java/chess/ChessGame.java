package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    TeamColor teamColor;
    ChessBoard board;
    public ChessGame() {
        teamColor = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamColor = team;
    }
    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        // get the settings for identifying piece type and team color
        ChessPiece startPiece = this.board.getPiece(startPosition);
        Collection<ChessMove> moves = startPiece.pieceMoves(board,startPosition);
        Collection<ChessMove> result = new ArrayList<>();
        // loop through the list "moves"
        for(ChessMove move : moves){
            // if there is no piece in the end_position
            if (board.getPiece(move.getEndPosition()) == null){
                board.addPiece(move.getEndPosition(),board.getPiece(move.getStartPosition()));
                board.removePiece(move.getStartPosition());
                // we need to check if the move make it check or not
                if (!isInCheck(startPiece.getTeamColor())){
                    result.add(move);
                }
                // Revert to before moving
                board.addPiece(move.getStartPosition(),board.getPiece(move.getEndPosition()));
                board.removePiece(move.getEndPosition());
                // if there is an opponent piece
            }else{ // since piece_moves returns valid moves where it can capture enemy or move to an empty position
                ChessPiece target = board.getPiece(move.getEndPosition());
                board.removePiece(move.getEndPosition());
                board.addPiece(move.getEndPosition(),board.getPiece(move.getStartPosition()));
                board.removePiece(move.getStartPosition());
                // we need to check if the move make it check or not
                if (!isInCheck(startPiece.getTeamColor())){
                    result.add(move);
                }
                // Revert to before moving
                board.addPiece(move.getStartPosition(),board.getPiece(move.getEndPosition()));
                board.removePiece(move.getEndPosition());
                board.addPiece(move.getEndPosition(),target);
            }
        }

        return result;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        throw new RuntimeException("Not implemented");
    }
}
