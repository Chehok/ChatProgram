package main.config;

public class ResponseBody<T> {
    private String isSuccess;
    private String statusCode;
    private T result;

    // 요청 성공
    public ResponseBody(T result) {
        this.isSuccess = "SUCCESS";
        this.statusCode = "200";
        this.result = result;
    }

    // 요청 실패
    public ResponseBody(ResponseStatus status) {
        this.isSuccess = "FAIL"; // FAIL
        this.statusCode = status.getStatusCode(); // 201 / 202 / 404
        this.result = (T) status.getMessage();
    }

    public String getIsSuccess() { return isSuccess; }
    public String getStatusCode() { return statusCode; }
    public T getResult() { return result; }
}
