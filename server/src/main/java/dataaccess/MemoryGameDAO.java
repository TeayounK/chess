package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements DataAccessGame{
    private HashMap<Integer, GameData> games = new HashMap<>();
    int maxID = 0;


    @Override
    public GameData createGame(GameData gameData) throws DataAccessException {
        if (gameData.gameName() == null){
            throw new DataAccessException("Error: Not a valid Game name");
        }else {
            maxID += 1;
            GameData newGame = new GameData(maxID, null,null,gameData.gameName(),null);
            games.put(maxID, newGame);
            return newGame;
        }
    }

    public Collection<GameData> getGames(){
        return games.values();
    }

    @Override
    public void clearAll() {
        games = new HashMap<>();
        maxID = 0;
    }

}
