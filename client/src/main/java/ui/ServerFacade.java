package ui;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.io.IOException;
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





}
