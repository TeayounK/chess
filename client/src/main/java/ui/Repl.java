package ui;

import com.sun.nio.sctp.NotificationHandler;

import javax.management.Notification;
import java.util.Scanner;

public class Repl {
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
                Login as an existing user: "l", "login" <USERNAME> <PASSWORD>
                Register a new user: "r", "register" <USERNAME> <PASSWORD> <EMAIL>
                Exit the program: "q", "quit"
                Print this message: "h", "help"
                \n
                """);
    }
}
