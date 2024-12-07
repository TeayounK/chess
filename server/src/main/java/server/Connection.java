package server;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String username;
    public Session session;
    public Type type;

    public Connection(String username, Session session, Type type){
        this.username = username;
        this.session = session;
        this.type = type;
    }

    public enum Type {
        OBSERVER,
        PLAYER
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}
