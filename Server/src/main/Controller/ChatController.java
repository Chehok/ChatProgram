package main.Controller;

import main.config.ResponseBody;

//path /chat
public class ChatController implements DefaultController {
    private static ChatController instance = new ChatController();
    private ChatController() {}
    public static ChatController getInstance() {
        return instance;
    }
    @Override
    public ResponseBody callMethod(String method, String body) {
//        switch (method) {
//            case "GET": loadChat(body);
//            case "POST": sendChat(body);
//            default: break;
//        }
        return null;
    }

    // GET
    public void loadChat() {

    }

    // POST
    public void sendChat() {

    }
}
