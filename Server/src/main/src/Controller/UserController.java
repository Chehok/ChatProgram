package main.src.Controller;

import main.src.Service.UserService;

public class UserController implements DefaultController {
    private static UserController instance = new UserController();
    private UserController() {}
    public static UserController getInstance() {
        return instance;
    }
    UserService userService = UserService.getInstance();
    @Override
    public void callService(String header, String body) {
        switch (header) {
            case "GET": userService.login(body); break;
            case "POST": userService.signUp(body); break;
            case "PATCH": userService.logout(body); break;
            default: userService.methodError();
        }
    }
}
