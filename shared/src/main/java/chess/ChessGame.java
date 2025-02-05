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
        // need to check if the piece we chose is not null
        if (board.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException("This is invalid move");
        }
        // need to check the team color is the same as the color of piece we want to move
        if (teamColor != board.getPiece(move.getStartPosition()).getTeamColor()) {
            throw new InvalidMoveException("This is invalid move");
        }
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        TeamColor pieceColor = board.getPiece(move.getStartPosition()).getTeamColor();
        // after successfully done with the move, we change the team color for next turn
        pieceColor = switch (pieceColor){
            case WHITE -> TeamColor.BLACK;
            case BLACK -> TeamColor.WHITE;
        };
        // want to make sure the move we want to make is valid.
        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("This is invalid move");
        } else {
            // moving to empty space
            if (board.getPiece(move.getEndPosition()) == null) {
                if (move.getPromotionPiece() != null) {
                    ChessPiece.PieceType pieceType = move.getPromotionPiece();
                    TeamColor colorType = board.getPiece(move.getStartPosition()).getTeamColor();
                    board.addPiece(move.getEndPosition(), new ChessPiece(colorType, pieceType));
                    board.removePiece(move.getStartPosition());
                    teamColor=pieceColor;
                } else {
                    board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
                    board.removePiece(move.getStartPosition());
                    teamColor=pieceColor;
                }
                // killing opponent piece
            } else { // since piece_moves returns valid moves where it can capture enemy or move to an empty position
                if (move.getPromotionPiece() != null) {
                    ChessPiece.PieceType pieceType = move.getPromotionPiece();
                    TeamColor colorType = board.getPiece(move.getStartPosition()).getTeamColor();
                    board.removePiece(move.getEndPosition());
                    board.addPiece(move.getEndPosition(), new ChessPiece(colorType, pieceType));
                    board.removePiece(move.getStartPosition());
                    teamColor=pieceColor;
                } else {
                    board.removePiece(move.getEndPosition());
                    board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
                    board.removePiece(move.getStartPosition());
                    teamColor=pieceColor;
                }
            }
        }
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // looping through board
        for (int i=1;i<9;i++){
            for (int j=1;j<9;j++){
                ChessPosition pos = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(pos);
                if (moveHelper(teamColor, piece, pos)) {return true;}
            }
        }
        return false;
    }

    private boolean moveHelper(TeamColor teamColor, ChessPiece piece, ChessPosition pos) {
        if ((piece != null)&&(piece.getTeamColor() != teamColor)){
            Collection<ChessMove> moves = piece.pieceMoves(board, pos);
            for (ChessMove move:moves){
                if ((board.getPiece(move.getEndPosition()) != null)&&
                        (board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING &&
                                board.getPiece(move.getEndPosition()).getTeamColor() == teamColor)){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // setting for result, clone of board, new game setup
        boolean result = true;
        ChessBoard clone = board.duplicate();
        ChessGame temp = new ChessGame();
        temp.setBoard(clone);
        // looping through new cloned board
        for (int i=1;i<9;i++){
            for (int j=1;j<9;j++){
                ChessPosition pos = new ChessPosition(i,j);
                // verify an existence of piece
                result = checkmateHelper(teamColor, clone, pos, temp, result);
            }
        }

        return result;
    }

    private static boolean checkmateHelper(TeamColor teamColor, ChessBoard clone, ChessPosition pos, ChessGame temp, boolean result) {
        if ((clone.getPiece(pos) != null)&&(clone.getPiece(pos).getTeamColor()== teamColor)){
            Collection<ChessMove> listMoves = clone.getPiece(pos).pieceMoves(clone, pos);
            for (ChessMove aMove : listMoves){
                // make a move and check its validation for escaping check
                if (clone.getPiece(aMove.getEndPosition()) == null){
                    clone.addPiece(aMove.getEndPosition(), clone.getPiece(aMove.getStartPosition()));
                    clone.removePiece(aMove.getStartPosition());
                    if (!temp.isInCheck(teamColor)){
                        result =false;
                    }
                    // Revert to before moving
                    clone.addPiece(aMove.getStartPosition(), clone.getPiece(aMove.getEndPosition()));
                    clone.removePiece(aMove.getEndPosition());

                }else{ // since piece_moves returns valid moves where it can capture enemy or move to an empty position
                    ChessPiece target = clone.getPiece(aMove.getEndPosition());
                    clone.removePiece(aMove.getEndPosition());
                    clone.addPiece(aMove.getEndPosition(), clone.getPiece(aMove.getStartPosition()));
                    clone.removePiece(aMove.getStartPosition());
                    if (!temp.isInCheck(teamColor)){
                        result =false;
                    }
                    // Revert to before moving
                    clone.addPiece(aMove.getStartPosition(), clone.getPiece(aMove.getEndPosition()));
                    clone.removePiece(aMove.getEndPosition());
                    clone.addPiece(aMove.getEndPosition(),target);
                }
            }
        }
        return result;
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
