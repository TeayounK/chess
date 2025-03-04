package service;

import dataaccess.DataAccessException;
import model.AuthData;

import java.util.UUID;

public class Service {

    private final DataAccessUser dataAccessUser;
    private final DataAccessAuth dataAccessAuth;
    private final DataAccessGame dataAccessGame;

    public Service(DataAccessUser dataAccess, DataAccessAuth dataAccessAuth, DataAccessGame dataAccessGame) {
        this.dataAccessUser = dataAccess;
        this.dataAccessAuth = dataAccessAuth;
        this.dataAccessGame = dataAccessGame;
    }
    // Get Auth data from User data for registration.
    public AuthData addUser(UserData newUser) throws DataAccessException {
        if (newUser.username() == null||newUser.password() == null||newUser.email() == null){
            throw new DataAccessException("Error: bad request");
        }
        UserData existingUser = dataAccessUser.getUser(newUser.username());
        if (existingUser!=null){
            throw new DataAccessException("Error: already taken");
        }
        dataAccessUser.addUser(newUser);
        AuthData authData = new AuthData(UUID.randomUUID().toString(), newUser.username());
        dataAccessAuth.addAuth(authData);
        return authData;
    }

    // get Auth data for login in.
    public AuthData loginUser(UserData user) throws DataAccessException{
        UserData existingUser = dataAccessUser.getUser(user.username());
        if (existingUser == null){
            throw new DataAccessException("Error: Unknown username");
        }
        if (!dataAccessUser.verifyUser(user.username(),user.password())){
            throw new DataAccessException("Error: Unauthorized");
        }
        AuthData authData = new AuthData(UUID.randomUUID().toString(), user.username());
        dataAccessAuth.addAuth(authData);
        return authData;
    }
}