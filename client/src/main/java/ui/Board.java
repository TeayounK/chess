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

    private static void emptyLine(PrintStream out){
        out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
        out.print("\n");
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

}







