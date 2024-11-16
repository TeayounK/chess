package ui;

import java.util.Scanner;

public class ReplGame {
    private ChessClientGame client;

    public ReplGame(String serverUrl) {
        client = new ChessClientGame(serverUrl,this);
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
                Highlight legal moves: "hi", "highlight" <position> (e.g. f5)
                Make a move: "m", "move", "make" <source> <destination> <optional promotion> (e.g. f5 e4 q)
                Redraw Chess Board: "r", "redrew"
                Change color scheme: "c", "colors" <color number>
                Resign from game: "res", "resign"
                Leave game: "leave"
                \n
                """);
    }
}
