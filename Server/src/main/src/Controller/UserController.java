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
        return switch (header) {
            case "GET" -> userService.login(body, out);
            case "POST" -> userService.signUp(body);
            case "PATCH" -> userService.logout(body);
            default -> userService.methodError();
        };
    }
}
