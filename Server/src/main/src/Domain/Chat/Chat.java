package main.src.Domain.Chat;

public class Chat {
    private Long roomId;
    private String message;
    private Long userId;

    public Chat(String body) {
        String[] args = body.split(",");
        for (String arg : args) {
            switch (arg.split(":")[0]) {
                case "roomId": roomId = Long.parseLong(arg.split(":")[1]); break;
                case "userId": userId = Long.parseLong(arg.split(":")[1]); break;
                case "message": message = arg.split(":")[1]; break;
                default: ;
            }
        }
    }

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

    private Chat() { }
}
