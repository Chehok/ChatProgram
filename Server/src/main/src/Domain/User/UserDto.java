package main.src.Domain.User;

import main.src.Domain.Result;

public class UserDto implements Result {
    private Long userId;
    private String nickname;

    public UserDto(long userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }

    public Long getUserId() { return userId; }

    public String getNickname() { return nickname; }

    @Override
    public String getResult() {
        return String.format("userId:%l,nickname:%s", userId, nickname);
    }
}
