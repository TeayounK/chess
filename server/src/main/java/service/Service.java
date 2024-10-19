package service;

import dataaccess.DataAccessAuth;
import dataaccess.DataAccessUser;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.util.Objects;
import java.util.UUID;

public class Service {

    private final DataAccessUser dataAccessUser;
    private final DataAccessAuth dataAccessAuth;

    public Service(DataAccessUser dataAccess, DataAccessAuth dataAccessAuth) {
        this.dataAccessUser = dataAccess;
        this.dataAccessAuth = dataAccessAuth;
    }
    // Get Auth data from User data for registration.
    public AuthData addUser(UserData newUser) throws DataAccessException {
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
        AuthData authExistUser = dataAccessAuth.getAuth(user.username());
        if (authExistUser != null){
            throw new DataAccessException("Error: Already logged-in username");
        }
        if (!Objects.equals(user.password(), existingUser.password())){
            throw new DataAccessException("Error: Unauthorized");
        }
        AuthData authData = new AuthData(UUID.randomUUID().toString(), user.username());
        dataAccessAuth.addAuth(authData);
        return authData;
    }

}
