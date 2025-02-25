package service;

public class Service {

    private final DataAccessUser dataAccessUser;
    private final DataAccessAuth dataAccessAuth;
    private final DataAccessGame dataAccessGame;

    public Service(DataAccessUser dataAccess, DataAccessAuth dataAccessAuth, DataAccessGame dataAccessGame) {
        this.dataAccessUser = dataAccess;
        this.dataAccessAuth = dataAccessAuth;
        this.dataAccessGame = dataAccessGame;
    }
}