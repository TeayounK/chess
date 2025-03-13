package dataaccess;

import model.AuthData;
import model.GameData;
import model.JoinGame;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Service;

public class MySqlDAOTest {
    private Service service = new Service(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());


    public void setting(){
        DataAccessUser dataAccessUser = new MemoryUserDAO();
        DataAccessAuth dataAccessAuth = new MemoryAuthDAO();
        DataAccessGame dataAccessGame = new MemoryGameDAO();

        try {
            dataAccessUser = new MySqlDataAccessUser();
            dataAccessAuth = new MySqlDataAccessAuth();
            dataAccessGame = new MySqlDataAccessGame();

        } catch(DataAccessException e) {
            System.out.println(e.getMessage());
        }
        this.service = new Service(dataAccessUser, dataAccessAuth, dataAccessGame);
    }

    @BeforeEach
    public void reset() throws DataAccessException {
        setting();
        service.clearDataBase();
    }

    @Test
    public void addUserPositiveTest() {
        UserData newUser = new UserData("username","password","email");
        try{
            service.addUser(newUser);
        }catch(DataAccessException e){
            Assertions.assertNotNull(e,"addUser is not working");
        }
    }

    @Test
    public void addUserBadRequestTest() {
        UserData newUser = new UserData(null, "password", "email");
        try {
            service.addUser(newUser);
        } catch (DataAccessException e) {
            Assertions.assertEquals(e.getMessage(), "Error: bad request", "addUser is not working");
        }
    }
    @Test
    public void addUserBadRequestTest2() {
        UserData newUser1 = new UserData("username", null, "email");
        try {
            service.addUser(newUser1);
        } catch (DataAccessException e) {
            Assertions.assertEquals(e.getMessage(), "Error: bad request", "addUser is not working");
        }
    }
    @Test
    public void addUserBadRequestTest3() {
        UserData newUser2 = new UserData("username","password",null);
        try{
            service.addUser(newUser2);
        }catch(DataAccessException e){
            Assertions.assertEquals(e.getMessage(),"Error: bad request","addUser is not working");
        }
    }

    @Test
    public void addUserAlreadyTakenTest() {

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

        UserData newUser = new UserData("username","password","email");
        try{
            service.loginUser(newUser);
        }catch(DataAccessException e){
            Assertions.assertEquals(e.getMessage(),"Error: Unknown username","addUser is not working");
        }
    }

    @Test
    public void loginUserUnauthorizeTest(){

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

        UserData newUser = new UserData("username","password","email");
        try{
            AuthData authData = service.addUser(newUser);
            service.logoutUser(authData.authToken());
        }catch(DataAccessException e){
            Assertions.assertNotNull(e,"logout is not working");
        }
    }

    @Test
    public void loginUserAlreadyLoggedOutTest() {

        UserData newUser = new UserData("username", "password", "email");

        try {
            service.addUser(newUser);
            service.logoutUser(null);
        } catch (DataAccessException e) {
            Assertions.assertEquals("Error: Already logged-out username", e.getMessage(), "authData=null given and get wrong error");
        }
    }

    @Test
    public void loginUserAlreadyLoggedOutTest1(){
        try{
            UserData newUser1 = new UserData("username1", "password1", "email1");
            AuthData authData = service.addUser(newUser1);
            service.logoutUser(authData.authToken());
            service.logoutUser(authData.authToken());
        }catch(DataAccessException e){
            Assertions.assertEquals("Error: Already logged-out username",e.getMessage(),"login is not working");
        }
    }


    @Test
    public void createGamePositiveTest(){

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

        AuthData authData = new AuthData("12312123","username");
        try{
            service.checkAuth(authData.authToken());
        }catch(DataAccessException e){
            Assertions.assertEquals("Error: Unauthorized",e.getMessage(),"addUser is not working");
        }
    }

    @Test
    public void listGamePositiveTest(){

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
        GameData newGame = new GameData(1,null,null,"newgame",null);
        try{
            JoinGame newJoin = new JoinGame("WHITE",1);
            UserData newUser = new UserData("username","password","email");

            AuthData authData = service.addUser(newUser);
            service.createGame(newGame);
            service.joinGame(newJoin,authData.authToken());
            service.joinGame(newJoin,authData.authToken());
        }catch(DataAccessException e){
            Assertions.assertEquals("Error: already taken",e.getMessage(),"joinGame is not valid");
        }
    }

    @Test
    public void joinGameBadRequestTest(){
        GameData newGame = new GameData(1,null,null,"newgame",null);
        try{
            JoinGame wrongGameID = new JoinGame("WHITE",0);
            UserData newUser = new UserData("username","password","email");
            AuthData authData = service.addUser(newUser);
            service.createGame(newGame);
            service.joinGame(wrongGameID,authData.authToken());
        }catch(DataAccessException e){
            Assertions.assertEquals("Error: bad request",e.getMessage(),"joinGame is not valid");
        }
    }

    @Test
    public void joinGameBadRequestTest2(){
        GameData newGame = new GameData(1,null,null,"newgame",null);

        try{
            JoinGame wrongPlayerColor = new JoinGame("Red",1);
            UserData newUser1 = new UserData("username1","password1","email1");
            AuthData authData = service.addUser(newUser1);
            service.createGame(newGame);
            service.joinGame(wrongPlayerColor,authData.authToken());
        }catch(DataAccessException e){
            Assertions.assertEquals("Error: not a valid color",e.getMessage(),"joinGame is not working");
        }
    }
}
