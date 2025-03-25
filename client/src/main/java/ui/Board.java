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
        } else {
            drawBoard(out);
        }

        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }
}







