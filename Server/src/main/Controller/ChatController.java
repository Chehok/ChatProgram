package main.Controller;

import main.Service.ChatService;

//path /chat
public class ChatController implements DefaultController {
    private static ChatController instance = new ChatController();
    private ChatController() {}
    public static ChatController getInstance() {
        return instance;
    }
    private ChatService chatService = ChatService.getInstance();
    @Override
    public void callService(String method, String body) {
        switch (method) {
            case "GET": chatService.loadChat(body); break;
            case "POST": chatService.sendChat(body); break;
            default: chatService.methodError();
        }
    }
}
