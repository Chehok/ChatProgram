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
        switch (header) {
            case "GET": return roomService.loadRoom(body);
            case "POST": return roomService.createRoom(body);
            case "PATCH": return roomService.inviteRoom(body);
            case "DELETE": return roomService.exitRoom(body);
            default: return roomService.methodError();
        }
    }
}
