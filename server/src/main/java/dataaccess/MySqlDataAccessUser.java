package dataaccess;

import com.google.gson.Gson;
import model.UserData;

import java.sql.SQLException;

public class MySqlDataAccessUser implements DataAccessUser{

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  users (
              `username` String NOT NULL,
              `password` String NOT NULL,
              `email` String NOT NULL,
              PRIMARY KEY (`username`)
            )
            """
    };

    public MySqlDataAccessUser() throws DataAccessException {
        configureDatabaseUser();
    }

    @Override
    public void addUser(UserData user) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO pet (username, password, email) VALUES (?, ?, ?)";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1,user.username());
                preparedStatement.setString(2,user.password());
                preparedStatement.setString(3,user.email());
                preparedStatement.executeUpdate();
            }

        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clearAll() {

    }

    private void configureDatabaseUser() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
