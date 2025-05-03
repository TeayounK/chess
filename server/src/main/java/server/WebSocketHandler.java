package server;

import com.google.gson.Gson;
import dataaccess.DataAccessAuth;
import dataaccess.DataAccessException;
import dataaccess.DataAccessGame;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGame;
import websocket.messages.Notification;

import java.io.IOException;
import java.lang.module.ResolutionException;
import java.util.ArrayList;
import java.util.Objects;

import static websocket.messages.ServerMessage.*;

@WebSocket
public class WebSocketHandler {

    private final DataAccessAuth dataAccessAuth;
    private final DataAccessGame dataAccessGame;
    private final ConnectionManager connections = new ConnectionManager();


    public WebSocketHandler(DataAccessAuth dataAccessAuth, DataAccessGame dataAccessGame) {
        this.dataAccessAuth = dataAccessAuth;
        this.dataAccessGame = dataAccessGame;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {

        try{
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            if(dataAccessAuth.checkAuth(command.getAuthToken())) {
                String username = dataAccessAuth.getUser(command.getAuthToken());

                connections.add(username ,session);

                switch (command.getCommandType()){
                    case CONNECT -> connectGame(session,username,command);
                    case LEAVE -> leaveGame("??",session);
                    case MAKE_MOVE -> makeMove();
                    case RESIGN -> resign();
                }
            }

        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "Error: Unauthorized")){
                ErrorMessage errorMessage = new ErrorMessage(ServerMessageType.ERROR, "Error: Unauthorized");
                try {
                    session.getRemote().sendString(new Gson().toJson(errorMessage));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    private void resign() {
    }

    private void makeMove() {

    }

    private void connectGame(Session session, String username, UserGameCommand command) {
        // LOAD_GAME, NOTIFICATION
        try {
            ArrayList<Integer> gameIDs = new ArrayList<>();
            for (GameData game :this.dataAccessGame.getGames()){
                gameIDs.add(game.gameID());
            }

            if (!gameIDs.contains(command.getGameID())) {
                ErrorMessage errorMessage = new ErrorMessage(ServerMessageType.ERROR, "Error: Wrong GameID");
                session.getRemote().sendString(new Gson().toJson(errorMessage));
            }else{
                LoadGame loadGame = new LoadGame(ServerMessageType.LOAD_GAME,command.getGameID());
                session.getRemote().sendString(new Gson().toJson(loadGame));

                Notification notification = new Notification(ServerMessageType.NOTIFICATION,
                        new Gson().toJson(username + " had been connected"));
                connections.broadcast(username,notification);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private void leaveGame(String username, Session session){
        // LOAD_GAME
    };

}
