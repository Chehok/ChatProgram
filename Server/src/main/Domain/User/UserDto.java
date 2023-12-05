package main.Domain.User;

public class UserDto {
    private long userId = 1;
    private String nickname;

    public UserDto(long userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }

    public long getUserId() { return userId; }

    public String getNickname() { return nickname; }
}
