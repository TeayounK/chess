package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryDAO implements DataAccess {
    final private HashMap<String, UserData> users = new HashMap<>();


    @Override
    public void addUser(UserData user) throws DataAccessException {
        if (user.username() == null){
            throw new DataAccessException("Error: Not a valid ID");
        }else {
            users.put(user.username(), user);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return users.get(username);
    }

    @Override
    public void addAuth(AuthData authData) throws DataAccessException {

    }
}
