package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Objects;

public class MemoryAuthDAO {
    private HashMap<String, AuthData> authsKey = new HashMap<>();

    @Override
    public void addAuth(AuthData authData) {
        authsKey.put(authData.authToken(),authData);
    }

    @Override
    public AuthData getAuth(String username){
        for (AuthData authData:authsKey.values()){
            if (Objects.equals(authData.username(), username)){
                return authData;
            }
        }
        return null;
    }
}
