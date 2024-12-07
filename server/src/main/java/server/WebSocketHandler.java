package server;


import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case CONNECT -> enter(action.getGameID(), session, );
            case MAKE_MOVE -> "";
            case LEAVE -> "";
            case RESIGN -> "";
        }
    }

    private void enter(String username, Session session, Connection.Type type) throws IOException {
        connections.add(username, session, type);
        String userType = switch(type){
            case OBSERVER -> "an observer";
            case PLAYER -> playerColer(username);
        };
        var message = username + " joins the game as " + userType;
        var notification = new Notification(Notification.Type.ARRIVAL, message);
        connections.broadcast(username, notification);
    }

    private String playerColer(String username){

    }

}
