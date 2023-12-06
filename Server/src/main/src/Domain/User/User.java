package main.src.Domain.User;

public class User {
    private Long userId;
    private String username;
    private String password;
    private String nickname;

    // "username:username1,password:password1,nickname:nickname1"
    public User(String body) {
        String[] args = body.split(",");
        for (String arg : args) {
            switch (arg.split(":")[0]) {
                case "userId": userId = Long.parseLong(arg.split(":")[1]); break;
                case "username": username = arg.split(":")[1]; break;
                case "password": password = arg.split(":")[1]; break;
                case "nickname": nickname = arg.split(":")[1]; break;
                default: ;
            }
        }
    }

    public Long getUserId() {
        return userId;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    private User() {}
}
