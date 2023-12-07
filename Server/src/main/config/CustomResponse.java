package main.config;

import main.src.Domain.Result;

import java.util.List;

public class CustomResponse<T> {
    private String statusCode;
    private String isSuccess;
    private T result; // List<Chat> 일 때는 값을 어떻게 String으로 변환하는 가

    // 요청 성공
    public CustomResponse(T result) {
        this.statusCode = "200";
        this.isSuccess = "SUCCESS";
        this.result = result;
    }

    // 요청 실패
    public CustomResponse(ResponseStatus status) {
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
            }
            // 문자열의 마지막 인덱스에 해당하는 부분을 잘라줌.
            // 결과: 마지막에 들어간 구분자 / 를 제거해줌.
            response = response.substring(0, response.length() - 1);
        } else if (results instanceof Result){
            response += String.format("%s", ((Result) results).getResult());
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
