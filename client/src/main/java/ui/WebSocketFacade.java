package ui;

import chess.ChessBoard;
import com.google.gson.Gson;
import model.AuthData;
import model.JoinGame;
import websocket.commands.CandM;
import websocket.commands.Move;
import websocket.commands.UserGameCommand;
import websocket.messages.Notification;
import websocket.messages.LoadGame;
import websocket.messages.ServerMessage;

import javax.websocket.Session;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint{

    Session session;
    NotificationHandler notificationHandler;
    ChessBoard updatedBoard;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;
            this.updatedBoard = new ChessBoard();
            this.updatedBoard.resetBoard();

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    // need something to avoid loadGame == null
                    LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
                    updatedBoard = loadGame.getChessGame().getBoard();

                    Notification notificationMessage = new Gson().fromJson(message, Notification.class);

                    System.out.println(notificationMessage.getServerMessageType());
                    System.out.println(notificationMessage.getMessage());

                    switch (notificationMessage.getServerMessageType()){
                        case LOAD_GAME -> loadingGame(message);
                        case NOTIFICATION -> alarm(message);
                    }

                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private void loadingGame(String message) {
        LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
        if (!loadGame.equals(new LoadGame(null,null,null, null))){
            Board board = new Board(loadGame.getChessGame().getBoard(),0);
            JoinGame joinGame = new JoinGame(loadGame.getUserColor().toString(),loadGame.getGameID());
            board.main(joinGame,0,0);
            System.out.println(" ");
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
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
            this.session.close();
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void connectGame(AuthData authData, JoinGame joinGame) throws ResponseException{
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT,
                    authData.authToken(), joinGame.gameID());
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void makeMove(AuthData authData, JoinGame joinGame, Move move) throws ResponseException{
        try{
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE,
                    authData.authToken(), joinGame.gameID());
            CandM commandAndMove = new CandM(command,move);
            this.session.getBasicRemote().sendText(new Gson().toJson(commandAndMove));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public ChessBoard getUpdatedBoard(){
        return updatedBoard;
    }

    public void alarm(String message){
        System.out.println("<Alarm>");
        notificationHandler.notify(message);
        Notification notification = new Gson().fromJson(message, Notification.class);
        switch (notification.getServerMessageType()){
            case NOTIFICATION -> System.out.println("Notification: " + notification.getMessage());
            case ERROR -> System.out.println("Error: " + notification.getMessage());
        }
    }

    public void resignRequest(AuthData authData, JoinGame joinGame) throws ResponseException{
        try {
            UserGameCommand resignRequest = new UserGameCommand(UserGameCommand.CommandType.RESIGN,
                    authData.authToken(), joinGame.gameID());
            this.session.getBasicRemote().sendText(new Gson().toJson(resignRequest));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
