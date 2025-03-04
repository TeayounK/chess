package service;

import dataaccess.DataAccessAuth;
import dataaccess.DataAccessException;
import dataaccess.DataAccessGame;
import dataaccess.DataAccessUser;
import model.AuthData;
import model.GameData;
import model.JoinGame;
import model.UserData;

import java.util.Collection;
import java.util.UUID;

public class Service {

    private final DataAccessUser dataAccessUser;
    private final DataAccessAuth dataAccessAuth;
    private final DataAccessGame dataAccessGame;

    public Service(DataAccessUser dataAccess, DataAccessAuth dataAccessAuth, DataAccessGame dataAccessGame) {
        this.dataAccessUser = dataAccess;
        this.dataAccessAuth = dataAccessAuth;
        this.dataAccessGame = dataAccessGame;
    }
    // Get Auth data from User data for registration.
    public AuthData addUser(UserData newUser) throws DataAccessException {
        if (newUser.username() == null||newUser.password() == null||newUser.email() == null){
            throw new DataAccessException("Error: bad request");
        }
        UserData existingUser = dataAccessUser.getUser(newUser.username());
        if (existingUser!=null){
            throw new DataAccessException("Error: already taken");
        }
        dataAccessUser.addUser(newUser);
        AuthData authData = new AuthData(UUID.randomUUID().toString(), newUser.username());
        dataAccessAuth.addAuth(authData);
        return authData;
    }

    // get Auth data for login in.
    public AuthData loginUser(UserData user) throws DataAccessException{
        UserData existingUser = dataAccessUser.getUser(user.username());
        if (existingUser == null){
            throw new DataAccessException("Error: Unknown username");
        }
        if (!dataAccessUser.verifyUser(user.username(),user.password())){
            throw new DataAccessException("Error: Unauthorized");
        }
        AuthData authData = new AuthData(UUID.randomUUID().toString(), user.username());
        dataAccessAuth.addAuth(authData);
        return authData;
    }

    // Logout
    public void logoutUser(String authToken) throws DataAccessException{
        dataAccessAuth.removeAuth(authToken);
    }


    // Create Game
    public GameData createGame(GameData gamename) throws DataAccessException{
        return dataAccessGame.createGame(gamename);

    }
    public void checkAuth(String authToken) throws DataAccessException{
        dataAccessAuth.checkAuth(authToken);
    }

    public Collection<GameData> listGames(){
        return dataAccessGame.getGames();
    }

    public void clearDataBase() throws DataAccessException{
        dataAccessAuth.clearAll();
        dataAccessGame.clearAll();
        dataAccessUser.clearAll();
    }

    public void joinGame(JoinGame gameData, String authToken) throws DataAccessException{
        String username = dataAccessAuth.getUser(authToken);
        dataAccessGame.updateGame(gameData, username);

    }
}