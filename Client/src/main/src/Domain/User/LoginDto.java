package main.src.Domain.User;

import main.src.Domain.DefaultDomain;

public class LoginDto implements DefaultDomain {
    private String username;
    private String password;

    public LoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
    @Override
    public String getDomain() {
        return String.format("username:%s,password:%s", username, password);
    }
}
