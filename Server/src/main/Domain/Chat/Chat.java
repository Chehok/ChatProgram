package main.Domain.Chat;

public class Chat {
    private Long roomId;
    private String message;
    private Long userId;

    public Chat(Long roomId, String message, Long userId) {
        this.roomId = roomId;
        this.message = message;
        this.userId = userId;
    }

    public Long getRoomId() {
        return roomId;
    }
    public String getMessage() {
        return message;
    }
    public Long getUserId() {
        return userId;
    }

    private Chat() {
    }
}
