package dataaccess;

import model.AuthData;

public class MySqlDataAccessAuth implements DataAccessAuth{
    @Override
    public void addAuth(AuthData authData) throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String username) throws DataAccessException {
        return null;
    }

    @Override
    public String getUser(String authToken) {
        return "";
    }

    @Override
    public void removeAuth(String authToken) throws DataAccessException {

    }

    @Override
    public boolean checkAuth(String authToken) throws DataAccessException {
        return false;
    }

    @Override
    public void clearAll() {

    }
}
