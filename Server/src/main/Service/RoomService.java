package main.Service;

import main.Dao.RoomDao;
import main.Domain.Room.Room;
import main.config.CustomException;
import main.config.Response;
import main.config.ResponseStatus;

import static main.ServerThread.out;

public class RoomService {
    private static RoomService instance = new RoomService();
    private RoomService() {}
    public static RoomService getInstance() {
        return instance;
    }
    RoomDao roomDao = RoomDao.getInstance();

    public void loadRoom (String body) {
//        return null;
        Response response = null;
        try{
            response = new Response<>(roomDao.loadRoom(new Room(body)));
        } catch (CustomException e) {
            response = new Response<>(e.getStatus());
        } finally {
            out.println(response.getResponse());
            out.flush();
        }
    }

    public void createRoom(String body) {
        Response response = null;
        try{
            roomDao.createRoom(new Room(body));
            response = new Response<>("방 생성 성공");
        } catch (CustomException e) {
            response = new Response<>(e.getStatus());
        } finally {
            out.println(response.getResponse());
            out.flush();
        }
    }

    public void inviteRoom() {

    }

    public void exitRoom() {

    }

    public void methodError() {
        out.println(new Response<>(new CustomException(ResponseStatus.METHOD_ERROR)));
        out.flush();
    }
}
