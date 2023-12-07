package main.src.Domain.Room;

import main.src.Domain.Result;

public class Room implements Result {
    private Long roomId;
    private Long userId;
    private String roomName;

    // 채팅방 생성
    public Room(String body) {
        String[] args = body.split(",");
        for (String arg : args) {
            switch (arg.split(":")[0]) {
                case "roomId": roomId = Long.parseLong(arg.split(":")[1]); break;
                case "userId": userId = Long.parseLong(arg.split(":")[1]); break;
                case "roomName": roomName = arg.split(":")[1]; break;
                default: ;
            }
        }
    }

    @Override
    public String getResult() {
        return String.format("roomId:%s,roomName:%s", roomId, roomName);
    }

    // 채팅방 조회
    public Room(Long roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }

    public Long getRoomId() {
        return roomId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getRoomName() {
        return roomName;
    }

    private Room() {
    }
}
