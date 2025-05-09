package ui;

import com.google.gson.Gson;
import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }
    public AuthData createUser(String... params) throws ResponseException{
        var path = "/user";
        UserData newUser = new UserData(params[0], params[1], params[2]);
        return this.makeRequest("POST", path, newUser, AuthData.class, null);
    }

    public AuthData loginUser(String... params) throws ResponseException{
        var path = "/session";
        UserData newUser = new UserData(params[0], params[1], null);
        return this.makeRequest("POST", path, newUser, AuthData.class, null);
    }

    public void logoutUser(AuthData authData) throws ResponseException{
        var path = "/session";
        this.makeRequest("DELETE", path,null,null, authData);
    }
    public GameData createGame(AuthData authData, String... params) throws ResponseException{
        var path = "/game";
        GameData newGame = new GameData(0,null,null,params[0],null);
        return this.makeRequest("POST", path, newGame, GameData.class, authData);
    }

    public ListResult listGames(AuthData authData) throws ResponseException{
        var path = "/game";
        return this.makeRequest("GET", path,null,ListResult.class, authData);
    }

    public void deleteDataBase() throws ResponseException{
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    public void joinGame(AuthData authData, JoinGame joinGame) throws ResponseException{
        var path = "/game";
        this.makeRequest("PUT",path,joinGame,null,authData);
    }

    public void leaveGame(AuthData authData, JoinGame joinGame) throws ResponseException{
        var path = "/game";
        this.makeRequest("DELETE",path,joinGame,null,authData);
    }



    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, AuthData authData) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            if (authData != null) {
                http.addRequestProperty("Authorization",authData.authToken());
            }
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        var mess = http.getResponseMessage();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + mess);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
