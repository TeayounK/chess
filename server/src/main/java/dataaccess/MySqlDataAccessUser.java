package dataaccess;

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
}