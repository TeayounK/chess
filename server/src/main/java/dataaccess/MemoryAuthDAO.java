package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Objects;

public class MemoryAuthDAO implements DataAccessAuth {
    private HashMap<String, AuthData> auths = new HashMap<>();
    private HashMap<String, AuthData> auths_key = new HashMap<>();

    @Override
    public void addAuth(AuthData authData) {
        auths.put(authData.username(),authData);
        auths_key.put(authData.authToken(),authData);
    }

    @Override
    public AuthData getAuth(String username){
        return auths.get(username);
    }
    @Override
    public String getUser(String authToken){
        return auths_key.get(authToken).username();
    }

    @Override
    public void removeAuth(String authToken) throws DataAccessException {
        if (auths_key.get(authToken)==null){
            throw new DataAccessException("Error: Already logged-out username");
        }
        String TheUser = auths_key.get(authToken).username();
        if (TheUser == null) throw new DataAccessException("Error: Already logged-out username");
        else {
            auths.remove(TheUser);
            auths_key.remove(authToken);
        }

    }

    @Override
    public boolean checkAuth(String authToken) throws DataAccessException {
        if(auths_key.get(authToken)==null){
            throw new DataAccessException("Error: Unauthorized");
        }
        return true;
    }

    public void clearAll(){
        auths = new HashMap<>();
        auths_key = new HashMap<>();
    }

}








