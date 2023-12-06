package main.src.Domain.Chat;

import main.src.Domain.Result;

public class ChatDto implements Result {
    private Long userId;
    private String roomName;
    private String nickname;
    private String message;

    public ChatDto(String roomName, String nickname, String message) {
        this.roomName = roomName;
        this.nickname = nickname;
        this.message = message;
    }

    public ChatDto(Long userId, String roomName, String nickname, String message) {
        this.userId = userId;
        this.roomName = roomName;
        this.nickname = nickname;
        this.message = message;
    }

    @Override
    public String getResult() {
        return String.format("roomName:%s,nickname:%s,message:%s", roomName, nickname, message);
    }

    public Long getUserId() { return userId; }

    private ChatDto() {}
}
