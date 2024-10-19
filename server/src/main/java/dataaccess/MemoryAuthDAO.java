package dataaccess;

import model.AuthData;

import java.util.HashMap;

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
}
