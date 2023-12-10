package main.src.Controller;

import main.config.CustomResponse;
import main.src.Service.RoomService;

import java.io.PrintWriter;

public class RoomController implements DefaultController {
    private static RoomController instance = new RoomController();

    private RoomController() {
    }

    public static RoomController getInstance() {
        return instance;
    }

    RoomService roomService = RoomService.getInstance();

    @Override
    public CustomResponse callService(String header, String body, PrintWriter out) {
        return switch (header) {
            case "GET" -> roomService.loadRoom(body);
            case "POST" -> roomService.createRoom(body);
            case "PATCH" -> roomService.inviteRoom(body);
            case "DELETE" -> roomService.exitRoom(body);
            default -> roomService.methodError();
        };
    }
}
