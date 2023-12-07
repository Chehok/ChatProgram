package main.src.Service;

import main.src.Dao.RoomDao;
import main.src.Domain.Room.Room;
import main.config.CustomException;
import main.config.CustomResponse;
import main.config.ResponseStatus;

import static main.config.ResponseStatus.BODY_ERROR;
import static main.src.ServerThread.out;

public class RoomService {
    private static RoomService instance = new RoomService();

    private RoomService() {
    }

    public static RoomService getInstance() {
        return instance;
    }

    RoomDao roomDao = RoomDao.getInstance();

    public void loadRoom(String body) {
        CustomResponse customResponse = null;
        Room room = new Room(body);

        try {
            if (room.getUserId() == null) {
                throw new CustomException(BODY_ERROR);
            }
            customResponse = new CustomResponse<>(roomDao.loadRoom(room));
        } catch (CustomException e) {
            customResponse = new CustomResponse<>(e.getStatus());
        } finally {
            out.println(customResponse.getResponse());
            out.flush();
        }
    }

    public void createRoom(String body) {
        CustomResponse customResponse = null;
        Room room = new Room(body);
        try {
            if (room.getUserId() == null || room.getRoomName() == null) {
                throw new CustomException(BODY_ERROR);
            }
            roomDao.createRoom(room);
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
