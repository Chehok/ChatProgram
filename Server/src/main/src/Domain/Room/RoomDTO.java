package main.src.Domain.Room;

import main.src.Domain.Result;

public class RoomDTO implements Result {
    private String roomName;
    private String nickname;
    private Long userId;

    public RoomDTO(String roomName, String nickname, Long userId){
        this.roomName = roomName;
        this.nickname = nickname;
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
    public String getNickname() {
        return nickname;
    }
    public String getRoomName() {
        return roomName;
    }

    @Override
    public String getResult() {
        return String.format("roomName:%s,nickname:%s,userId:%s", roomName, nickname, userId);
    }
}
