package main.src.Service;

import main.src.Dao.ChatDao;
import main.src.Domain.Chat.Chat;
import main.src.Domain.Chat.ChatDto;
import main.config.CustomException;
import main.config.CustomResponse;
import main.config.ResponseStatus;

import java.io.PrintWriter;
import java.util.List;

import static main.src.MainServer.onlineUser;
import static main.src.ServerThread.out;

public class ChatService {
    private static ChatService instance = new ChatService();
    private ChatService() {}
    public static ChatService getInstance() {
        return instance;
    }
    ChatDao chatDao = ChatDao.getInstance();

    public void loadChat(String body) {
        CustomResponse customResponse = null;
        try {
            customResponse = new CustomResponse<>(chatDao.loadChat(new Chat(body)));
        } catch (CustomException e) {
            customResponse = new CustomResponse<>(e.getStatus());
        } finally {
            if (customResponse == null) out.println(new CustomResponse<>(ResponseStatus.DB_ERROR));
            else out.println(customResponse.getResponse());
            out.flush();
        }
    }

    public void sendChat(String body) {
        CustomResponse<List<ChatDto>> customResponse;
        PrintWriter sender;
        try {
            customResponse = new CustomResponse<>(chatDao.sendChat(new Chat(body)));
            synchronized (onlineUser) {
                for (ChatDto c : customResponse.getResult()) {
                    sender = onlineUser.get(c.getUserId());
                    sender.println(customResponse.getResponse());
                    sender.flush();
                }
            }
        } catch (CustomException e) {
            customResponse = new CustomResponse<>(e.getStatus());
            out.println(customResponse.getResponse());
            out.flush();
        }
    }

    public void methodError() {
        out.println(new CustomResponse<>(new CustomException(ResponseStatus.METHOD_ERROR)));
        out.flush();
    }
}
