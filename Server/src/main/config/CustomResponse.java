package main.config;

import main.src.Domain.Result;

import java.util.List;

public class CustomResponse<T> {
    private String statusCode;
    private String isSuccess;
    private T result;

    // 요청 성공
    public CustomResponse(T result) {
        this.statusCode = "200";    // 성공 코드는 200으로 통일한다.
        this.isSuccess = "SUCCESS"; // 성공 메세지는 SUCCESS로 통일한다.
        this.result = result;       // 성공 리턴 값
    }

    // 요청 실패
    public CustomResponse(ResponseStatus status) {
        this.statusCode = status.getStatusCode(); // 401, 404, 500 etc..
        this.isSuccess = "FAIL";                  // 실패 메세지는 FAIL 로 통일한다.
        this.result = (T) status.getMessage();    // 실패 상세설명
    }

    /**
     * 성공 결과를 문자열로 변환해주는 메서드
     * 헤더와 바디를 "\t" 구분자로,
     * 바디가 List 값이면 "/" 구분자로,
     * 각 필드들은 "," 구분자로
     * 필드의 Key-Value 값은 ":" 구분자로 나눈다.
     * 성공 예시) "200 SUCCESS\tuserId:1,nickname:Chehok"
     * 실패 예시) "400 FAIL\tmessage:요청이 잘못 되었습니다."
     */
    public String getResponse() {
        String response = String.format("%s %s\t", statusCode, isSuccess);
//        T results = getResult();
        if (result instanceof String) {
            response += String.format("message:%s", result);
        } else if (result instanceof List) {
            for (Result result : (List<Result>) result) {
                response += String.format("%s/", result.getResult());
            }
            // 문자열의 마지막 인덱스에 해당하는 부분을 잘라줌.
            // 결과: 마지막에 들어간 구분자 / 를 제거해줌.
            response = response.substring(0, response.length() - 1);
        } else if (result instanceof Result){
            response += String.format("%s", ((Result) result).getResult());
        }
        return response;
    }

    public T getResult() {
        return result;
    }
}
