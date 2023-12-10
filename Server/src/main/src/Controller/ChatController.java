package main.src.Controller;

import main.config.CustomResponse;
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
    public CustomResponse callService(String header, String body, PrintWriter out) {
        return switch (header) {
            case "GET" -> chatService.loadChat(body);
            case "POST" -> chatService.sendChat(body);
            default -> chatService.methodError();
        };
    }
}
