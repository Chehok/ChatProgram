package main.Service;

import main.Dao.RoomDao;
import main.config.CustomException;
import main.config.ResponseBody;

public class RoomService {
    RoomDao roomDao;

    public ResponseBody<Void> loadRoom () {

    }

    public ResponseBody<String> createRoom(String roomName, Long userId) {
        try{

            roomDao.createRoom(roomName, userId);
            return new ResponseBody<>("방 생성 성공");
        } catch (CustomException e) {
            return new ResponseBody<>(e.getStatus());
        }
    }

    public void inviteRoom() {

    }

    public void exitRoom() {

    }
}
