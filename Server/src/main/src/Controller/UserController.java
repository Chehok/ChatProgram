package main.src.Controller;

import main.src.Service.UserService;

import java.io.PrintWriter;

public class UserController implements DefaultController {
    private static UserController instance = new UserController();
    private UserController() {}
    public static UserController getInstance() {
        return instance;
    }
    UserService userService = UserService.getInstance();
    @Override
    public void callService(String header, String body, PrintWriter out) {
        switch (header) {
            case "GET": userService.login(body, out); break;
            case "POST": userService.signUp(body, out); break;
            case "PATCH": userService.logout(body, out); break;
            default: userService.methodError(out);
        }
    }
}
