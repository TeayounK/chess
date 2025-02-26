package server;

import service.Service;
import spark.Spark;

public class Server {
    private Service service = new Service(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());

    public int run(int desiredPort) {
        DataAccessUser dataAccessUser = new MemoryUserDAO();
        DataAccessAuth dataAccessAuth = new MemoryAuthDAO();
        DataAccessGame dataAccessGame = new MemoryGameDAO();

        this.service = new Service(dataAccessUser, dataAccessAuth, dataAccessGame);

        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}