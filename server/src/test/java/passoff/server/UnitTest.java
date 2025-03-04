package passoff.server;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.JoinGame;
import model.UserData;
import org.junit.jupiter.api.*;
import service.Service;

public class UnitTest {

    @Test
    public void addUserPositiveTest() {
        Service service = new Service(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());
        UserData newUser = new UserData("username","password","email");
        try{
            service.addUser(newUser);
        }catch(DataAccessException e){
            Assertions.assertNotNull(e,"addUser is not working");
        }
    }

    @Test
    public void addUserBadRequestTest() {
        Service service = new Service(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());
        UserData newUser = new UserData(null,"password","email");
        try{
            service.addUser(newUser);
        }catch(DataAccessException e){
            Assertions.assertEquals(e.getMessage(),"Error: bad request","addUser is not working");
        }
        UserData newUser1 = new UserData("username",null,"email");
        try{
            service.addUser(newUser1);
        }catch(DataAccessException e){
            Assertions.assertEquals(e.getMessage(),"Error: bad request","addUser is not working");
        }
        UserData newUser2 = new UserData("username","password",null);
        try{
            service.addUser(newUser2);
        }catch(DataAccessException e){
            Assertions.assertEquals(e.getMessage(),"Error: bad request","addUser is not working");
        }
    }

    @Test
    public void addUserAlreadyTakenTest() {
        Service service = new Service(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());
        UserData newUser = new UserData("username","password","email");
        try{
            service.addUser(newUser);
            service.addUser(newUser);
        }catch(DataAccessException e){
            Assertions.assertEquals(e.getMessage(),"Error: already taken","addUser is not working");
        }
    }



    @Test
    public void loginUserPositiveTest(){
        Service service = new Service(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());
        UserData newUser = new UserData("username","password","email");
        try{
            service.addUser(newUser);
            service.loginUser(newUser);
        }catch(DataAccessException e){
            Assertions.assertNotNull(e,"login is not working");
        }
    }
    @Test
    public void loginUserUnknowUserTest(){
        Service service = new Service(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());
        UserData newUser = new UserData("username","password","email");
        try{
            service.loginUser(newUser);
        }catch(DataAccessException e){
            Assertions.assertEquals(e.getMessage(),"Error: Unknown username","addUser is not working");
        }
    }

    @Test
    public void loginUserUnauthorizeTest(){
        Service service = new Service(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());
        UserData newUser = new UserData("username","password","email");
        UserData wrongPasswordUser = new UserData("username","password1","email");
        try{
            service.addUser(newUser);
            service.loginUser(wrongPasswordUser);
        }catch(DataAccessException e){
            Assertions.assertEquals(e.getMessage(),"Error: Unauthorized","addUser is not working");
        }
    }


    @Test
    public void logoutUserPositiveTest(){
        Service service = new Service(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());
        UserData newUser = new UserData("username","password","email");
        try{
            AuthData authData = service.addUser(newUser);
            service.logoutUser(authData.authToken());
        }catch(DataAccessException e){
            Assertions.assertNotNull(e,"logout is not working");
        }
    }

    @Test
    public void loginUserAlreadyLoggedOutTest(){
        Service service = new Service(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());
        UserData newUser = new UserData("username","password","email");
        UserData newUser1 = new UserData("username1","password1","email1");

        try{
            service.addUser(newUser);
            service.logoutUser(null);
        }catch(DataAccessException e){
            Assertions.assertEquals("Error: Already logged-out username",e.getMessage(),"authData=null given and get wrong error");
        }

        try{
            AuthData authData = service.addUser(newUser1);
            service.logoutUser(authData.authToken());
            service.logoutUser(authData.authToken());
        }catch(DataAccessException e){
            Assertions.assertEquals("Error: Already logged-out username",e.getMessage(),"login is not working");
        }
    }


    @Test
    public void createGamePositiveTest(){
        Service service = new Service(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());
        UserData newUser = new UserData("username","password","email");
        GameData newGame = new GameData(0,null,null,"newgame",null);
        try{
            service.addUser(newUser);
            service.createGame(newGame);
        }catch(DataAccessException e){
            Assertions.assertNotNull(e,"createGame is not working");
        }
    }

    @Test
    public void createGameBadRequestTest() {
        Service service = new Service(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());
        UserData newUser = new UserData("username","password","email");
        GameData newGame = new GameData(0,null,null,null,null);
        try{
            service.addUser(newUser);
            service.createGame(newGame);
        }catch(DataAccessException e){
            Assertions.assertEquals("Error: Not a valid Game name",e.getMessage(),"creatGame is not working");
        }
    }

    @Test
    public void checkAuthPositiveTest(){
        Service service = new Service(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());
        UserData newUser = new UserData("username","password","email");
        try{
            AuthData authData =  service.addUser(newUser);
            service.checkAuth(authData.authToken());
        }catch(DataAccessException e){
            Assertions.assertNotNull(e,"checkAuth is not working");
        }
    }

    @Test
    public void checkAuthUnauthorizeTest(){
        Service service = new Service(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());
        AuthData authData = new AuthData("12312123","username");
        try{
            service.checkAuth(authData.authToken());
        }catch(DataAccessException e){
            Assertions.assertEquals("Error: Unauthorized",e.getMessage(),"addUser is not working");
        }
    }

    @Test
    public void listGamePositiveTest(){
        Service service = new Service(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());
        UserData newUser = new UserData("username","password","email");
        GameData newGame = new GameData(1,null,null,"newgame",null);
        try{
            service.addUser(newUser);
            service.createGame(newGame);
            service.listGames();
        }catch(DataAccessException e){
            Assertions.assertNotNull(e,"listGame is not working");
        }
    }

    @Test
    public void clearDBPositiveTest(){
        Service service = new Service(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());
        UserData newUser = new UserData("username","password","email");
        GameData newGame = new GameData(1,null,null,"newgame",null);
        try{
            service.addUser(newUser);
            service.createGame(newGame);
            service.clearDataBase();
        }catch(DataAccessException e){
            Assertions.assertNotNull(e,"clearDataBase is not working");
        }
    }

    @Test
    public void joinGamePositiveTest(){
        Service service = new Service(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());
        UserData newUser = new UserData("username","password","email");
        GameData newGame = new GameData(1,null,null,"newgame",null);
        JoinGame newJoin = new JoinGame("WHITE",1);
        try{
            AuthData authData = service.addUser(newUser);
            service.createGame(newGame);
            service.joinGame(newJoin,authData.authToken());
        }catch(DataAccessException e){
            Assertions.assertNotNull(e,"clearDataBase is not working");
        }
    }
    @Test
    public void joinGameAlreadyTakenTest(){
        Service service = new Service(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());
        UserData newUser = new UserData("username","password","email");
        UserData newUser1 = new UserData("username1","password1","email1");
        GameData newGame = new GameData(1,null,null,"newgame",null);
        JoinGame newJoin = new JoinGame("WHITE",1);
        JoinGame newJoin1 = new JoinGame("BLACK",1);
        try{
            AuthData authData = service.addUser(newUser);
            service.createGame(newGame);
            service.joinGame(newJoin,authData.authToken());
            service.joinGame(newJoin,authData.authToken());
        }catch(DataAccessException e){
            Assertions.assertEquals("Error: already taken",e.getMessage(),"joinGame is not working");
        }
        try{
            AuthData authData = service.addUser(newUser1);
            service.createGame(newGame);
            service.joinGame(newJoin,authData.authToken());
            service.joinGame(newJoin1,authData.authToken());
            service.joinGame(newJoin,authData.authToken());
        }catch(DataAccessException e){
            Assertions.assertEquals("Error: already taken",e.getMessage(),"joinGame is not working");
        }
    }

    @Test
    public void joinGameBadRequestTest(){
        Service service = new Service(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());
        UserData newUser = new UserData("username","password","email");
        UserData newUser1 = new UserData("username1","password1","email1");
        GameData newGame = new GameData(1,null,null,"newgame",null);
        JoinGame wrongGameID = new JoinGame("WHITE",0);
        JoinGame wrongPlayerColor = new JoinGame("Red",1);
        try{
            AuthData authData = service.addUser(newUser);
            service.createGame(newGame);
            service.joinGame(wrongGameID,authData.authToken());
        }catch(DataAccessException e){
            Assertions.assertEquals("Error: bad request",e.getMessage(),"joinGame is not working");
        }
        try{
            AuthData authData = service.addUser(newUser1);
            service.createGame(newGame);
            service.joinGame(wrongPlayerColor,authData.authToken());
        }catch(DataAccessException e){
            Assertions.assertEquals("Error: not a valid color",e.getMessage(),"joinGame is not working");
        }
    }

}
