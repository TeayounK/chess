package client;

import model.GameData;
import model.JoinGame;
import model.ListResult;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ResponseException;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade("http://localhost:"+port);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void registerPositive() throws Exception {
        facade.deleteDataBase();
        var authData = facade.createUser("player1", "password", "p1@email.com");
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerNegative() throws Exception {
        try{
            facade.deleteDataBase();
            var authData = facade.createUser("player1", "password", null);
            assertTrue(authData.authToken().length() > 10);
        }catch(ResponseException e){
            Assertions.assertEquals(e.getMessage(),"failure: Bad Request");
        }
    }

    @Test
    void loginPositive() throws Exception{
        facade.deleteDataBase();
        var authData = facade.createUser("player1", "password", "p1@email.com");
        try{
            var newAuthData = facade.loginUser("player1", "password");
            assertTrue(newAuthData.authToken().length() > 10);
        }catch(ResponseException e){
            throw new Exception("login failed");
        }
    }

    @Test
    void loginNegative() throws Exception{
        facade.deleteDataBase();
        var authData = facade.createUser("player1", "password", "p1@email.com");
        try{
            var newAuthData = facade.loginUser("player1", "wrongPassword");
            assertTrue(newAuthData.authToken().length() > 10);
        }catch(ResponseException e){
            Assertions.assertEquals(e.getMessage(),"failure: Unauthorized");
        }
    }

    @Test
    void logoutPositive() throws Exception{
        facade.deleteDataBase();
        var authData = facade.createUser("player1", "password", "p1@email.com");
        try{
            facade.logoutUser(authData);
            assertTrue(true);
        }catch(ResponseException e){
            Assertions.assertEquals(e.getMessage(),"failure: Unauthorized");
        }
    }

    @Test
    void logoutNegative() throws Exception{
        facade.deleteDataBase();
        var authData = facade.createUser("player1", "password", "p1@email.com");
        try{
            facade.logoutUser(authData);
            facade.logoutUser(authData);
            assertTrue(true);
        }catch(ResponseException e){
            Assertions.assertEquals(e.getMessage(),"failure: Unauthorized");
        }
    }

    @Test
    void createGamePositive() throws Exception{
        facade.deleteDataBase();
        try{
            var authData = facade.createUser("player1", "password", "p1@email.com");
            GameData game = facade.createGame(authData,"Testgame");
            Assertions.assertEquals(1,game.gameID());
        }catch(ResponseException e){
            Assertions.assertEquals(e.getMessage(),"failure: Unauthorized");
        }
    }

    @Test
    void createGameNegative() throws Exception{
        facade.deleteDataBase();
        try{
            var authData = facade.createUser("player1", "password", "p1@email.com");
            GameData game = facade.createGame(null,"Testgame");
            Assertions.assertEquals("Testgame",game.gameName());
        }catch(ResponseException e){
            Assertions.assertEquals(e.getMessage(),"failure: Unauthorized");
        }
    }

    @Test
    void listGamePositive() throws Exception{
        facade.deleteDataBase();
        try{
            facade.deleteDataBase();
            var authData = facade.createUser("player1", "password", "p1@email.com");
            GameData game = facade.createGame(authData,"Testgame");
            ListResult result = facade.listGames(authData);
            Assertions.assertEquals("Testgame", result.games().getFirst().gameName());
        }catch(ResponseException e){
            Assertions.assertEquals(e.getMessage(),"failure: Unauthorized");
        }
    }

    @Test
    void listGameNegative() throws Exception{
        facade.deleteDataBase();
        try{
            var authData = facade.createUser("player1", "password", "p1@email.com");
            GameData game = facade.createGame(null,"Testgame");
            facade.listGames(null);
            Assertions.assertEquals("Testgame",game.gameName());
        }catch(ResponseException e){
            Assertions.assertEquals(e.getMessage(),"failure: Unauthorized");
        }
    }

    @Test
    void joinGamePositive() throws Exception{
        facade.deleteDataBase();
        try{
            facade.deleteDataBase();
            var authData = facade.createUser("player1", "password", "p1@email.com");
            GameData game = facade.createGame(authData,"Testgame2");
            JoinGame joinGame = new JoinGame("white",game.gameID());
            facade.joinGame(authData,joinGame);
            ListResult result = facade.listGames(authData);
            Assertions.assertEquals("[GameData[gameID=2, whiteUsername=player1, blackUsername=null, gameName=Testgame2, game=null]]",
                    result.games().toString());
        }catch(ResponseException e){
            Assertions.assertEquals(e.getMessage(),"failure: Unauthorized");
        }
    }

    @Test
    void joinGameNegative() throws Exception{
        facade.deleteDataBase();
        try{
            facade.deleteDataBase();
            var authData = facade.createUser("player1", "password", "p1@email.com");
            GameData game = facade.createGame(authData,"Testgame2");
            JoinGame joinGame = new JoinGame("RED",game.gameID());
            facade.joinGame(authData,joinGame);
            ListResult result = facade.listGames(authData);
            Assertions.assertEquals("[GameData[gameID=2, whiteUsername=null, blackUsername=null, gameName=Testgame1, game=null]]",
                    result.games().toString());
        }catch(ResponseException e){
            Assertions.assertEquals("failure: Bad Request",e.getMessage());
        }
    }


}
