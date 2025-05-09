package ui;

import chess.*;
import model.JoinGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class Board {

    // Board dimensions.
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 8;
    // Padded characters.
    private static final String EMPTY = "   ";
    private static ChessBoard BOARD;

    public Board(){
        BOARD = new ChessBoard();
    }


    public static void main(JoinGame joinGame, Integer i, Integer j) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        ChessPosition pos = new ChessPosition(i, j);

        out.print(ERASE_SCREEN);
        drawBoard(out, pos, joinGame.playerColor().equalsIgnoreCase("black"));

        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);

    }

    public static void create(){
        BOARD.resetBoard();
    }

    private static void drawBoard(PrintStream out, ChessPosition pos, Boolean backward) {

        ChessPiece piece = null;
        Collection<ChessMove> possMoves = new ArrayList<>();

        if (pos.getRow()!=0 && pos.getColumn()!=0){
            piece = BOARD.getPiece(pos);
            possMoves = piece.pieceMoves(BOARD,pos);
        }

        Collection<Collection<Integer>> pairs = new ArrayList<>();

        positionCollector(possMoves, pairs);

        if (!backward){
            for (int i = 9; i > -1; i--) {
                for (int j = 0; j < 10; j++) {
                    // first line
                    drawOnebyOne(out, i, j, pairs);
                }
                out.print(RESET_BG_COLOR);
                out.print("\n");
            }
        }else{
            for (int i=0; i < 10 ; i++){
                for (int j=9; j > -1 ; j--){
                    drawOnebyOne(out, i, j, pairs);
                }
                out.print(RESET_BG_COLOR);
                out.print("\n");
            }
        }

    }

    private static void drawOnebyOne(PrintStream out, int i, int j, Collection<Collection<Integer>> pairs) {
        Collection<Integer> match = new ArrayList<>();
        match.add(i);
        match.add(j);
        drawBoardHelper(out, i, j, pairs.contains(match));
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

    private static void drawMainChessBoard(PrintStream out, int i, int j, boolean highlight) {

        if (i % 2 == 0 && j % 2 == 0 || i % 2 == 1 && j % 2 == 1) {
            if (highlight){
                out.print(SET_BG_COLOR_DARK_GREEN);
            }else {
                out.print(SET_BG_COLOR_BLACK);
            }

            ChessPosition pos = new ChessPosition(i, j);
            ChessPiece piece = BOARD.getPiece(pos);

            if (piece == null) {
                out.print(EMPTY);
            } else {
                pieceColor(out, piece);
                out.print(chessPiece2String(piece));
            }

        } else {
            if (highlight){
                out.print(SET_BG_COLOR_GREEN);
            }else {
                out.print(SET_BG_COLOR_WHITE);
            }

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


    private static void positionCollector(Collection<ChessMove> possMoves, Collection<Collection<Integer>> pairs) {
        for (ChessMove pm : possMoves) {
            Collection<Integer> temp = new ArrayList<>();
            temp.add(pm.getEndPosition().getRow());
            temp.add(pm.getEndPosition().getColumn());
            pairs.add(temp);
        }
    }

    private static void drawBoardHelper(PrintStream out, int i, int j, boolean highlight) {
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
                drawMainChessBoard(out, i, j, highlight);
            }
        }
    }
}







