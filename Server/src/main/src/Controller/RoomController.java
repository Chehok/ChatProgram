package main.src.Controller;

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
    public void callService(String header, String body, PrintWriter out) {
        switch (header) {
            case "GET":
                roomService.loadRoom(body, out);
                break;
            case "POST":
                roomService.createRoom(body, out);
                break;
            case "PATCH":
                roomService.inviteRoom(body, out);
                break;
            case "DELETE":
                roomService.exitRoom(body, out);
                break;
            default:
                roomService.methodError(out);
                break;
        }
    }
}
