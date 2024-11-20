package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Random;

import static ui.EscapeSequences.*;

public class Board {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 2;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 0;

    // Padded characters.
    private static final String EMPTY = "   ";
    private static final String X = " X ";
    private static final String O = " O ";

    private static Random rand = new Random();

    private static ChessBoard board = new ChessBoard();


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        board.resetBoard();

        out.print(ERASE_SCREEN);

        for (int i=0; i < 10 ; i++){
            for (int j=0; j< 10; j++){
                // first line
                if (i == 0){
                    if (j==0||j==9){
                        out.print(EMPTY);
                    }else{
                        drawFirstLine(out,j);
                    }
                // last line
                }else if (i == 9) {
                    if (j==0||j==9){
                        out.print(EMPTY);
                    }else{
                        drawFirstLine(out,j);
                    }
                // lines in the middle
                }else{
                    if (j==0||j==9){
                        drawIntheMiddle(out,i);
                    }else{
                        drawMainChessBoard(out,i,j);
                    }
                }
            }
        }

        drawHeaders(out);

        drawTicTacToeBoard(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static String chessPiece2String(ChessPiece piece){
        ChessPiece.PieceType type = piece.getPieceType();
        return switch(type){
            case ChessPiece.PieceType.PAWN -> " P ";
            case ChessPiece.PieceType.KING -> " K ";
            case ChessPiece.PieceType.KNIGHT -> " N ";
            case ChessPiece.PieceType.ROOK -> " R ";
            case ChessPiece.PieceType.QUEEN -> " Q ";
            case ChessPiece.PieceType.BISHOP -> " B ";
            default -> EMPTY;
        };
    }

    private static void pieceColor(PrintStream out, ChessPiece piece){
        ChessGame.TeamColor color = piece.getTeamColor();
        if (color == ChessGame.TeamColor.WHITE){
            out.print(SET_TEXT_COLOR_RED);
        }else if (color == ChessGame.TeamColor.BLACK){
            out.print(SET_TEXT_COLOR_BLUE);
        }
    }

    private static void drawMainChessBoard(PrintStream out, int i, int j){

        if (i % 2==0 && j % 2==0||i % 2==1 && j % 2==1){
            out.print(SET_BG_COLOR_WHITE);

            ChessPosition pos = new ChessPosition(i,j);
            ChessPiece piece = board.getPiece(pos);

            pieceColor(out,piece);
            out.print(chessPiece2String(piece));

        }else{
            out.print(SET_BG_COLOR_BLACK);

            ChessPosition pos = new ChessPosition(i,j);
            ChessPiece piece = board.getPiece(pos);

            pieceColor(out,piece);
            out.print(chessPiece2String(piece));
        }
    }

    private static void drawFirstLine(PrintStream out, int j){
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_GREEN);

        String[] headers = {" a ", " b ", " c ", " d "," e "," f "," g "," h "};

        out.print(headers[j]);
    }

    private static void drawIntheMiddle(PrintStream out, int i){
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_GREEN);

        String[] side = {" 1 ", " 2 ", " 3 "," 4 "," 5 "," 6 "," 7 "," 8 "};

        out.print(side[i]);
    }













    private static void drawHeaders(PrintStream out) {

        setBlack(out);

        String[] headers = { " a ", " b ", " c ", " d "," e "," f "," g "," h "};
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);

            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
            }
        }

        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS/2;
        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void drawEmptyBG(PrintStream out){

    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);

        out.print(player);

        setBlack(out);
    }

    private static void drawTicTacToeBoard(PrintStream out) {

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {

            drawRowOfSquares(out);

            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                // Draw horizontal row separator.
                drawHorizontalLine(out);
                setBlack(out);
            }
        }
    }

    private static void drawRowOfSquares(PrintStream out) {

        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                setWhite(out);

                if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

                    out.print(EMPTY.repeat(prefixLength));
                    printPlayer(out, rand.nextBoolean() ? X : O);
                    out.print(EMPTY.repeat(suffixLength));
                }
                else {
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                }

                if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                    // Draw vertical column separator.
                    setRed(out);
                    out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
                }

                setBlack(out);
            }

            out.println();
        }
    }

    private static void drawHorizontalLine(PrintStream out) {

        int boardSizeInSpaces = BOARD_SIZE_IN_SQUARES * SQUARE_SIZE_IN_PADDED_CHARS +
                (BOARD_SIZE_IN_SQUARES - 1) * LINE_WIDTH_IN_PADDED_CHARS;

        for (int lineRow = 0; lineRow < LINE_WIDTH_IN_PADDED_CHARS; ++lineRow) {
            setRed(out);
            out.print(EMPTY.repeat(boardSizeInSpaces));

            setBlack(out);
            out.println();
        }
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void printPlayer(PrintStream out, String player) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setWhite(out);
    }
}