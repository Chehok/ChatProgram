package main.config;

import main.Domain.Result;

import java.util.List;

public class Response<T> {
    private String statusCode;
    private String isSuccess;
    private T result; // List<Chat> 일 때는 값을 어떻게 String으로 변환하는 가

    // 요청 성공
    public Response(T result) {
        this.statusCode = "200";
        this.isSuccess = "SUCCESS";
        this.result = result;
    }

    // 요청 실패
    public Response(ResponseStatus status) {
        this.statusCode = status.getStatusCode(); // 201 / 202 / 404
        this.isSuccess = "FAIL"; // FAIL
        this.result = (T) status.getMessage();
    }

    public String getResponse() {
        String response = String.format("%s %s\t", statusCode, isSuccess);
        T results = getResult();
        if (results instanceof String) {
            response += String.format("message:%s", results);
        } else if (results instanceof List) {
            for (Result result : (List<Result>) results) {
                response += String.format("%s/", result.getResult());
                response.trim();
            }
        } else {
            response += String.format("%s", ( (Result) results).getResult());
        }
        return response;
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public T getResult() {
        return result;
    }
}
