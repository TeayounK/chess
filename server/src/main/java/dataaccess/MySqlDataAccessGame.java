package dataaccess;

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
}