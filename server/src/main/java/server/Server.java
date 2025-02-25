package server;

import service.Service;

public class Server {
    private Service service = new Service(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());
}