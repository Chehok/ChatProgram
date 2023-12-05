package main.Controller;

import main.Service.UserService;
import main.config.ResponseBody;
import static main.config.ResponseStatus.*;

// path /user
public class UserController implements DefaultController {
    private static UserController instance = new UserController();
    private UserController() {}
    public static UserController getInstance() {
        return instance;
    }
    UserService userService;
    @Override
    public ResponseBody callMethod(String method, String body) {
        switch (method) {
            case "GET": return userService.login(body);
            case "POST": return userService.signUp(body);
            case "PATCH": return userService.logout(body);
            default: return new ResponseBody(CLIENT_ERROR); // 잘못된 요청 이라는 메시지 반환
        }
    }
}
