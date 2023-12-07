package main.src.Domain.User;

import main.src.Domain.DefaultDomain;

public class SignUpDto implements DefaultDomain {
    private String username;
    private String password;
    private String nickname;

    public SignUpDto(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }
    @Override
    public String getDomain() {
        return String.format("username:%s,password:%s,nickname:%s", username, password, nickname);
    }
}
