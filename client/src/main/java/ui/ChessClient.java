package ui;

import model.*;

import java.util.Arrays;
import java.util.HashMap;


public class ChessClient {
    private final ServerFacade server;
    public States state;
    private AuthData authData;
    private HashMap<Integer, GameData> num2Game;
    private String username;
    private WebSocketFacade ws;


    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.state = States.PRELOGIN;
        this.authData = null;
        this.num2Game = new HashMap<>();
        this.username = "";

    }


}
