package main.Service;

import java.io.PrintWriter;

import static main.MainServer.onlineUser;

public class ChatService {
    private void sendChat(String message) {

        synchronized (onlineUser) {
            for (PrintWriter out : onlineUser.values()) {
                out.println(message);
                out.flush();
            }
        }
    }
}
