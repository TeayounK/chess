package dataaccess;

import model.AuthData;

public interface DataAccessAuth {

    void addAuth(AuthData authData) throws DataAccessException;

    AuthData getAuth(String username) throws DataAccessException;

    public String getUser(String authToken) throws DataAccessException ;

    void removeAuth(String authToken) throws DataAccessException;

    boolean checkAuth(String authToken) throws DataAccessException;

    void clearAll()throws DataAccessException;

}