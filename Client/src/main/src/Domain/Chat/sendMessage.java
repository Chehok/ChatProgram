package main.src.Domain.Chat;

import main.src.Domain.DefaultDomain;

public class sendMessage implements DefaultDomain {
    private String roomId;
    private String message;
    private String userId;

    public sendMessage(String roomId, String message, String userId){
        this.roomId = roomId;
        this.message = message;
        this.userId = userId;
    }
    @Override
    public String getDomain() {
        return String.format("roomId:%s,message:%s,userId:%s", roomId, message, userId);
    }
}
