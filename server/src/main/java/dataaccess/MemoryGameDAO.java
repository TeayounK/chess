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

    public void updateGame(JoinGame game, String username) throws DataAccessException {
        GameData gameCalled = games.get(game.gameID());
        if (game.playerColor() == null){
            throw new DataAccessException("Error: bad request");
        }
        if (gameCalled == null){
            throw new DataAccessException("Error: bad request");
        } else if (!game.playerColor().equalsIgnoreCase("black")&&!game.playerColor().equalsIgnoreCase("white")) {
            throw new DataAccessException("Error: not a valid color");
        } else if (gameCalled.blackUsername() != null && gameCalled.whiteUsername() != null) {
            throw new DataAccessException("Error: already taken");
        } else if ((gameCalled.blackUsername() != null && game.playerColor().equalsIgnoreCase("black"))
                || (gameCalled.whiteUsername() != null && game.playerColor().equalsIgnoreCase("white"))) {
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
        }
    }

}
