package websocket.messages;

public class LoadGame extends ServerMessage {
    Integer game;

    public LoadGame(ServerMessageType type, Integer gameID) {
        super(type);
        this.game = gameID;
    }

}
