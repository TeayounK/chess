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

        ChessPiece startPiece = this.board.getPiece(startPosition);
        Collection<ChessMove> moves = startPiece.pieceMoves(board,startPosition);
        TeamColor teamColor = startPiece.getTeamColor();
        Collection<ChessMove> result = new ArrayList<>();
        for(ChessMove move : moves){
            if (board.getPiece(move.getEndPosition()) == null){
                board.addPiece(move.getEndPosition(),board.getPiece(move.getStartPosition()));
                board.removePiece(move.getStartPosition());
                if (!isInCheck(teamColor)){
                    result.add(move);
                }
                // Revert to before moving
                board.addPiece(move.getStartPosition(),board.getPiece(move.getEndPosition()));
                board.removePiece(move.getEndPosition());

            }else{ // since piecemoves returns valid moves where it can capture enemy or move to an empty position
                ChessPiece target = board.getPiece(move.getEndPosition());
                board.removePiece(move.getEndPosition());
                board.addPiece(move.getEndPosition(),board.getPiece(move.getStartPosition()));
                board.removePiece(move.getStartPosition());
                if (!isInCheck(teamColor)){
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
        if (board.getPiece(move.getStartPosition())!=null){
            if(teamColor==board.getPiece(move.getStartPosition()).getTeamColor()){
                Collection<ChessMove> valid_moves = validMoves(move.getStartPosition());
                TeamColor piece_color = board.getPiece(move.getStartPosition()).getTeamColor();
                piece_color = switch (piece_color){
                    case WHITE -> TeamColor.BLACK;
                    case BLACK -> TeamColor.WHITE;
                };
                if (valid_moves.contains(move)) {
                    if (board.getPiece(move.getEndPosition()) == null) {
                        if (move.getPromotionPiece() != null) {
                            ChessPiece.PieceType pieceType = move.getPromotionPiece();
                            TeamColor colorType = board.getPiece(move.getStartPosition()).getTeamColor();
                            board.addPiece(move.getEndPosition(), new ChessPiece(colorType, pieceType));
                            board.removePiece(move.getStartPosition());
                            teamColor=piece_color;
                        } else {
                            board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
                            board.removePiece(move.getStartPosition());
                            teamColor=piece_color;
                        }


                    } else { // since piecemoves returns valid moves where it can capture enemy or move to an empty position
                        if (move.getPromotionPiece() != null) {
                            ChessPiece.PieceType pieceType = move.getPromotionPiece();
                            TeamColor colorType = board.getPiece(move.getStartPosition()).getTeamColor();
                            board.removePiece(move.getEndPosition());
                            board.addPiece(move.getEndPosition(), new ChessPiece(colorType, pieceType));
                            board.removePiece(move.getStartPosition());
                            teamColor=piece_color;
                        } else {
                            board.removePiece(move.getEndPosition());
                            board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
                            board.removePiece(move.getStartPosition());
                            teamColor=piece_color;
                        }
                    }
                } else {
                    throw new InvalidMoveException("This is invalid move"); //??
                }
            }else {
                throw new InvalidMoveException("This is invalid move"); //??
            }
        }else {
            throw new InvalidMoveException("This is invalid move"); //??
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (int i=1;i<9;i++){
            for (int j=1;j<9;j++){
                ChessPosition pos = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null){
                    if(piece.getTeamColor() != teamColor) {
                        Collection<ChessMove> moves = piece.pieceMoves(board, pos);
                        for (ChessMove move:moves){
                            if (board.getPiece(move.getEndPosition()) != null){
                                if (board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING &&
                                        board.getPiece(move.getEndPosition()).getTeamColor() == teamColor) {
                                    return true;
                                }
                            }
                        }
                    }
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
        boolean result = true;
        ChessBoard clone = board.duplicate();
        ChessGame temp = new ChessGame();
        temp.setBoard(clone);
        for (int i=1;i<9;i++){
            for (int j=1;j<9;j++){
                ChessPosition pos = new ChessPosition(i,j);
                // verify a existence of piece
                if (clone.getPiece(pos) != null){
                    // verify team color
                    if (clone.getPiece(pos).getTeamColor()==teamColor){
                        Collection<ChessMove> list_moves = clone.getPiece(pos).pieceMoves(clone,pos);
                        for (ChessMove a_move : list_moves){
                            // make a move and check its validation for escaping check
                            if (clone.getPiece(a_move.getEndPosition()) == null){
                                clone.addPiece(a_move.getEndPosition(),clone.getPiece(a_move.getStartPosition()));
                                clone.removePiece(a_move.getStartPosition());
                                if (!temp.isInCheck(teamColor)){
                                    result=false;
                                }
                                // Revert to before moving
                                clone.addPiece(a_move.getStartPosition(),clone.getPiece(a_move.getEndPosition()));
                                clone.removePiece(a_move.getEndPosition());

                            }else{ // since piecemoves returns valid moves where it can capture enemy or move to an empty position
                                ChessPiece target = clone.getPiece(a_move.getEndPosition());
                                clone.removePiece(a_move.getEndPosition());
                                clone.addPiece(a_move.getEndPosition(),clone.getPiece(a_move.getStartPosition()));
                                clone.removePiece(a_move.getStartPosition());
                                if (!temp.isInCheck(teamColor)){
                                    result=false;
                                }
                                // Revert to before moving
                                clone.addPiece(a_move.getStartPosition(),clone.getPiece(a_move.getEndPosition()));
                                clone.removePiece(a_move.getEndPosition());
                                clone.addPiece(a_move.getEndPosition(),target);
                            }
                        }
                    }
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
        if (!isInCheck(teamColor)){
            int temp_sum = 0;
            for (int i=1;i<9;i++) {
                for (int j = 1; j < 9; j++) {
                    ChessPosition pos = new ChessPosition(i, j);
                    if (board.getPiece(pos) != null) {
                        if (board.getPiece(pos).getTeamColor() == teamColor) {
                            temp_sum += (validMoves(pos)).toArray().length;
                        }
                    }
                }
            }
            return temp_sum == 0;
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
