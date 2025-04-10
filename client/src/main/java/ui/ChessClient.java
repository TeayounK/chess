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

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                // commends in preLogin phase
                case "login" -> logIn(params);
                case "list" -> listGame();
                case "l" -> l(params);
                case "register" -> register(params);
                case "r" -> register(params);
                case "quit" -> "quit";
                case "q" -> "quit";
                case "clear" -> clearData();
                // commends in login phase
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "c" -> createGame(params);
                case "join" -> joinGame(params);
                case "j" -> joinGame(params);
                case "watch" -> watchGame(params);
                case "w" -> watchGame(params);
                // commends in game phase
                case "leave" -> leaveGame();

                default -> "";
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }


}
