package main.src.Service;

import main.src.Dao.UserDao;
import main.src.Domain.User.User;
import main.src.Domain.User.UserDto;
import main.config.CustomException;
import main.config.CustomResponse;
import main.config.ResponseStatus;

import static main.src.MainServer.onlineUser;
import static main.src.ServerThread.out;

public class UserService {
    private static UserService instance = new UserService();
    private UserService() {}
    public static UserService getInstance() {
        return instance;
    }
    UserDao userDao = UserDao.getInstance();

    public void signUp(String body) {
        CustomResponse customResponse = null;
        try {
            userDao.signUp(new User(body));
            customResponse = new CustomResponse<>("회원 가입 성공");
        } catch (CustomException e) {
            customResponse = new CustomResponse<>(e.getStatus());
        } finally {
            out.println(customResponse.getResponse());
            out.flush();
        }
    }

    public void login(String body) {
        CustomResponse customResponse = null;
        UserDto userDto;
        try {
            userDto = userDao.login(new User(body));
            customResponse = new CustomResponse(userDto);

            synchronized (onlineUser) {
                onlineUser.put(userDto.getUserId(), out);
            }
        } catch (CustomException e) {
            customResponse = new CustomResponse<>(e.getStatus());
        } finally {
            out.println(customResponse.getResponse());
            out.flush();
        }
    }

    public void logout(String body) {
        CustomResponse customResponse;
        Long userId = new User(body).getUserId();
        onlineUser.remove(userId);

        if (userId == null) {
            customResponse = new CustomResponse(new CustomException(ResponseStatus.BODY_ERROR));
        } else {
            customResponse = new CustomResponse("로그아웃 되었습니다!");
        }
        out.println(customResponse.getResponse());
        out.flush();
    }

    public void methodError() {
        out.println(new CustomResponse<>(new CustomException(ResponseStatus.METHOD_ERROR)));
        out.flush();
    }
}
