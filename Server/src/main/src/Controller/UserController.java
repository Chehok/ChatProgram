package main.src.Controller;

import main.config.CustomResponse;
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
    public CustomResponse callService(String header, String body, PrintWriter out) {
        switch (header) {
            case "GET": return userService.login(body, out);
            case "POST": return userService.signUp(body);
            case "PATCH": return userService.logout(body);
            default: return userService.methodError();
        }
    }
}
