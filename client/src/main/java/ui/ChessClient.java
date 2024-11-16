package ui;

import java.util.Arrays;

import static ui.Repl.*;

public class ChessClient {
    private ServerFacade server;
    private final String serverUrl;
    public States state;


    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.state = States.PRELOGIN;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> logIn(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ui.ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String help() {
        return switch (this.state){
            case States.PRELOGIN -> printPromptPreLogin();
            case States.LOGIN -> printPromptLogin();
            case States.GAME -> printPromptGame();
        };
    }

    private String logIn(String[] params) throws ResponseException {

    }
    
    private String register(String[] params) throws ResponseException{
        
    } 
}
