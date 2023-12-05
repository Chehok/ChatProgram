package main.config;

public class CustomException extends Exception {
    private ResponseStatus status;
    public CustomException(ResponseStatus status) {
        this.status = status;
    }
    public ResponseStatus getStatus() {
        return status;
    }
}
