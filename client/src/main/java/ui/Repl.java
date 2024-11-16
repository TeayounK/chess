package ui;

import com.sun.nio.sctp.NotificationHandler;

import java.util.Scanner;

public class Repl implements NotificationHandler {
    private ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl,this);
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }



    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + GREEN);
    }
}
