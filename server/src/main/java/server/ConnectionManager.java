package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.LoadGame;
import websocket.messages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    public ConcurrentHashMap<Integer, Boolean> resigns = new ConcurrentHashMap<>();


    public void add(String username, Session session, Integer gameID) {
        var connection = new Connection(username, session, gameID);
        connections.put(username, connection);
    }

    public void addResign(int gameID, boolean boo){
        resigns.put(gameID,boo);
    }

    public void removeResign(int gameID){
        resigns.remove(gameID);
    }

    public Boolean getResign(int gameID){
        return resigns.get(gameID);
    }

    public void remove(String username) {
        connections.remove(username);
    }

    public void broadcast(String excludeVisitorName, Notification notification, Integer currGameID) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.username.equals(excludeVisitorName) && c.gameID.equals(currGameID)) {
                    c.send(new Gson().toJson(notification));
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.username);
        }
    }

    public void loadGames(LoadGame lGame, Integer currGameID) throws IOException {
        for (var c : connections.values()) {
            if (c.session.isOpen() && currGameID.equals(c.gameID)) {
                c.session.getRemote().sendString(new Gson().toJson(lGame));
            }
        }
    }
}