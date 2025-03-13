package dataaccess;

import model.GameData;
import model.JoinGame;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class MySqlDataAccessGame implements DataAccessGame {
    private int maxID = 0;

    public MySqlDataAccessGame() throws DataAccessException {
        String[] createStatements = {
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
        DatabaseManager.configureDatabase(createStatements);
    }
    @Override
    public GameData createGame(GameData gameData) throws DataAccessException {
        maxID += 1;
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) " +
                    "VALUES (?,?,?,?,?)";
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

}