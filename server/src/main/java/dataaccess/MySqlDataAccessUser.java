package dataaccess;

import model.UserData;

public class MySqlDataAccessUser implements DataAccessUser{
    @Override
    public void addUser(UserData user) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clearAll() {

    }
}