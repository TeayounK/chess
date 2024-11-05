package dataaccess;

import com.google.gson.Gson;
import model.GameData;
import model.JoinGame;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class MySqlDataAccessGame implements DataAccessGame{
    private int maxID = 0;

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  games (
              `gameID` INT NOT NULL,
              `whiteUsername` varchar(256) NULL,
              `blackUsername` varchar(256) NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT NULL,
              PRIMARY KEY (`gameID`)
            )
            """
    };

    public MySqlDataAccessGame() throws DataAccessException {
        configureDatabaseGame();
    }

    @Override
    public GameData createGame(GameData gameData) throws DataAccessException {
        maxID += 1;
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?,?,?,?,?)";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1,maxID);
                preparedStatement.setString(2,null);
                preparedStatement.setString(3,null);
                preparedStatement.setString(4,gameData.gameName());
                preparedStatement.setString(5,null); //new Gson().toJson(gameData.game())
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new DataAccessException("Error: Not a valid Game name");
        }
        return new GameData(maxID, null,null,gameData.gameName(),null);
    }

    @Override
    public Collection<GameData> getGames() {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM games";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                try(var rs = preparedStatement.executeQuery()){
                    while (rs.next()){
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (SQLException | DataAccessException ex) {
            System.out.println("Unable to list games :" + ex.getMessage());
        }
        return result;
    }

    @Override
    public void clearAll() {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE games";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException ex) {
            System.out.println("Unable to list games :" + ex.getMessage());
        }
    }

    @Override
    public void updateGame(JoinGame game, String username) throws DataAccessException {
        String statement1 = "";

        if (game.playerColor() == null){
            throw new DataAccessException("Error: bad request");
        }
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM games WHERE gameID = ?";

            if (game.playerColor().equalsIgnoreCase("black")){
                statement1 = "UPDATE games SET blackUsername = ? WHERE gameID = ?";
            }else if (game.playerColor().equalsIgnoreCase("white")){
                statement1 = "UPDATE games SET whiteUsername = ? WHERE gameID = ?";
            }else{
                throw new DataAccessException("Error: bad request");
            }
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, game.gameID());
                try(var result = preparedStatement.executeQuery()){
                    if (result.next()){
                        GameData gameData = readGame(result);
                        if (gameData.blackUsername() != null && gameData.whiteUsername() != null) {
                            throw new DataAccessException("Error: already taken");
                        } else if ((gameData.blackUsername() != null && game.playerColor().equalsIgnoreCase("black")) || (gameData.whiteUsername() != null && game.playerColor().equalsIgnoreCase("white"))) {
                            throw new DataAccessException("Error: already taken");
                        }else{
                            try(var preparedStatement1 = conn.prepareStatement(statement1)){
                                preparedStatement1.setString(1, username);
                                preparedStatement1.setInt(2, game.gameID());
                                preparedStatement1.executeUpdate();
                            }
                        }
                    }else{
                        throw new DataAccessException("Error: bad request");
                    }
                }
            }
        }catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to get the authToken : %s", ex.getMessage()));
        }
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var gameID = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var game = rs.getString("game");
        if (game==null){
            return new GameData(gameID,whiteUsername,blackUsername,gameName,null);
        }else{
            GameData gameData = new Gson().fromJson(game, GameData.class);
            return new GameData(gameID,whiteUsername,blackUsername,gameName,gameData.game());
        }
    }


    private void configureDatabaseGame() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
