package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import service.Service;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.Collection;
import java.util.Map;

public class Server {
    private Service service = new Service(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());

    public int run(int desiredPort) {
        DataAccessUser dataAccessUser = new MemoryUserDAO();
        DataAccessAuth dataAccessAuth = new MemoryAuthDAO();
        DataAccessGame dataAccessGame = new MemoryGameDAO();

        this.service = new Service(dataAccessUser, dataAccessAuth, dataAccessGame);

        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    // Registration
    private String createUser(Request req, Response res) {
        var g = new Gson();
        int errorCode = 500;
        var newUser = g.fromJson(req.body(), UserData.class);
        try {
            AuthData authData = service.addUser(newUser);
            res.body(g.toJson(authData));
            res.status(200);
            return g.toJson(authData);
        } catch (DataAccessException e) {
            errorCode = switch (e.getMessage()) {
                case "Error: bad request" -> 400;
                case "Error: already taken" -> 403;
                default -> errorCode;
            };
            res.body(g.toJson(Map.of("message",e.getMessage())));
            res.status(errorCode);
            return g.toJson(Map.of("message",e.getMessage()));
        }
    }

    // Login
    private String loginUser(Request req, Response res) {
        var g = new Gson();
        int errorCode = 500;
        var userinfo = g.fromJson(req.body(), UserData.class);
        try {
            AuthData authData = service.loginUser(userinfo);
            res.body(g.toJson(authData));
            res.status(200);
            return g.toJson(authData);
        } catch (DataAccessException e){
            errorCode = switch (e.getMessage()) {
                case "Error: Unauthorized" -> 401;
                case "Error: Unknown username" -> 401;
                default -> errorCode;
            };
            res.body(g.toJson(Map.of("message",e.getMessage())));
            res.status(errorCode);
            return g.toJson(Map.of("message",e.getMessage()));
        }
    }

    private String logoutUser(Request req, Response res) {
        var g = new Gson();
        int errorCode = 500;
        String authToken = req.headers("Authorization");
        try {
            service.logoutUser(authToken);
            res.status(200);
            return g.toJson(Map.of("message",""));
        } catch (DataAccessException e){
            if (e.getMessage().equals("Error: Already logged-out username")) {
                errorCode = 401;
            }
            res.body(g.toJson(Map.of("message",e.getMessage())));
            res.status(errorCode);
            return g.toJson(Map.of("message",e.getMessage()));
        }
    }
    // Create game
    private String createGame(Request req, Response res) {
        var g = new Gson();
        int errorCode = 500;
        System.out.println(req.body());
        var gameinfo = g.fromJson(req.body(), GameData.class);
        String authToken = req.headers("Authorization");
        System.out.println(authToken);
        try{
            service.checkAuth(authToken);
            GameData newGame = service.createGame(gameinfo);
            res.status(200);
            return g.toJson(Map.of("gameID",newGame.gameID()));
        }catch (DataAccessException e){
            errorCode = switch (e.getMessage()) {
                case "Error: Unauthorized" -> 401;
                case "Error: Not a valid Game name" -> 400;
                default -> errorCode;
            };
            res.body(g.toJson(Map.of("message",e.getMessage())));
            res.status(errorCode);
            return g.toJson(Map.of("message",e.getMessage()));
        }
    }

    public String listGames(Request req, Response res){
        var g = new Gson();
        int errorCode = 500;
        String authToken = req.headers("Authorization");
        try {
            service.checkAuth(authToken);
            Collection<GameData> games = service.listGames();
            res.status(200);
            return g.toJson((Map.of("games",games)));
        }catch (DataAccessException e){
            if (e.getMessage().equals("Error: Unauthorized")) {
                errorCode = 401;
            }
            res.body(g.toJson(Map.of("message",e.getMessage())));
            res.status(errorCode);
            return g.toJson(Map.of("message",e.getMessage()));
        }
    }

    public String clearDataBase(Request req, Response res){
        var g = new Gson();
        int errorCode = 500;
        try{
            service.clearDataBase();
            res.status(200);
            return g.toJson(Map.of("message",""));
        }catch (DataAccessException e){
            res.status(errorCode);
            return g.toJson(Map.of("message",e.getMessage()));
        }

    }

    public String joinGame(Request req, Response res){
        var g = new Gson();
        int errorCode = 500;
        var gameinfo = g.fromJson(req.body(), JoinGame.class);
        String authToken = req.headers("Authorization");
        try{
            service.checkAuth(authToken);
            service.joinGame(gameinfo,authToken);
            res.status(200);
            return g.toJson(Map.of("message",""));
        }catch(DataAccessException e){
            errorCode = switch (e.getMessage()) {
                case "Error: Unauthorized" -> 401;
                case "Error: bad request" -> 400;
                case "Error: not a valid color" -> 400;
                case "Error: already taken" -> 403;
                default -> errorCode;
            };
            res.body(g.toJson(Map.of("message",e.getMessage())));
            res.status(errorCode);
            return g.toJson(Map.of("message",e.getMessage()));
        }
    }

}