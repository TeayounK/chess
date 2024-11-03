package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements DataAccessAuth {
    private HashMap<String, AuthData> auths = new HashMap<>();
    private HashMap<String, AuthData> authsKey = new HashMap<>();

    @Override
    public void addAuth(AuthData authData) {
        auths.put(authData.username(),authData);
        authsKey.put(authData.authToken(),authData);
    }

    @Override
    public AuthData getAuth(String username){
        return auths.get(username);
    }
    @Override
    public String getUser(String authToken){
        return authsKey.get(authToken).username();
    }

    @Override
    public void removeAuth(String authToken) throws DataAccessException {
        if (authsKey.get(authToken)==null){
            throw new DataAccessException("Error: Already logged-out username");
        }
        String theUser = authsKey.get(authToken).username();
        if (theUser == null) throw new DataAccessException("Error: Already logged-out username");
        else {
            auths.remove(theUser);
            authsKey.remove(authToken);
        }

    }

    @Override
    public boolean checkAuth(String authToken) throws DataAccessException {
        if(authsKey.get(authToken)==null){
            throw new DataAccessException("Error: Unauthorized");
        }
        return true;
    }

    public void clearAll(){
        auths = new HashMap<>();
        authsKey = new HashMap<>();
    }

}








