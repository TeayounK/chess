package ui;

import websocket.messages.ServerMessage;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    private final ChessClient client;
    private States state;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
        this.state = States.PRELOGIN;
    }
}