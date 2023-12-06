package main.config;

public class CustomRequest<T> {
    private String method;
    private String path;
    private T body;
    public CustomRequest(String method, String path, T body) {
        this.method = method;
        this.path = path;
        this.body = body;
    }

//    public String getRequest() {
//        String request = "";
//        T body = getResult();
//        if (body instanceof String) {
//            request = String.format("%s %s\t%s", method, path, body);
//        } else if (body instanceof List) {
//            for (Result result : (List<Result>) results) {
//                request += String.format("%s/", result.getResult());
//            }
//            // 문자열의 마지막 인덱스에 해당하는 부분을 잘라줌.
//            // 결과: 마지막에 들어간 구분자 / 를 제거해줌.
//            request = request.substring(0, request.length() - 1);
//        } else {
//            request += String.format("%s", ( (Result) results).getResult());
//        }
//
//        return request;
//    }

    public T getResult() { return body; }
}
