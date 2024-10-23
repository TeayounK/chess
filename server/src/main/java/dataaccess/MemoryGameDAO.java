package dataaccess;

import model.GameData;
import model.JoinGame;

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

    public GameData getaGame(JoinGame game, String username) throws DataAccessException {
        GameData gameCalled = games.get(game.gameID());
        if (gameCalled == null||(!game.playerColor().equalsIgnoreCase("black")&&!game.playerColor().equalsIgnoreCase("white"))) {
            throw new DataAccessException("Error: bad request");
        } else if (gameCalled.blackUsername() != null && gameCalled.whiteUsername() != null) {
            throw new DataAccessException("Error: already taken");
        } else if ((gameCalled.blackUsername() != null && game.playerColor().equalsIgnoreCase("black")) || (gameCalled.whiteUsername() != null && game.playerColor().equalsIgnoreCase("white"))) {
            throw new DataAccessException("Error: already taken");
        } else {
            games.remove(gameCalled.gameID(),gameCalled);
            if (game.playerColor().equalsIgnoreCase("white")){
                GameData newGame = new GameData(gameCalled.gameID(),username,gameCalled.blackUsername(),gameCalled.gameName(),gameCalled.game());
                games.put(gameCalled.gameID(),newGame);
            }else{
                GameData newGame = new GameData(gameCalled.gameID(),gameCalled.whiteUsername(),username,gameCalled.gameName(),gameCalled.game());
                games.put(gameCalled.gameID(),newGame);
            }
            return games.get(game.gameID());
        }
    }
}
