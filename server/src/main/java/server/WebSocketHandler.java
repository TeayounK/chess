package server;

import chess.ChessBoard;
import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessAuth;
import dataaccess.DataAccessException;
import dataaccess.DataAccessGame;
import model.GameData;
import model.JoinGame;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.CandM;
import websocket.commands.Move;
import websocket.commands.UserGameCommand;

import websocket.messages.LoadGame;
import websocket.messages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static websocket.messages.ServerMessage.*;

@WebSocket
public class WebSocketHandler {

    private final DataAccessAuth dataAccessAuth;
    private final DataAccessGame dataAccessGame;
    private final ConnectionManager connections = new ConnectionManager();
    private final ArrayList<Integer> gameIDs;



    public WebSocketHandler(DataAccessAuth dataAccessAuth, DataAccessGame dataAccessGame) {
        this.dataAccessAuth = dataAccessAuth;
        this.dataAccessGame = dataAccessGame;
        gameIDs = new ArrayList<>();

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try{
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            Move move = new Gson().fromJson(message, Move.class);

            if (command.equals(new UserGameCommand(null,null,null))){
                CandM candM = new Gson().fromJson(message, CandM.class);
                command = candM.getUserGameCommand();
                move = candM.getMove();
            }

            if(dataAccessAuth.checkAuth(command.getAuthToken())) {
                String username = dataAccessAuth.getUser(command.getAuthToken());

                connections.add(username ,session, command.getGameID());

                switch (command.getCommandType()){
                    case CONNECT -> connectGame(session,username,command);
                    case LEAVE -> leaveGame(session, username, command);
                    case MAKE_MOVE -> isResign(session, username, command, move);
                    case RESIGN -> resign(session, username, command);
                }
            }
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "Error: Unauthorized")){
                Notification errorMessage = new Notification(ServerMessageType.ERROR,
                        "Error: Unauthorized");
                try {
                    session.getRemote().sendString(new Gson().toJson(errorMessage));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    private void resign(Session session, String username, UserGameCommand command) {
        try {
            Integer gameID = command.getGameID();
            if (!gameIDs.contains(gameID)||connections.getResign(gameID)) {
                Notification errorMessage = new Notification(ServerMessageType.ERROR,
                        "Error: Wrong GameID");
                session.getRemote().sendString(new Gson().toJson(errorMessage));
            } else {
                Collection<GameData> games = dataAccessGame.getGames();
                for (GameData game : games) {
                    gameValidationChecks(session, username, game, gameID);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void gameValidationChecks(Session session, String username, GameData game, Integer gameID)
            throws IOException {
        if(game.gameID() == gameID) {
            if (game.game()==null){
                Notification errorMessage = new Notification(ServerMessageType.ERROR,
                        "Error: Invalid command");
                session.getRemote().sendString(new Gson().toJson(errorMessage));
            }else {
                connections.removeResign(gameID);
                connections.addResign(gameID,true);
                if (username.equals(game.whiteUsername())){

                    Notification notification = new Notification(ServerMessageType.NOTIFICATION,
                            new Gson().toJson("White player, " + username + ", resigned."));
                    connections.broadcast("",notification, gameID);


                }else if (username.equals(game.blackUsername())){

                    Notification notification = new Notification(ServerMessageType.NOTIFICATION,
                            new Gson().toJson("Black player, " + username + ", resigned." ));
                    connections.broadcast("",notification, gameID);

                }else{
                    Notification errorMessage = new Notification(ServerMessageType.ERROR,
                            "Error: Invalid command");
                    session.getRemote().sendString(new Gson().toJson(errorMessage));
                }
            }
        }
    }

    private void isResign(Session session, String username, UserGameCommand command, Move move){
        try{
            if (connections.getResign(command.getGameID())){
                Notification errorMessage = new Notification(ServerMessageType.ERROR,
                        "Error: Resigned. Cannot make a move");
                session.getRemote().sendString(new Gson().toJson(errorMessage));
            }else{
                makeMove(session, username, command, move);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void makeMove(Session session, String username, UserGameCommand command, Move move) throws IOException {
        try {
            Integer gameID = command.getGameID();
            if (!gameIDs.contains(gameID)) {
                Notification errorMessage = new Notification(ServerMessageType.ERROR,
                        "Error: Wrong GameID");
                session.getRemote().sendString(new Gson().toJson(errorMessage));
            } else {
                Collection<GameData> games = dataAccessGame.getGames();
                for (GameData game: games){
                    validationCheck1(session, username, command, move, game, gameID);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidMoveException ex) {
            Notification errorMessage = new Notification(ServerMessageType.ERROR, "Error: Invalid move");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void validationCheck1(Session session, String username, UserGameCommand command, Move move, GameData game,
                                  Integer gameID) throws IOException, InvalidMoveException, DataAccessException {
        if(game.gameID() == gameID){
            if (game.game()==null){
                Notification errorMessage = new Notification(ServerMessageType.ERROR,
                        "Error: Invalid command");
                session.getRemote().sendString(new Gson().toJson(errorMessage));
            }else {
                // move validation check
                moveValidationCheck(session, username, command, move, game, gameID);
            }
        }
    }

    private void moveValidationCheck(Session session, String username, UserGameCommand command, Move move,GameData game,
                                     Integer gameID) throws InvalidMoveException, DataAccessException, IOException {
        if (game.game().validMoves(move.getMove().getStartPosition()).contains(move.getMove())){

            ChessGame.TeamColor userColor = getUserColor(username, game);

            if (game.game().getTeamTurn() == userColor){
                ChessGame updatedGame = game.game();
                updatedGame.makeMove(move.getMove());

                GameData tempGame = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername()
                        , game.gameName(),updatedGame);

                // update the game
                dataAccessGame.updateMove(tempGame);

                LoadGame loadGame = new LoadGame(ServerMessageType.LOAD_GAME, command.getGameID(), updatedGame, userColor);
                connections.loadGames(loadGame, gameID);

                Notification notification = new Notification(ServerMessageType.NOTIFICATION,
                        new Gson().toJson(username + " made a move." ));
                connections.broadcast(username,notification, gameID);


            }else{
                Notification errorMessage = new Notification(ServerMessageType.ERROR,
                        "Error: Invalid command. " +
                        "\n" + "You are not the right user to take this turn");
                session.getRemote().sendString(new Gson().toJson(errorMessage));
            }

        }else{
            Notification errorMessage = new Notification(ServerMessageType.ERROR, "Error: Invalid move");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
        }
    }

    private static ChessGame.TeamColor getUserColor(String username, GameData game) {
        ChessGame.TeamColor userColor = null;
        if (username.equals(game.whiteUsername())){
            userColor = ChessGame.TeamColor.WHITE;
        }else if (username.equals(game.blackUsername())){
            userColor = ChessGame.TeamColor.BLACK;
        }
        return userColor;
    }

    private void connectGame(Session session, String username, UserGameCommand command) {
        // LOAD_GAME, NOTIFICATION
        try {
            Integer gameID = command.getGameID();
            GameData gameData = null;
            connections.addResign(command.getGameID(),false);
            for (GameData game :this.dataAccessGame.getGames()){
                gameIDs.add(game.gameID());
                if (game.gameID()==gameID){
                    gameData=game;
                }
            }
            ChessGame.TeamColor userColor = ChessGame.TeamColor.WHITE;
            if (!gameData.equals(null)){
                userColor = getUserColor(username, gameData);
            }

            if (!gameIDs.contains(command.getGameID())) {
                Notification errorMessage = new Notification(ServerMessageType.ERROR,
                        "Error: Wrong GameID");
                session.getRemote().sendString(new Gson().toJson(errorMessage));
            }else{
                LoadGame loadGame = new LoadGame(ServerMessageType.LOAD_GAME,command.getGameID(),gameData.game(),userColor);
                session.getRemote().sendString(new Gson().toJson(loadGame));

                Notification notification = new Notification(ServerMessageType.NOTIFICATION,
                        new Gson().toJson(username + " had been connected"));
                connections.broadcast(username, notification, command.getGameID());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void leaveGame(Session session, String username, UserGameCommand command){
        // LOAD_GAME
        try {
            for (GameData game :this.dataAccessGame.getGames()){
                gameIDs.add(game.gameID());
            }

            if (!gameIDs.contains(command.getGameID())) {
                Notification errorMessage = new Notification(ServerMessageType.ERROR,
                        "Error: Wrong GameID");
                session.getRemote().sendString(new Gson().toJson(errorMessage));
            }else{

                Notification notification = new Notification(ServerMessageType.NOTIFICATION,
                        new Gson().toJson(username + " left the game."));
                connections.broadcast(username,notification, command.getGameID());
                connections.remove(username);

                // remove the player from the game by updating the game
                for (GameData game : dataAccessGame.getGames()){
                    removeCheck(username, command, game);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeCheck(String username, UserGameCommand command, GameData game) throws DataAccessException {
        if (game.gameID() == command.getGameID()){
            if (game.whiteUsername().equals(username)){
                String color = "white";
                JoinGame joinGame = new JoinGame(color, command.getGameID());
                dataAccessGame.updateGame(joinGame, null);

            }else if (game.blackUsername().equals(username)){
                String color = "black";
                JoinGame joinGame = new JoinGame(color, command.getGameID());
                dataAccessGame.updateGame(joinGame, null);
            }else{
                throw new DataAccessException("Error: unknown username");
            }
        }
    }



}
