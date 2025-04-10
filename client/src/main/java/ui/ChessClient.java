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

    private String l(String... params) throws ResponseException{
        if (state == States.PRELOGIN){
            return this.logIn(params);
        }else if (state == States.LOGIN){
            return this.listGame();
        }else {
            throw new ResponseException(400, "failure: not a valid command.");
        }
    }


    private String logIn(String... params) throws ResponseException {
        assertPreLogin();
        if (params.length == 2){
            this.authData = server.loginUser(params);
            initializeHashMap();
            state = States.LOGIN;
            username = authData.username();
            return String.format("You logged in as %s.", this.authData.username());
        }else{
            throw new ResponseException(400, "failure: not a valid input. \n" +
                    "Expected: <USERNAME> <PASSWORD>");
        }
    }

    private String register(String... params) throws ResponseException{
        assertPreLogin();
        if (params.length == 3){
            try {
                AuthData result = server.createUser(params);
                this.authData = server.loginUser(params[0], params[1]);
                state = States.LOGIN;
                username = authData.username();
                return String.format("You registered in as %s.", result.username());
            }catch(ResponseException e){
                if (e.getMessage().equalsIgnoreCase("failure: Forbidden")){
                    throw new ResponseException(400,"failure: already existing user");
                }else{
                    System.out.println(e.getMessage());
                    throw new ResponseException(400,"failure: not a valid input. \n" +
                            "Expected: <USERNAME> <PASSWORD> <EMAIL>");
                }
            }
        }else{
            throw new ResponseException(400, "failure: not a valid input. \n" +
                    "Expected: <USERNAME> <PASSWORD> <EMAIL>");
        }
    }


}
