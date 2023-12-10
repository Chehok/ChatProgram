package main.src.Controller;

import main.src.Service.ChatService;

import java.io.PrintWriter;

public class ChatController implements DefaultController {
    private static ChatController instance = new ChatController();
    private ChatController() {}
    public static ChatController getInstance() {
        return instance;
    }
    private ChatService chatService = ChatService.getInstance();
    @Override
    public void callService(String header, String body, PrintWriter out) {
        switch (header) {
            case "GET": chatService.loadChat(body, out); break;
            case "POST": chatService.sendChat(body, out); break;
            default: chatService.methodError(out);
        }
    }
}
