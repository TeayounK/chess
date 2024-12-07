package ui;

import messages.ServerMessage;

public interface NotificationHandler {
    void notify(ServerMessage serverMessage);
}
