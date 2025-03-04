package dataaccess;

import java.util.HashMap;

public class MemoryUserDAO implements DataAccessUser {
    private HashMap<String, UserData> users = new HashMap<>();


    @Override
    public void addUser(UserData user) throws DataAccessException {
        if (user.username() == null){
            throw new DataAccessException("Error: Not a valid ID");
        }else {
            users.put(user.username(), user);
        }
    }

    @Override
    public UserData getUser(String username){
        return users.get(username);
    }

    public void clearAll() throws DataAccessException{
        users = new HashMap<>();
    }
    public boolean verifyUser(String username, String password){
        return false;
    }

}