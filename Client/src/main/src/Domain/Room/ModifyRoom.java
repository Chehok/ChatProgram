package main.src.Domain.Room;

import main.src.Domain.DefaultDomain;

public class ModifyRoom implements DefaultDomain {
    private String roomId;
    private String userId;

    public ModifyRoom(String roomId, String userId) {
        this.roomId = roomId;
        this.userId = userId;
    }
    @Override
    public String getDomain() {
        return String.format("roomId:%s,userId:%s", roomId, userId);
    }
}
