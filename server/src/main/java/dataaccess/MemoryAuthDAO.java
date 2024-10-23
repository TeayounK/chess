package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Objects;

public class MemoryAuthDAO implements DataAccessAuth {
    private HashMap<String, AuthData> auths = new HashMap<>();

    @Override
    public void addAuth(AuthData authData) {
        auths.put(authData.username(),authData);
    }

    @Override
    public AuthData getAuth(String username){
        return auths.get(username);
    }
    @Override
    public String getUser(String authToken){
        for (String username: auths.keySet()) {
            if (Objects.equals(authToken, auths.get(username).authToken())) {
                return username;
            }
        }
        return null;
    }

    @Override
    public void removeAuth(String authToken) throws DataAccessException {
        boolean isin = false;
        String TheUser = "";
        for (String username: auths.keySet()){
            if (Objects.equals(authToken, auths.get(username).authToken())){
                isin = true;
                TheUser = username;
            }
        }
        if (!isin) throw new DataAccessException("Error: Already logged-out username");
        else {
            auths.remove(TheUser);
        }

    }

    @Override
    public boolean checkAuth(String authToken) throws DataAccessException {
        for (String username: auths.keySet()){
            if (Objects.equals(authToken, auths.get(username).authToken())){
                return true;
            }
        }
        throw new DataAccessException("Error: Unauthorized");
    }

    public void clearAll(){
        auths = new HashMap<>();
    }

}








