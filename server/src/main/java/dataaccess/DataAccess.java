package dataaccess;

import model.AuthData;
import model.UserData;

public interface DataAccess {
    void addUser(UserData user) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void addAuth(AuthData authData) throws DataAccessException;
}
