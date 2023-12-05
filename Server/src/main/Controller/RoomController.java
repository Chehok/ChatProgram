package main.Controller;

import main.config.ResponseBody;

// path /room
public class RoomController implements DefaultController {
    private static RoomController instance = new RoomController();
    private RoomController() {}
    public static RoomController getInstance() {
        return instance;
    }

    @Override
    public ResponseBody callMethod(String method, String body) {
        switch (method) {
//            case "GET": ;
//            case "POST": signUp(body);
//            case "PATCH": logout(body);
//            case "DELETE": deleteUser(body);
            default: return null;
        }
    }

    // POST
    public void createRoom() {

    }

    // PATCH
    public void inviteRoom() {

    }

    // DELETE
    public void exitRoom() {

    }
}
