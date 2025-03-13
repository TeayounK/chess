import dataaccess.DataAccessException;
import dataaccess.DataAccessUser;
import dataaccess.DatabaseManager;

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
}