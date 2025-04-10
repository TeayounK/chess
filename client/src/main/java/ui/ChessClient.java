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

    private void initializeHashMap()throws ResponseException{
        ListResult listResult = server.listGames(this.authData);
        int i = 0;
        for (GameData game : listResult.games()) {
            i += 1;
            this.num2Game.put(i, game);
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
                this.num2Game.put(i,game);
                stringResult.append(i).append(". | GameName: ").append(game.gameName()).append(" | White User: ")
                        .append(game.whiteUsername()).append(" | Black User: ").append(game.blackUsername()).append(" |").append("\n");
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
            throw new ResponseException(400, "failure: not a valid input. \n" +
                    "Expected: <GAMENAME>");
        }
    }

    private String clearData() throws ResponseException{
        server.deleteDataBase();
        state = States.PRELOGIN;
        return "Successfully cleaned DataBase";
    }

    private String joinGame(String... params) throws ResponseException{
        assertLogIn();
        try {
            int gameNum = Integer.parseInt(params[0]);
            JoinGame joinGame = new JoinGame(params[1], this.num2Game.get(gameNum).gameID());
            server.joinGame(this.authData, joinGame);
            state = States.GAME;
            Board.main(joinGame);
            return "Successfully joined a game";
        }catch(NumberFormatException ex){
            throw new ResponseException(ex.hashCode(), "failure: not a valid game number. \n" +
                    "<GAME NUMBER> has to be integer");
        }catch(NullPointerException ex){
            throw new ResponseException(ex.hashCode(), "failure: not a valid game number. \n" +
                    "<GAME NUMBER> is not on the list");
        }catch(ResponseException ex){
            if ((params.length == 2)) {
                throw new ResponseException(ex.hashCode(), "failure: not a valid color. \n" +
                        "<COLOR> has to be \"white\" or \"black\"");
            }else if (params.length > 2){
                throw new ResponseException(400, "failure: not a valid input. \n" +
                        "Expected: <game NUMBER> <playerColor>");
            }else{
                throw new ResponseException(400, "failure: not a valid input. \n" +ex.getMessage());
            }
        }catch(ArrayIndexOutOfBoundsException ex){
            throw new ResponseException(400, "failure: not a valid input. \n" +
                    "Expected: <playerColor> <game NUMBER>");
        }
    }


}
