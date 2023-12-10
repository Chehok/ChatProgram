package main.src.Controller;

import java.io.PrintWriter;

public class ProxyController implements DefaultController {
    DefaultController controller;
    /**
     * request[0] (header) == "POST /user"
     * request[1] (body) == "username:username1,password:password1,nickname:nickname1"
     */
    @Override
    public void callService(String header, String body, PrintWriter out) {
        switch (header.split(" ")[1]) { // path
            case "/user":
                controller = UserController.getInstance();
                break;
            case "/room":
                controller = RoomController.getInstance();
                break;
            case "/chat":
                controller = ChatController.getInstance();
                break;
            default: ; // 잘못된 요청
        }
        controller.callService(header.split(" ")[0], body, out);
    }
}
