package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.JoinGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class Board {

    // Board dimensions.
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 8;
    // Padded characters.
    private static final String EMPTY = "   ";
    private static final ChessBoard BOARD = new ChessBoard();


    public static void main(JoinGame joinGame) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        if (joinGame.playerColor().equalsIgnoreCase("black")) {
            drawBoardBackward(out);
        }else{
            drawBoard(out);
        }

        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private static void drawBoard(PrintStream out) {
        BOARD.resetBoard();

        for (int i = 9; i > -1; i--) {
            for (int j = 0; j < 10; j++) {
                // first line
                drawBoardHelper(out, i, j);
            }
            out.print(RESET_BG_COLOR);
            out.print("\n");
        }
    }

    private static String chessPiece2String(ChessPiece piece) {
        ChessPiece.PieceType type = piece.getPieceType();
        return switch (type) {
            case ChessPiece.PieceType.PAWN    -> " P ";
            case ChessPiece.PieceType.KING    -> " K ";
            case ChessPiece.PieceType.KNIGHT  -> " N ";
            case ChessPiece.PieceType.ROOK    -> " R ";
            case ChessPiece.PieceType.QUEEN   -> " Q ";
            case ChessPiece.PieceType.BISHOP  -> " B ";
        };
    }

    private static void pieceColor(PrintStream out, ChessPiece piece) {
        ChessGame.TeamColor color = piece.getTeamColor();
        if (color == ChessGame.TeamColor.WHITE) {
            out.print(SET_TEXT_COLOR_RED);
        } else if (color == ChessGame.TeamColor.BLACK) {
            out.print(SET_TEXT_COLOR_BLUE);
        }
    }

    private static void drawMainChessBoard(PrintStream out, int i, int j) {

        if (i % 2 == 0 && j % 2 == 0 || i % 2 == 1 && j % 2 == 1) {
            out.print(SET_BG_COLOR_BLACK);

            ChessPosition pos = new ChessPosition(i, j);
            ChessPiece piece = BOARD.getPiece(pos);

            if (piece == null) {
                out.print(EMPTY);
            } else {
                pieceColor(out, piece);
                out.print(chessPiece2String(piece));
            }

        } else {
            out.print(SET_BG_COLOR_WHITE);

            ChessPosition pos = new ChessPosition(i, j);
            ChessPiece piece = BOARD.getPiece(pos);

            if (piece == null) {
                out.print(EMPTY);
            } else {
                pieceColor(out, piece);
                out.print(chessPiece2String(piece));
            }
        }
    }

    private static void drawFirstLine(PrintStream out, int j) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_GREEN);

        String[] headers = {" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};

        out.print(headers[j - 1]);
    }

    private static void drawIntheMiddle(PrintStream out, int i) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_GREEN);

        String[] side = {" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};

        out.print(side[i - 1]);
    }


    private static void drawBoardBackward(PrintStream out){
        BOARD.resetBoard();

        for (int i=0; i < 10 ; i++){
            for (int j=9; j > -1 ; j--){
                drawBoardHelper(out, i, j);
            }
            out.print(RESET_BG_COLOR);
            out.print("\n");
        }
    }

    private static void drawBoardHelper(PrintStream out, int i, int j) {
        // first line
        if (i == 0){
            if (j ==0|| j ==9){
                out.print(SET_BG_COLOR_LIGHT_GREY);
                out.print(EMPTY);
            }else{
                drawFirstLine(out, j);
            }
            // last line
        }else if (i == 9) {
            if (j ==0|| j ==9){
                out.print(SET_BG_COLOR_LIGHT_GREY);
                out.print(EMPTY);
            }else{
                drawFirstLine(out, j);
            }
            // lines in the middle
        }else{
            if (j ==0|| j ==9){
                drawIntheMiddle(out, i);
            }else{
                drawMainChessBoard(out, i, j);
            }
        }
    }
}







