package ui;

import websocket.messages.Notification;
import websocket.messages.ServerMessage;

public interface NotificationHandler {
    void notify(String serverMessage);
}
