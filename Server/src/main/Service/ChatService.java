package main.Service;

import main.Dao.ChatDao;
import main.Domain.Chat.Chat;
import main.Domain.Chat.ChatDto;
import main.config.CustomException;
import main.config.Response;
import main.config.ResponseStatus;

import java.io.PrintWriter;
import java.util.List;

import static main.MainServer.onlineUser;
import static main.ServerThread.out;

public class ChatService {
    private static ChatService instance = new ChatService();
    private ChatService() {}
    public static ChatService getInstance() {
        return instance;
    }
    ChatDao chatDao = ChatDao.getInstance();

    public void loadChat(String body) {
        Response response = null;
        try {
            response = new Response<>(chatDao.loadChat(new Chat(body)));
        } catch (CustomException e) {
            response = new Response<>(e.getStatus());
        } finally {
            if (response == null) out.println(new Response<>(ResponseStatus.DB_ERROR));
            else out.println(response.getResponse());
            out.flush();
        }
    }

    public void sendChat(String body) {
        Response<List<ChatDto>> response;
        PrintWriter sender;
        try {
            // DB
            response = new Response<>(chatDao.sendChat(new Chat(body)));
            synchronized (onlineUser) {
                for (ChatDto c : response.getResult()) {
                    sender = onlineUser.get(c.getUserId());
                    sender.println(response.getResponse());
                    sender.flush();
                }
            }
        } catch (CustomException e) {
            response = new Response<>(e.getStatus());
            out.println(response.getResponse());
            out.flush();
        }
    }

    public void methodError() {
        out.println(new Response<>(new CustomException(ResponseStatus.METHOD_ERROR)));
        out.flush();
    }
}
