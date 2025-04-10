package ui;

import websocket.messages.ServerMessage;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler{
    private final ChessClient client;
    private States state;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
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
            });

            String line = scanner.nextLine();

//            do I need to separate two cases whether we want to do websocket or http?

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


}
