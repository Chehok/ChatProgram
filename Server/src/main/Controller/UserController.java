package main.Controller;

import main.Service.UserService;

// path /user
public class UserController implements DefaultController {
    private static UserController instance = new UserController();
    private UserController() {}
    public static UserController getInstance() {
        return instance;
    }
    UserService userService = UserService.getInstance();
    @Override
    public void callService(String method, String body) {
        switch (method) {
            case "GET": userService.login(body); break;
            case "POST": userService.signUp(body); break;
            case "PATCH": userService.logout(body); break;
            default: userService.methodError();
        }
    }
}
