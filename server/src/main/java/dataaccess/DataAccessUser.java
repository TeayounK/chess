package dataaccess;

import model.UserData;

public interface DataAccessUser {
    void addUser(UserData user) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void clearAll();
}
