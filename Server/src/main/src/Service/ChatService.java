package main.src.Service;

import main.config.CustomException;
import main.config.CustomResponse;
import main.config.ResponseStatus;
import main.src.DAO.ChatDAO;
import main.src.Domain.Chat.Chat;
import main.src.Domain.Chat.ChatDTO;

import java.io.PrintWriter;
import java.util.List;

import static main.src.MainServer.onlineUser;

public class ChatService {
    private static ChatService instance = new ChatService();
    private ChatService() {}
    public static ChatService getInstance() {
        return instance;
    }
    ChatDAO chatDao = ChatDAO.getInstance();

    public void loadChat(String body, PrintWriter out) {
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

    public void sendChat(String body, PrintWriter out) {
        List<ChatDTO> chat;
        CustomResponse customResponse;
        PrintWriter sender;
        try {
            chat = chatDao.sendChat(new Chat(body));
            synchronized (onlineUser) {
                for (ChatDTO c : chat) {
                    if((sender = onlineUser.get(c.getUserId())) != null) {
                        customResponse = new CustomResponse<>(c);
                        sender.println(customResponse.getResponse());
                        sender.flush();
                    }
                }
            }
        } catch (CustomException e) {
            customResponse = new CustomResponse<>(e.getStatus());
            out.println(customResponse.getResponse());
            out.flush();
        }
    }

    public void methodError(PrintWriter out) {
        out.println(new CustomResponse<>(new CustomException(ResponseStatus.METHOD_ERROR)));
        out.flush();
    }
}
