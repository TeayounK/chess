package dataaccess;

import model.GameData;
import model.JoinGame;

import java.util.Collection;

public interface DataAccessGame {
    public GameData CreateGame(GameData gameData) throws DataAccessException;

    public Collection<GameData> getGames();

    void clearAll();

    public GameData getaGame(JoinGame game, String username)throws DataAccessException;
}
