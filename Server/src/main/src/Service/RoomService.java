package main.src.Service;

import main.src.Dao.RoomDao;
import main.src.Domain.Room.Room;
import main.config.CustomException;
import main.config.CustomResponse;
import main.config.ResponseStatus;

import static main.src.ServerThread.out;

public class RoomService {
    private static RoomService instance = new RoomService();
    private RoomService() {}
    public static RoomService getInstance() {
        return instance;
    }
    RoomDao roomDao = RoomDao.getInstance();

    public void loadRoom (String body) {
//        return null;
        CustomResponse customResponse = null;
        try{
            customResponse = new CustomResponse<>(roomDao.loadRoom(new Room(body)));
        } catch (CustomException e) {
            customResponse = new CustomResponse<>(e.getStatus());
        } finally {
            out.println(customResponse.getResponse());
            out.flush();
        }
    }

    public void createRoom(String body) {
        CustomResponse customResponse = null;
        try{
            roomDao.createRoom(new Room(body));
            customResponse = new CustomResponse<>("방 생성 성공");
        } catch (CustomException e) {
            customResponse = new CustomResponse<>(e.getStatus());
        } finally {
            out.println(customResponse.getResponse());
            out.flush();
        }
    }

    public void inviteRoom() {

    }

    public void exitRoom() {

    }

    public void methodError() {
        out.println(new CustomResponse<>(new CustomException(ResponseStatus.METHOD_ERROR)));
        out.flush();
    }
}
