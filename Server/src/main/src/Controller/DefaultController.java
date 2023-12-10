package main.src.Controller;

import main.config.CustomResponse;

import java.io.PrintWriter;

public interface DefaultController {
    CustomResponse callService(String header, String body, PrintWriter out);
}

