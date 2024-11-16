package ui;

import java.util.Scanner;

public class ReplLogin {
    private ChessClientLogin client;

    public ReplLogin(String serverUrl) {
        client = new ChessClientLogin(serverUrl,this);
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

//    public void notify(Notification notification) {
//        System.out.println(notification.message());
//        printPrompt();
//    }


    private void printPrompt() {
        System.out.print("""
                Options:
                List current games: "l", "list"
                Create a new game: "c", "create" <GAMENAME>
                Join a game: "j", "join" <GAME ID> <COLOR>
                Watch a game: "w", "watch" <GAME ID>
                Logout: "logout"
                \n
                """);
    }
}
