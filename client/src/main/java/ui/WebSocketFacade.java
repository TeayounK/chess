package ui;

import com.google.gson.Gson;
import model.AuthData;
import model.JoinGame;
import websocket.commands.CandM;
import websocket.commands.Move;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.Session;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint{

    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }

    // not sure but good attempt. need a test.
    public void leaveGame(AuthData authData, JoinGame joinGame) throws ResponseException{
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE,
                    authData.authToken(), joinGame.gameID());
            session.getBasicRemote().sendText(new Gson().toJson(command));
            session.close();
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void joinGame(AuthData authData, JoinGame joinGame) throws ResponseException{
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT,
                    authData.authToken(), joinGame.gameID());
            session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void makeMove(AuthData authData, JoinGame joinGame, Move move) throws ResponseException{
        try{
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE,
                    authData.authToken(), joinGame.gameID());
            CandM commandAndMove = new CandM(command,move);
            session.getBasicRemote().sendText(new Gson().toJson(commandAndMove));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
