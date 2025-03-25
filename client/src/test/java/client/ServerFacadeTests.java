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
            Assertions.assertEquals(e.getMessage(),"failure: 400 Bad Request");
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
            Assertions.assertEquals(e.getMessage(),"failure: 401 Unauthorized");
        }
    }




}
