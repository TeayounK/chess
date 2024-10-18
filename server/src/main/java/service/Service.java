package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class Service {

    private final DataAccess dataAccess;

    public Service(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public AuthData addUser(UserData newUser) throws DataAccessException {
        UserData existingUser = dataAccess.getUser(newUser.username());
        if (existingUser!=null){
            throw new DataAccessException("Error: already taken");
        }
        dataAccess.addUser(newUser);
        AuthData authData = new AuthData(UUID.randomUUID().toString(), newUser.username());
        dataAccess.addAuth(authData);


        return authData;
    }

//    public UserData addUser(UserData newUser){
//
//    }
}
