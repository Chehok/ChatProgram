package main.config;

import main.src.Domain.DefaultDomain;

import java.util.List;

public class CustomRequest<T> {
    private String method;
    private String path;
    private T body;
    public CustomRequest(String method, String path, T body) {
        this.method = method;
        this.path = path;
        this.body = body;
    }

    public String getRequest() {
        String request = String.format("%s %s\t", method, path);
        if (body instanceof String) {
            request += String.format("%s", body);
        } else if (body instanceof List) {
            for (DefaultDomain result : (List<DefaultDomain>) body) {
                request += String.format("%s/", result.getDomain());
            }
            // 문자열의 마지막 인덱스에 해당하는 부분을 잘라줌.
            // 결과: 마지막에 들어간 구분자 / 를 제거해줌.
            request = request.substring(0, request.length() - 1);
        } else {
            request += String.format("%s", ( (DefaultDomain) body).getDomain());
        }

        return request;
    }
}
