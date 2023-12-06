package main.Controller;

import main.Service.RoomService;

// path /room
public class RoomController implements DefaultController {
    private static RoomController instance = new RoomController();
    private RoomController() {}
    public static RoomController getInstance() {
        return instance;
    }
    RoomService roomService = RoomService.getInstance();

    @Override
    public void callService(String method, String body) {
        switch (method) {
            case "GET": roomService.loadRoom(body); break;
            case "POST": roomService.createRoom(body); break;
//            case "PATCH": roomService.inviteRoom(body);
//            case "DELETE": roomService.exitRoom(body);
            default: roomService.methodError();
        }
    }
}
