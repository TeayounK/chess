package service;

import dataaccess.DataAccessAuth;
import dataaccess.DataAccessGame;
import dataaccess.DataAccessUser;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.JoinGame;
import model.UserData;

import javax.xml.crypto.Data;
import java.util.*;

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
        AuthData authExistUser = dataAccessAuth.getAuth(user.username());

        if (authExistUser != null){
            throw new DataAccessException("Error: Already logged-in username");
        }
        if (!Objects.equals(user.password(), existingUser.password())){
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
        GameData newGame = dataAccessGame.CreateGame(gamename);
        return newGame;


    }

    public boolean checkAuth(String authToken) throws DataAccessException{
        return dataAccessAuth.checkAuth(authToken);
    }

    public Collection<GameData> listGames(){
        return dataAccessGame.getGames();
    }

    public void clearDataBase() throws DataAccessException {
        dataAccessAuth.clearAll();
        dataAccessGame.clearAll();
        dataAccessUser.clearAll();
    }

    public void joinGame(JoinGame gameData, String authToken) throws DataAccessException{
        String username = dataAccessAuth.getUser(authToken);
        GameData gameCalled = dataAccessGame.getaGame(gameData, username);


    }

}
