package main.Service;

import main.Dao.UserDao;
import main.Domain.User.User;
import main.Domain.User.UserDto;
import main.config.CustomException;
import main.config.Response;
import main.config.ResponseStatus;

import static main.MainServer.onlineUser;
import static main.ServerThread.out;

public class UserService {
    private static UserService instance = new UserService();
    private UserService() {}
    public static UserService getInstance() {
        return instance;
    }
    UserDao userDao = UserDao.getInstance();

    public void signUp(String body) {
        Response response = null;
        try {
            userDao.signUp(new User(body));
            response = new Response<>("회원 가입 성공");
        } catch (CustomException e) {
            response = new Response<>(e.getStatus());
        } finally {
            out.println(response.getResponse());
            out.flush();
        }
    }

    public void login(String body) {
        Response response = null;
        UserDto userDto;
        try {
            userDto = userDao.login(new User(body));
            response = new Response(userDto);

            synchronized (onlineUser) {
                onlineUser.put(userDto.getUserId(), out);
            }
        } catch (CustomException e) {
            response = new Response<>(e.getStatus());
        } finally {
            out.println(response.getResponse());
            out.flush();
        }
    }

    public void logout(String body) {
        Response response;
        Long userId = new User(body).getUserId();
        onlineUser.remove(userId);

        if (userId == null) {
            response = new Response(new CustomException(ResponseStatus.BODY_ERROR));
        } else {
            response = new Response("로그아웃 되었습니다!");
        }
        out.println(response.getResponse());
        out.flush();
    }

    public void methodError() {
        out.println(new Response<>(new CustomException(ResponseStatus.METHOD_ERROR)));
        out.flush();
    }
}
