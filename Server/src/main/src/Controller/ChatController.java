package main.src.Controller;

import main.src.Service.ChatService;

public class ChatController implements DefaultController {
    private static ChatController instance = new ChatController();
    private ChatController() {}
    public static ChatController getInstance() {
        return instance;
    }
    private ChatService chatService = ChatService.getInstance();
    @Override
    public void callService(String header, String body) {
        switch (header) {
            case "GET": chatService.loadChat(body); break;
            case "POST": chatService.sendChat(body); break;
            default: chatService.methodError();
        }
    }
}
