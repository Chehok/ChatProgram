package main.src.Controller;

import main.src.Service.RoomService;

// path /room
public class RoomController implements DefaultController {
    private static RoomController instance = new RoomController();
    private RoomController() {}
    public static RoomController getInstance() {
        return instance;
    }
    RoomService roomService = RoomService.getInstance();

    @Override
    public void callService(String header, String body) {
        switch (header) {
            case "GET": roomService.loadRoom(body); break;
            case "POST": roomService.createRoom(body); break;
//            case "PATCH": roomService.inviteRoom(body);
//            case "DELETE": roomService.exitRoom(body);
            default: roomService.methodError();
        }
    }
}
