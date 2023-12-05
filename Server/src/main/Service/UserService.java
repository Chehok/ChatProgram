package main.Service;

import main.Dao.UserDao;
import main.Domain.User.User;
import main.Domain.User.UserDto;
import main.config.CustomException;
import main.config.ResponseBody;

public class UserService {
    UserDao userDao;

    public ResponseBody<String> signUp(String body) {
        try {
            userDao.signUp(new User(body));
            return new ResponseBody<>("회원 가입 성공");
        } catch (CustomException e) {
            return new ResponseBody<>(e.getStatus());
        }
    }

    public ResponseBody<UserDto> login(String body) {
        UserDto userDto;
        try {
            userDto = userDao.login(new User(body));
            return new ResponseBody<>(userDto);
        } catch (CustomException e) {
            return new ResponseBody<>(e.getStatus());
        }
    }

    public ResponseBody<String> logout(String body) {
        String result = "로그아웃 되었습니다!";
        return new ResponseBody<>(result);
    }
}
