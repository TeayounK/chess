package ui;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import model.*;
import websocket.commands.Move;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.util.Arrays;
import java.util.HashMap;


public class ChessClient implements NotificationHandler{
    private final ServerFacade server;
    private final NotificationHandler notificationHandler;
    public States state;
    private AuthData authData;
    private HashMap<Integer, GameData> num2Game;
    private String username;
    private WebSocketFacade ws;
    private JoinGame joinGame;
    private JoinGame gameObserver;
    private final String serverUrl;
    private Board board;
    private int colornumber;


    public ChessClient(String serverUrl, NotificationHandler notificationHandler) {
        server = new ServerFacade(serverUrl);
        this.state = States.PRELOGIN;
        this.authData = null;
        this.num2Game = new HashMap<>();
        this.username = "";
        this.joinGame = new JoinGame("null",0);
        this.gameObserver = new JoinGame("null",0);
        this.notificationHandler = notificationHandler;
        this.serverUrl = serverUrl;
        board = new Board(null,0);
        colornumber = 0;

    }

    private Integer charToInt(String input){
        Integer result = switch (input) {
            case "a" -> 1;
            case "b" -> 2;
            case "c" -> 3;
            case "d" -> 4;
            case "e" -> 5;
            case "f" -> 6;
            case "g" -> 7;
            case "h" -> 8;
            default -> 0;
        };
        return result;
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
                case "r" -> r(params);
                case "quit" -> "quit";
                case "q" -> "quit";
                case "clear" -> clearData();
                // commends in login phase
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "c" -> c(params);
                case "join" -> joinGame(params);
                case "j" -> joinGame(params);
                case "watch" -> watchGame(params);
                case "w" -> watchGame(params);
                // commends in game phase
                case "leave" -> leaveGame();
                case "hi" -> highlight(params);
                case "highlight" -> highlight(params);
                case "redrew" -> redrawBoard();
                case "m" -> makeMove(params);
                case "make" -> makeMove(params);
                case "move" -> makeMove(params);
                case "color" -> c(params);
                case "res" -> reallyResign();
                case "y" -> yesResign();
                case "n" -> noResign();


                default -> "";
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String noResign() throws ResponseException {
        assertResign();
        state = States.GAME;
        return "Successfully not resign the game.";
    }

    private String yesResign() throws ResponseException {
        assertResign();
        state = States.GAME;
        ws.resignRequest(authData,joinGame);
        return "Successfully resign the game. \n Any further moves are prohibited.";
    }

    private String reallyResign() throws ResponseException {
        assertInGame();
        state = States.RESIGN;
        return "You commanded 'resign' the game.";
    }

    private String c(String... params) throws ResponseException{
        System.out.println("<colorTest>");
        if (state == States.LOGIN){
            return this.createGame(params);
        }else if (state == States.GAME || state == States.WATCH){
            return this.setColor(params);
        }else {
            throw new ResponseException(400, "failure: not a valid command.");
        }
    }

    private String setColor(String[] params) throws ResponseException {
        assertGame();
        if (params.length == 1) {
            try {
                colornumber = Integer.parseInt(params[0]);;
                System.out.println(colornumber);

                board = new Board(ws.getUpdatedBoard(),colornumber);
                if (state == States.WATCH){
                    board.main(gameObserver, 0,0);
                }else{
                    board.main(joinGame,0,0);
                }
                return "Successfully redraws the board";

            } catch (NumberFormatException e) {
                throw new ResponseException(e.hashCode(), "failure: not a valid color number. \n" +
                        "<color number> has to be an integer of [0,1,2,3]");
            }
        }else{
            throw new ResponseException(400, "failure: Not a valid command. \n"+
                    "Expected: <color number>");
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

    private String r(String... params) throws ResponseException{
        if (state == States.PRELOGIN){
            return this.register(params);
        }else if (state == States.GAME ||state == States.WATCH ){
            return this.redrawBoard();
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
            joinGame = new JoinGame(params[1], this.num2Game.get(gameNum).gameID());
            server.joinGame(this.authData, joinGame);
            state = States.GAME;

            ws = new WebSocketFacade(this.serverUrl, notificationHandler);
            ws.connectGame(this.authData, joinGame);

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

    private String watchGame(String... params) throws ResponseException{
        assertLogIn();
        if (params.length == 1){
            try{
                int gameNum = Integer.parseInt(params[0]);
                gameObserver = new JoinGame("white", this.num2Game.get(gameNum).gameID());
                state = States.WATCH;

                ws = new WebSocketFacade(this.serverUrl, notificationHandler);
                ws.connectGame(this.authData, gameObserver);

                board = new Board(ws.getUpdatedBoard(),colornumber);
                board.main(gameObserver, 0,0);

                return "Successfully enter the game as an observer";
            }catch(NumberFormatException ex){
                throw new ResponseException(ex.hashCode(), "failure: not a valid game number. \n" +
                        "<GAME NUMBER> has to be integer");
            }catch(NullPointerException ex){
                throw new ResponseException(ex.hashCode(), "failure: not a valid game number. \n" +
                        "<GAME NUMBER> is not on the list");
            }
        }else{
            throw new ResponseException(400, "failure: not a valid input. \n" +
                    "Expected: <game NUMBER>");
        }
    }

    private String leaveGame() throws ResponseException{
        assertGame();
        state = States.LOGIN;
        ws.leaveGame(this.authData,joinGame);
        ws = null;
        return "You have successfully left the game";
    }

    private String makeMove(String... params) throws ResponseException {
        assertInGame();
        if (params.length == 2){
            try{
                char[] source = params[0].toCharArray();
                int si = Integer.parseInt(String.valueOf(source[1]));
                int sj = charToInt(String.valueOf(source[0]));
                if (sj==0){
                    throw new ResponseException(403, "failure: not a valid source position character. \n"
                            + "The character has to be one of the following characters, [a,b,c,d,e,f,g]");
                }

                char[] desti = params[1].toCharArray();
                int di = Integer.parseInt(String.valueOf(desti[1]));
                int dj = charToInt(String.valueOf(desti[0]));
                if (dj==0){
                    throw new ResponseException(403, "failure: not a valid destination position character. \n"
                            + "The character has to be one of the following characters, [a,b,c,d,e,f,g]");
                }

                Move move2make = new Move(new ChessMove(new ChessPosition(si,sj),
                                                        new ChessPosition(di,dj),
                                                            null));

                ws.makeMove(authData, joinGame, move2make);

                return " ";
            }catch(NumberFormatException ex) {
                throw new ResponseException(ex.hashCode(), "failure: not a valid position number. \n" +
                        "<position> has to be character + integer (e.g. f5)");
            }
        }else if (params.length == 3){
            try{
                ChessPiece.PieceType promo = null;
                if (params[2].equals("Q")||params[2].equals("QUEEN")){
                    promo = ChessPiece.PieceType.QUEEN;
                }else if(params[2].equals("R")||params[2].equals("ROOK")){
                    promo = ChessPiece.PieceType.ROOK;
                }else if(params[2].equals("B")||params[2].equals("BISHOP")){
                    promo = ChessPiece.PieceType.BISHOP;
                }else if(params[2].equals("K")||params[2].equals("KNIGHT")){
                    promo = ChessPiece.PieceType.KNIGHT;
                }else{
                    throw new ResponseException(403, "Not a valid promotion command. \n"
                    + "Leave it empty if you meant not to promote. But if you meant to promote, \n"
                    + "type one of followings when you promote your pawn in which that is applicable."
                            + "<source> <destination> <Q>/<QUEEN> -> promote a pawn to queen \n"
                            + "<source> <destination> <R>/<ROOK> -> promote a pawn to rook \n"
                            + "<source> <destination> <B>/<BISHOP> -> promote a pawn to bishop \n"
                            + "<source> <destination> <K>/<KNIGHT> -> promote a pawn to knight \n"
                    );
                }

                char[] source = params[0].toCharArray();
                int si = Integer.parseInt(String.valueOf(source[1]));
                int sj = charToInt(String.valueOf(source[0]));
                if (sj==0){
                    throw new ResponseException(403, "failure: not a valid source position character. \n"
                            + "The character has to be one of the following characters, [a,b,c,d,e,f,g]");
                }

                char[] desti = params[1].toCharArray();
                int di = Integer.parseInt(String.valueOf(desti[1]));
                int dj = charToInt(String.valueOf(desti[0]));
                if (dj==0){
                    throw new ResponseException(403, "failure: not a valid destination position character. \n"
                            + "The character has to be one of the following characters, [a,b,c,d,e,f,g]");
                }

                Move move2make = new Move(new ChessMove(new ChessPosition(si,sj),
                                                        new ChessPosition(di,dj),
                                                        promo));

                ws.makeMove(authData, joinGame, move2make);

                return " ";

            }catch(NumberFormatException ex) {
                throw new ResponseException(ex.hashCode(), "failure: not a valid position number. \n" +
                        "<position> has to be character + integer (e.g. f5)");
            }
        }else{
            throw new ResponseException(400, "failure: not a valid input. \n" +
                    "Expected: <source> <destination> <optional promotion> (e.g. f5 e4 q)");
        }
    }

    // in game functions
    private String highlight(String... params) throws ResponseException{
        assertGame();
        if (params.length == 1){
            try{
                 // transport strings to i and j and make a chess position variable as "highlight"
                char[] cArray = params[0].toCharArray();
                int i = Integer.parseInt(String.valueOf(cArray[1]));
                int j = charToInt(String.valueOf(cArray[0]));
                if (j == 0){
                    throw new ResponseException(403, "failure: not a valid position character. \n"
                    + "The character has to be one of the following characters, [a,b,c,d,e,f,g]");
                }

                state = States.GAME;
                board = new Board(ws.getUpdatedBoard(),colornumber);
                board.main(joinGame, i, j);

                return "Successfully highlights possible moves";
            }catch(NumberFormatException ex){
                throw new ResponseException(ex.hashCode(), "failure: not a valid position number. \n" +
                        "<position> has to be character + integer (e.g. f5)");
            }catch(NullPointerException ex){
                throw new ResponseException(ex.hashCode(), "failure: no piece is found. \n" +
                        "Enter a position again (e.g. f5)");
            }
        }else{
            throw new ResponseException(400, "failure: not a valid input. \n" +
                    "Expected: <position> (e.g. f5)");
        }
    }

    private String redrawBoard() throws ResponseException{
        assertGame();
        board = new Board(ws.getUpdatedBoard(),colornumber);
        if (state == States.WATCH){
            board.main(gameObserver, 0,0);
        }else{
            board.main(joinGame,0,0);
        }
        return "Successfully redraws the board";
    }


    // checking status
    private void assertLogIn() throws ResponseException {
        if (state != States.LOGIN) {
            throw new ResponseException(400, "failure: This is a commend for logged in users");
        }
    }

    private void assertPreLogin() throws ResponseException {
        if (state != States.PRELOGIN) {
            throw new ResponseException(400, "failure: This is a commend for pre-login users");
        }
    }
    private void assertGame() throws ResponseException {
        if (state != States.GAME && state != States.WATCH) {
            throw new ResponseException(400, "failure: This is a commend for users in a game");
        }
    }
    private void assertInGame() throws ResponseException {
        if (state != States.GAME) {
            throw new ResponseException(400, "failure: This is a commend for users in a game");
        }
    }
    private void assertResign() throws ResponseException {
        if (state != States.RESIGN) {
            throw new ResponseException(400, "failure: This is a commend for users who request resign");
        }
    }


    // perhaps I can make them public and call them from Repl to print out notifications

    @Override
    public void notify(String message) {
        Notification notification = new Gson().fromJson(message, Notification.class);
        System.out.println("Notification: "+ notification.getMessage());
    }
}
