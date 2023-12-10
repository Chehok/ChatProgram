package main.src.Service;

import main.config.CustomException;
import main.config.CustomResponse;
import main.src.DAO.ChatDAO;
import main.src.Domain.Chat.Chat;
import main.src.Domain.Chat.ChatDTO;

import java.io.PrintWriter;
import java.util.List;

import static main.config.ResponseStatus.DB_ERROR;
import static main.config.ResponseStatus.METHOD_ERROR;
import static main.src.MainServer.onlineUser;

public class ChatService {
    private static ChatService instance = new ChatService();
    private ChatService() {}
    public static ChatService getInstance() {
        return instance;
    }
    ChatDAO chatDao = ChatDAO.getInstance();

    public CustomResponse loadChat(String body) {
        CustomResponse customResponse = null;
        try {
            customResponse = new CustomResponse<>(chatDao.loadChat(new Chat(body)));
        } catch (CustomException e) {
            customResponse = new CustomResponse<>(e.getStatus());
        } finally {
            if (customResponse == null)
                customResponse = new CustomResponse(DB_ERROR);
            return customResponse;
        }
    }

    public CustomResponse sendChat(String body) {
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
            return null;
        } catch (CustomException e) {
            customResponse = new CustomResponse<>(e.getStatus());
            return customResponse;
        }
    }

    public CustomResponse methodError() {
        return new CustomResponse<>(new CustomException(METHOD_ERROR));
    }
}
