import dataaccess.DataAccessException;
import dataaccess.DataAccessUser;
import dataaccess.DatabaseManager;
import org.mindrot.jbcrypt.BCrypt;

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
}