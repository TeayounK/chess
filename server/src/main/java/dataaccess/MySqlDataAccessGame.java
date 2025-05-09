package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.JoinGame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        ChessGame chessGame = new ChessGame();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) " +
                    "VALUES (?,?,?,?,?)";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1,maxID);
                preparedStatement.setString(2,null);
                preparedStatement.setString(3,null);
                preparedStatement.setString(4,gameData.gameName());
                preparedStatement.setString(5,new Gson().toJson(chessGame));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new DataAccessException("Error: Not a valid Game name");
        }
        return new GameData(maxID, null,null,gameData.gameName(),chessGame);
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
        String statement1;

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
                throw new DataAccessException("Error: not a valid color");
            }
            try (var preparedStatement = conn.prepareStatement(statement)) {
                updateHelper(game, username, preparedStatement, conn, statement1);
            }
        }catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to get the authToken : %s",
                    ex.getMessage()));
        }
    }
    private void updateHelper(JoinGame game, String username, PreparedStatement preparedStatement, Connection conn,
                              String statement1) throws SQLException, DataAccessException {
        preparedStatement.setInt(1, game.gameID());
        try(var result = preparedStatement.executeQuery()){
            if (!result.next()) {
                throw new DataAccessException("Error: bad request");
            } else {
                GameData gameData = readGame(result);
                if (gameData.blackUsername() != null && gameData.whiteUsername() != null) {
                    throw new DataAccessException("Error: already taken");
                } else if ((gameData.blackUsername() != null &&
                        game.playerColor().equalsIgnoreCase("black"))
                        || (gameData.whiteUsername() != null &&
                        game.playerColor().equalsIgnoreCase("white"))) {
                    throw new DataAccessException("Error: already taken");
                }else{
                    try(var preparedStatement1 = conn.prepareStatement(statement1)){
                        preparedStatement1.setString(1, username);
                        preparedStatement1.setInt(2, game.gameID());
                        preparedStatement1.executeUpdate();
                    }
                }
            }
        }
    }

    @Override
    public void updateMove(GameData game) throws DataAccessException {
//        System.out.println("MySQL");
        //TODO: it doesn't work... Don't know why
        String statement1;
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM games WHERE gameID = ?";
            statement1 = "UPDATE games SET game = ? WHERE gameID = ?";

            try (var preparedStatement = conn.prepareStatement(statement)) {
                updateMoveHelper(game, preparedStatement, conn, statement1);
            }
        }catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to get the authToken : %s",
                    ex.getMessage()));
        }
    }
    private void updateMoveHelper(GameData game, PreparedStatement preparedStatement, Connection conn,
                              String statement1) throws SQLException, DataAccessException {
        preparedStatement.setInt(1, game.gameID());
        try(var result = preparedStatement.executeQuery()){
            if (!result.next()) {
                throw new DataAccessException("Error: bad request");
            } else {
                try(var preparedStatement1 = conn.prepareStatement(statement1)){
                    preparedStatement1.setString(1, new Gson().toJson(game.game()));
                    preparedStatement1.setInt(2, game.gameID());
                    preparedStatement1.executeUpdate();
                }
            }
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
//            GameData gameData = new Gson().fromJson(game, GameData.class);
            ChessGame chessGame = new Gson().fromJson(game, ChessGame.class);
            return new GameData(gameID,whiteUsername,blackUsername,gameName,chessGame);
        }
    }

}