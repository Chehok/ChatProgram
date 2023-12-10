package main.src.Service;

import main.config.CustomException;
import main.config.CustomResponse;
import main.config.ResponseStatus;
import main.src.DAO.RoomDAO;
import main.src.Domain.Room.Room;
import main.src.Domain.Room.RoomDTO;

import java.io.PrintWriter;
import java.util.List;

import static main.config.ResponseStatus.BODY_ERROR;
import static main.src.MainServer.onlineUser;

public class RoomService {
    private static RoomService instance = new RoomService();

    private RoomService() {
    }

    public static RoomService getInstance() {
        return instance;
    }

    RoomDAO roomDao = RoomDAO.getInstance();

    public CustomResponse loadRoom(String body) {
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
            return customResponse;
        }
    }

    public CustomResponse createRoom(String body) {
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
            return customResponse;
        }
    }

    public CustomResponse inviteRoom(String body) {
        List<RoomDTO> roomDTO;
        CustomResponse customResponse = null;
        PrintWriter sender;
        Room room = new Room(body);

        try {
            if (room.getUserId() == null || room.getRoomId() == null) {
                throw new CustomException(BODY_ERROR);
            }

            roomDTO = roomDao.inviteRoom(room);

            synchronized (onlineUser) {
                customResponse = new CustomResponse<>(
                        String.format("\"%s\"에 \"%s\"님이 들어오셨습니다", roomDTO.get(0).getRoomName(), roomDTO.get(0).getNickname())
                );
                for (RoomDTO r : roomDTO) {
                    if ((sender = onlineUser.get(r.getUserId())) != null) {
                        sender.println(customResponse.getResponse());
                    }
                }
            }

            customResponse = new CustomResponse<>("초대 성공");
        } catch (CustomException e) {
            customResponse = new CustomResponse<>(e.getStatus());
        } finally {
            return customResponse;
        }
    }

    public CustomResponse exitRoom(String body) {
        List<RoomDTO> roomDTO;
        CustomResponse customResponse = null;
        PrintWriter sender;
        Room room = new Room(body);

        try {
            if (room.getUserId() == null || room.getRoomId() == null) {
                throw new CustomException(BODY_ERROR);
            }

            roomDTO = roomDao.exitRoom(room);

            synchronized (onlineUser) {
                customResponse = new CustomResponse<>(
                        String.format("\"%s\"에서 \"%s\"님이 나가셨습니다", roomDTO.get(0).getRoomName(), roomDTO.get(0).getNickname())
                );
                for (RoomDTO r : roomDTO) {
                    if ((sender = onlineUser.get(r.getUserId())) != null) {
                        sender.println(customResponse.getResponse());
                    }
                }
            }
            customResponse = new CustomResponse<>(String.format("\"%s\"에서 나왔습니다.", roomDTO.get(0).getRoomName()));
        } catch (CustomException e) {
            customResponse = new CustomResponse<>(e.getStatus());
        } finally {
            return customResponse;
        }
    }

    public CustomResponse methodError() {
        return new CustomResponse<>(new CustomException(ResponseStatus.METHOD_ERROR));
    }
}
