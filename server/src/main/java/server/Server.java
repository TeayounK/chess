package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.JoinGame;
import model.UserData;
import spark.*;
import service.Service;

import java.util.Collection;
import java.util.Map;


public class Server {
    private final Service service = new Service(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::CreateUser);

        Spark.post("/session", this::LoginUser);

        Spark.delete("/session", this::LogoutUser);

        Spark.post("/game", this::CreateGame);

        Spark.get("/game", this::ListGames);

        Spark.delete("/db", this::ClearDataBase);

        Spark.put("/game", this::JoinGame);

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
    private String CreateUser(Request req, Response res) {
        var g = new Gson();
        int errorCode = 500;
        var newUser = g.fromJson(req.body(), UserData.class);
        try {
            AuthData authData = service.addUser(newUser);
            res.body(g.toJson(authData));
            res.status(200);
            return g.toJson(authData);
        } catch (DataAccessException e) {
            switch (e.getMessage()){
                case "Error: bad request":
                    errorCode = 400;
                    break;
                case "Error: already taken":
                    errorCode = 403;
                    break;
            }
            res.body(g.toJson(Map.of("message",e.getMessage())));
            res.status(errorCode);
            return g.toJson(Map.of("message",e.getMessage()));
        }
    }


    // Login
    private String LoginUser(Request req, Response res) {
        var g = new Gson();
        int errorCode = 500;
        var userinfo = g.fromJson(req.body(), UserData.class);
        try {
            AuthData authData = service.loginUser(userinfo);
            res.body(g.toJson(authData));
            res.status(200);
            return g.toJson(authData);
        } catch (DataAccessException e){
            switch (e.getMessage()){
                case "Error: Unauthorized":
                    errorCode = 401;
                    break;
                case "Error: Unknown username":
                    errorCode = 401;
                    break;
            }
            res.body(g.toJson(Map.of("message",e.getMessage())));
            res.status(errorCode);
            return g.toJson(Map.of("message",e.getMessage()));
        }
    }

    private String LogoutUser(Request req, Response res) {
        var g = new Gson();
        int errorCode = 500;
        String authToken = req.headers("Authorization");
        try {
            service.logoutUser(authToken);
            res.status(200);
            return g.toJson(Map.of("message",""));
        } catch (DataAccessException e){
            switch (e.getMessage()){
                case "Error: Already logged-out username":
                    errorCode = 401;
                    break;
            }
            res.body(g.toJson(Map.of("message",e.getMessage())));
            res.status(errorCode);
            return g.toJson(Map.of("message",e.getMessage()));
        }
    }

    private String CreateGame(Request req, Response res) {
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
            switch (e.getMessage()){
                case "Error: Unauthorized":
                    errorCode = 401;
                    break;
                case "Error: Not a valid Game name":
                    errorCode = 400;
                    break;
            }
            res.body(g.toJson(Map.of("message",e.getMessage())));
            res.status(errorCode);
            return g.toJson(Map.of("message",e.getMessage()));
        }
    }

    public String ListGames(Request req, Response res){
        var g = new Gson();
        int errorCode = 500;
        String authToken = req.headers("Authorization");
        try {
            service.checkAuth(authToken);
            Collection<GameData> games = service.listGames();
            res.status(200);
            return g.toJson((Map.of("games",games)));
        }catch (DataAccessException e){
            switch (e.getMessage()){
                case "Error: Unauthorized":
                    errorCode = 401;
                    break;
            }
            res.body(g.toJson(Map.of("message",e.getMessage())));
            res.status(errorCode);
            return g.toJson(Map.of("message",e.getMessage()));
        }
    }

    public String ClearDataBase(Request req, Response res){
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

    public String JoinGame(Request req, Response res){
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
            switch (e.getMessage()){
                case "Error: Unauthorized":
                    errorCode = 401;
                    break;
                case "Error: bad request":
                    errorCode = 400;
                    break;
                case "Error: already taken":
                    errorCode = 403;
            }
            res.body(g.toJson(Map.of("message",e.getMessage())));
            res.status(errorCode);
            return g.toJson(Map.of("message",e.getMessage()));
        }
    }
}
