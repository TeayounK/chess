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
}