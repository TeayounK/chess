package ui;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class ChessColors {
    private String white = SET_TEXT_COLOR_RED;
    private String black;

    public ChessColors(String white, String black) {
        this.white = white;
        this.black = black;
    }

    public String getWhite() {
        return white;
    }

    public String getBlack() {
        return black;
    }
}
