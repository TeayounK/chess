package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Objects;

public class MemoryAuthDAO implements DataAccessAuth {
    final private HashMap<String, AuthData> auths = new HashMap<>();

    @Override
    public void addAuth(AuthData authData) throws DataAccessException {
        auths.put(authData.username(),authData);
    }

    @Override
    public AuthData getAuth(String username) throws DataAccessException {
        return auths.get(username);
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
}
