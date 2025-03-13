package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class MySqlDataAccessUser implements DataAccessUser {

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  users (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            )
            """
    };

    public MySqlDataAccessUser() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }

    private String storeUserPassword(String clearTextPassword) {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }

    public boolean verifyUser(String username, String providedClearTextPassword){
        // read the previously hashed password from the database
        var hashedPassword = readHashedPasswordFromDatabase(username);

        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
    }

    private String readHashedPasswordFromDatabase(String username) {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM users WHERE username=?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                try (var result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        return result.getString("password");
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void addUser(UserData user) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1,user.username());
                preparedStatement.setString(2,storeUserPassword(user.password()));
                preparedStatement.setString(3,user.email());
                preparedStatement.executeUpdate();
            }

        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable register user: %s", ex.getMessage()));
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM users WHERE username=?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1,username);
                try(var result = preparedStatement.executeQuery()){
                    if (result.next()){
                        var password = result.getString("password");
                        var email = result.getString("email");
                        return new UserData(username,password,email);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to get the user data: %s", ex.getMessage()));
        }
        return null;
    }

    @Override
    public void clearAll() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE users";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to clean up the database: %s", ex.getMessage()));
        }
    }
}