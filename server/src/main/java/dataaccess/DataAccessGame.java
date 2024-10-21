package dataaccess;

import model.GameData;

import java.util.Collection;

public interface DataAccessGame {
    public GameData CreateGame(GameData gameData) throws DataAccessException;

    public Collection<GameData> getGames();

    void clearAll();

}
