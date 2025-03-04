package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import service.Service;
import spark.Request;
import spark.Response;
import spark.Spark;

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

}