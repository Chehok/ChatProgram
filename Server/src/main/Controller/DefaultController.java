package main.Controller;

import main.config.ResponseBody;

public interface DefaultController {
    ResponseBody callMethod(String method, String args);
}
