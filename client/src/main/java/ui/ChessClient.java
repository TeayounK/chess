package ui;

import model.AuthData;
import model.GameData;
import model.ListResult;
import model.UserData;

import java.util.Arrays;

import static ui.Repl.*;

public class ChessClient {
    private ServerFacade server;
    private final String serverUrl;
    public States state;
    private AuthData authData;


    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.state = States.PRELOGIN;
        this.authData = null;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                // commends in preLogin phase
                case "login" -> logIn(params);
                case "l" -> L(params);
                case "register" -> register(params);
                case "r" -> register(params);
                case "quit" -> "quit";
                case "q" -> clearData();
                // commends in login phase
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "c" -> createGame(params);
                case "join" -> joinGame(params);
                case "j" -> joinGame(params);
                case "watch" -> watchGame(params);
                case "w" -> watchGame(params);

                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String L(String... params) throws ResponseException{
        if (state == States.PRELOGIN){
            return this.logIn(params);
        }else if (state == States.LOGIN){
            return this.listGame();
        }else {
            throw new ResponseException(400, "Not a valid command");
        }
    }

    private String help() {
        return switch (this.state){
            case States.PRELOGIN -> printPromptPreLogin();
            case States.LOGIN -> printPromptLogin();
            case States.GAME -> printPromptGame();
        };
    }

    private String logIn(String... params) throws ResponseException {
        assertPreLogin();
        if (params.length == 2){
            this.authData = server.loginUser(params);
            state = States.LOGIN;
            return String.format("You logged in as %s.", this.authData.username());
        }else{
            throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD>");
        }
    }
    
    private String register(String... params) throws ResponseException{
        assertPreLogin();
        if (params.length == 3){
            state = States.PRELOGIN;
            UserData result = server.createUser(params);
            this.authData = server.loginUser(params[0],params[1]);
            return String.format("You registered in as %s.", result.username());

        }else{
            throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
        }
    }

    private String logout() throws ResponseException{
        try{
            assertLogIn();
            state = States.PRELOGIN;
            server.logoutUser();
            return "You successfully logged out";
        }catch(ResponseException e){
            throw new ResponseException(400, e.getMessage());
        }

    }

    private String listGame() throws ResponseException{
        try{
            String stringResult = "";
            int i = 0;
            assertLogIn();
            ListResult listResult = server.listGames(this.authData);
            for (GameData game : listResult.games()){
                i+= 1;
                stringResult += i + "." + game.gameName() + " " + game.whiteUsername() + " " + game.blackUsername() +"\n";
            }
            return stringResult;
        }catch(ResponseException e){
            throw new ResponseException(400, e.getMessage());
        }
    }

    private String createGame(String... params) throws ResponseException{
        assertLogIn();
        if (params.length==1){
            state = States.LOGIN;
            GameData result = server.createGame(this.authData,params);
            return String.format("You created a game with game ID: %s.", result.gameID());
        }else{
            throw new ResponseException(400, "Expected: <GAMENAME>");
        }
    }

    private String clearData() throws ResponseException{
        state = States.PRELOGIN;
        server.deleteDataBase();
        return "Successfully cleaned DataBase";
    }

    private String joinGame(String... params) throws ResponseException{
        assertPreLogin();
        if (params.length == 2){
            state = States.GAME;
            server.joinGame(this.authData,params);
            return "Successfully joined a game";
        }else{
            throw new ResponseException(400, "Expected: <playerColor> <gameID>");
        }
    }

    private String watchGame(String... params) throws ResponseException{
        assertLogIn();
        if (params.length == 1){
            state =
        }
    }

    // checking status
    private void assertLogIn() throws ResponseException {
        if (state != States.LOGIN) {
            throw new ResponseException(400, "This is a commend for logged in users");
        }
    }

    private void assertPreLogin() throws ResponseException {
        if (state != States.PRELOGIN) {
            throw new ResponseException(400, "This is a commend for pre-login users");
        }
    }
    private void assertGame() throws ResponseException {
        if (state != States.GAME) {
            throw new ResponseException(400, "This is a commend for users in a game");
        }
    }
}
