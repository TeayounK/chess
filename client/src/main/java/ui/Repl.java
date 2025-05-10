package ui;

import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler{
    private final ChessClient client;
    private States state;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl, this);
        this.state = States.PRELOGIN;
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (!result.equals("quit")) {
            System.out.println(switch(this.state){
                case States.PRELOGIN -> printPromptPreLogin();
                case States.LOGIN -> printPromptLogin();
                case States.GAME -> printPromptGame();
                case States.WATCH -> printPromptGame();
                case States.RESIGN -> printPromptResign();
            });

            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                this.state = client.state;
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private String printPromptResign() {
        return """
                \n
                Options:
                Are you sure you want to resign the game? (y/n)
                """;
    }

    static String printPromptPreLogin() {
        return """
                \n
                Options:
                Login as an existing user: "l", "login" <USERNAME> <PASSWORD>
                Register a new user: "r", "register" <USERNAME> <PASSWORD> <EMAIL>
                Exit the program: "q", "quit"
                Print this message: "h", "help"
                """;
    }

    static String printPromptLogin() {
        return """
                \n
                Options:
                List current games: "l", "list"
                Create a new game: "c", "create" <GAMENAME>
                Join a game: "j", "join" <GAME NUMBER> <COLOR> 
                Watch a game: "w", "watch" <GAME NUMBER>
                Logout: "logout"
                """;
    }

    static String printPromptGame() {
        return """
                \n
                Options:
                Highlight legal moves: "hi", "highlight" <position> (e.g. f5)
                Make a move: "m", "move", "make" <source> <destination> <optional promotion> (e.g. f5 e4 q)
                Redraw Chess Board: "r", "redrew"
                Change color scheme: "c", "colors" <color number>
                Resign from game: "res", "resign"
                Leave game: "leave"
                """;
    }

    @Override
    public void notify(String serverMessage) {
        System.out.println("<Repl notify>");
        client.notify(serverMessage);
    }
}
