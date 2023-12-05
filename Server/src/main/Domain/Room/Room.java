package main.Domain.Room;

public class Room {
    private Long roomId;
    private String roomName;

    // 채팅방 생성
    public Room(String roomName) {
        this.roomName = roomName;
    }

    // 채팅방 조회
    public Room(Long roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    private Room() {
    }
}
