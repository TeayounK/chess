package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class MySqlDataAccessAuth implements DataAccessAuth{

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  authsKey (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            )
            """
    };

    public MySqlDataAccessAuth() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }

    @Override
    public void addAuth(AuthData authData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO authsKey (authToken, username) VALUES (?, ?)";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1,authData.authToken());
                preparedStatement.setString(2,authData.username());
                preparedStatement.executeUpdate();
            }

        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to add authData: %s", ex.getMessage()));
        }
    }

    @Override
    public AuthData getAuth(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM authsKey WHERE username=?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1,username);
                try(var result = preparedStatement.executeQuery()){
                    if (result.next()){
                        var authToken = result.getString("authToken");
                        return new AuthData(authToken,username);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to get the authData for user : %s", ex.getMessage()));
        }
        return null;
    }

    @Override
    public String getUser(String authToken)throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM authsKey WHERE authToken=?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1,authToken);
                try(var result = preparedStatement.executeQuery()){
                    if (result.next()){
                        return result.getString("username");
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to get the username with authData : %s", ex.getMessage()));
        }
        return null;
    }

    @Override
    public void removeAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM authsKey WHERE authToken = ?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                int updateRow = preparedStatement.executeUpdate();
                if (updateRow == 0){
                    throw new DataAccessException("Error: Already logged-out username");
                }
            }
        }catch (SQLException ex) {
            throw new DataAccessException("Error: Already logged-out username");
        }
    }
    @Override
    public boolean checkAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM authsKey WHERE authToken = ?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                try(var result = preparedStatement.executeQuery()){
                    if (result.next()){
                        return true;
                    }
                }
            }
        }catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to get the authToken : %s", ex.getMessage()));
        }
        throw new DataAccessException("Error: Unauthorized");
    }

    @Override
    public void clearAll() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE authsKey";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to clean up the database: %s", ex.getMessage()));
        }
    }
}
