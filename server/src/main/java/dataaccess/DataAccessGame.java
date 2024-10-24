package dataaccess;

import model.GameData;
import model.JoinGame;

import java.util.Collection;

public interface DataAccessGame {
    GameData createGame(GameData gameData) throws DataAccessException;

    Collection<GameData> getGames();

    void clearAll();

    void updateGame(JoinGame game, String username)throws DataAccessException;
}
