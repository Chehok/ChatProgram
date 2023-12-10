package main.src.Controller;

import java.io.PrintWriter;

public interface DefaultController {
    void callService(String header, String body, PrintWriter out);
}

