package ui;

import model.*;

import java.util.Arrays;

import static ui.Repl.*;

public class ChessClient {
    private final ServerFacade server;
    public States state;
    private AuthData authData;


    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
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

                default -> "Calling help";
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
            throw new ResponseException(400, "Not a valid command");
        }
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
            state = States.LOGIN;
            AuthData result = server.createUser(params);
            this.authData = server.loginUser(params[0],params[1]);
            return String.format("You registered in as %s.", result.username());

        }else{
            throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
        }
    }

    private String logout() throws ResponseException{
        try{
            assertLogIn();
            server.logoutUser(authData);
            state = States.PRELOGIN;
            return "You successfully logged out";
        }catch(ResponseException e){
            throw new ResponseException(400, e.getMessage());
        }

    }

    private String listGame() throws ResponseException{
        try{
            StringBuilder stringResult = new StringBuilder();
            int i = 0;
            assertLogIn();
            ListResult listResult = server.listGames(this.authData);
            for (GameData game : listResult.games()){
                i+= 1;
                stringResult.append(i).append(".").append(game.gameName()).append(" ")
                        .append(game.whiteUsername()).append(" ").append(game.blackUsername()).append("\n");
            }
            return stringResult.toString();
        }catch(ResponseException e){
            throw new ResponseException(400, e.getMessage());
        }
    }

    private String createGame(String... params) throws ResponseException{
        assertLogIn();
        if (params.length==1){
            state = States.LOGIN;
            server.createGame(this.authData,params);
            return "You created a game";
        }else{
            throw new ResponseException(400, "Expected: <GAMENAME>");
        }
    }

    private String clearData() throws ResponseException{
        server.deleteDataBase();
        state = States.PRELOGIN;
        return "Successfully cleaned DataBase";
    }

    private String joinGame(String... params) throws ResponseException{
        assertLogIn();
        if (params.length == 2){
            JoinGame joinGame = new JoinGame(params[1], Integer.parseInt(params[0]));
            server.joinGame(this.authData,joinGame);
            state = States.GAME;
            Board boardDraw = new Board();
            boardDraw.main(null);
            return "Successfully joined a game";
        }else{
            throw new ResponseException(400, "Expected: <playerColor> <gameID>");
        }
    }

    private String watchGame(String... params) throws ResponseException{
        assertLogIn();
        if (params.length == 1){
            state = States.GAME;
            Board boardDraw = new Board();
            boardDraw.main(null);
            return "Successfully enter the game as an observer";
        }else{
            throw new ResponseException(400, "Expected: <gameID>");
        }
    }

    private String leaveGame() throws ResponseException{
        assertGame();
        state = States.LOGIN;
        return "You have logged out";
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
