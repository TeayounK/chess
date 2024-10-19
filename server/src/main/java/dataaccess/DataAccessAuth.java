package dataaccess;

import model.AuthData;

public interface DataAccessAuth {

    void addAuth(AuthData authData) throws DataAccessException;

    AuthData getAuth(String username) throws DataAccessException;

}
