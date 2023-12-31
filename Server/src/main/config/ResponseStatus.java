package main.config;

public enum ResponseStatus {
    ID_DUPLICATION("2001", "아이디가 중복됩니다."),
    USERS_NULL("2002", "아이디 또는 비밀번호가 잘못되었습니다."),
    ROOMS_NULL("2003", "해당하는 방이 없습니다."),
    CHATS_NULL("2004", "채팅이 없습니다."),
    ALREADY_INVITED("2005", "이미 초대한 사람입니다."),
    METHOD_ERROR("400", "요청 메서드가 잘못 되었습니다."),
    BODY_ERROR("400", "요청이 잘못 되었습니다."),
    DB_ERROR("500", "DB 에러");

    private final String statusCode;
    private final String message;

    private ResponseStatus(String statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public String getStatusCode() { return statusCode; }
    public String getMessage() { return message; }
}