package main.src.Domain.Room;

import main.src.Domain.DefaultDomain;

public class CreateRoom implements DefaultDomain {
    private String roomName;
    private String userId;

    public CreateRoom(String roomName, String userId) {
        this.roomName = roomName;
        this.userId = userId;
    }
    @Override
    public String getDomain() {
        return String.format("roomName:%s,userId:%s", roomName, userId);
    }
}
