package main.Domain.Room;

public class RoomUser {
    private long roomId;
    private long userId;

    public RoomUser(long roomId, long userId) {
        this.roomId = roomId;
        this.userId = userId;
    }

    public long getRoomId() {
        return roomId;
    }
    public long getUserId() {
        return userId;
    }
    private RoomUser() {}
}
