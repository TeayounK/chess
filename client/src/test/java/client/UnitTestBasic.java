package client;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.Server;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnitTestBasic {
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        Server server = new Server();
        var port = server.run(0);
        facade = new ServerFacade("http://localhost:"+port);
        System.out.println("Started test HTTP server on " + port);
//        assert(port,"Not functioning");
    }

    @AfterAll
    static void stopServer() {
        Server server = new Server();
        var port = server.run(0);
//        assert(port.stop(),"not functioning");
    }


    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    void register() throws Exception {
        var authData = facade.createUser("player1", "password", "p1@email.com");
        assertTrue(authData.authToken().length() > 10);
    }

}
