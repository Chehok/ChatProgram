package main.Domain.User;

public class User {
    private String username;
    private String password;
    private String nickname;

    // "username:username1,password:password1,nickname:nickname1"
    public User(String body) {
        String[] args = body.split(",");
        for (String arg : args) {
            switch (arg.split(":")[0]) {
                case "username": username = arg.split(":")[1];
                case "password": password = arg.split(":")[1];
                case "nickname": nickname = arg.split(":")[1];
                default: ;
            }
        }
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
