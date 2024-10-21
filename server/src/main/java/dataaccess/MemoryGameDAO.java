package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements DataAccessGame{
    private HashMap<Integer, GameData> games = new HashMap<>();
    int max_id = -1;


    @Override
    public GameData CreateGame(GameData gameData) throws DataAccessException {
        if (gameData.gameName() == null){
            throw new DataAccessException("Error: Not a valid Game name");
        }else {
            max_id += 1;
            GameData newGame = new GameData(max_id, "","",gameData.gameName(),null);
            games.put(max_id, newGame);
            return newGame;
        }
    }

    public Collection<GameData> getGames(){
        return games.values();
    }

    @Override
    public void clearAll() {
        games = new HashMap<>();
        max_id = -1;
    }
}
