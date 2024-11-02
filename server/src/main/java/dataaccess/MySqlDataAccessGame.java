package dataaccess;

import model.GameData;
import model.JoinGame;

import java.util.Collection;
import java.util.List;

public class MySqlDataAccessGame implements DataAccessGame{
    @Override
    public GameData createGame(GameData gameData) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> getGames() {
        return List.of();
    }

    @Override
    public void clearAll() {

    }

    @Override
    public void updateGame(JoinGame game, String username) throws DataAccessException {

    }
}
