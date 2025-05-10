package websocket.messages;

import chess.ChessGame;

public class LoadGame extends ServerMessage {
    Integer game;
    ChessGame chessGame;
    ChessGame.TeamColor userColor;

    public LoadGame(ServerMessageType type, Integer gameID, ChessGame chessGame, ChessGame.TeamColor userColor) {
        super(type);
        this.game = gameID;
        this.chessGame = chessGame;
        this.userColor = userColor;
    }

    public ChessGame getChessGame() {
        return chessGame;
    }
    public ChessGame.TeamColor getUserColor(){
        return userColor;
    }
    public Integer getGameID(){
        return game;
    }
}
