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

    public void loadRoom(String body, PrintWriter out) {
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

    public void createRoom(String body, PrintWriter out) {
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

    public void inviteRoom(String body, PrintWriter out) {
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
                        String.format("\"%s\"에 \"%s\"님이 들어오셨습니다", roomDTO.get(1).getRoomName(), roomDTO.get(1).getNickname())
                );
                for (RoomDTO r : roomDTO) {
                    if ((sender = onlineUser.get(r.getUserId())) != null) { // 온라인이 아닌데도 sender 에 값이 들어가는 오류?
//                        customResponse = new CustomResponse<>(
//                                String.format("\"%s\"에 \"%s\"님이 들어오셨습니다", r.getRoomName(), r.getNickname())
//                        );
                        sender.println(customResponse.getResponse());
                        sender.flush();
                    }
                }
            }

            customResponse = new CustomResponse<>("초대 성공");
        } catch (CustomException e) {
            customResponse = new CustomResponse<>(e.getStatus());
        } finally {
            out.println(customResponse.getResponse());
            out.flush();
        }
    }

    public void exitRoom(String body, PrintWriter out) {
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
                        String.format("\"%s\"에서 \"%s\"님이 나가셨습니다", roomDTO.get(1).getRoomName(), roomDTO.get(1).getNickname())
                );
                for (RoomDTO r : roomDTO) {
                    if ((sender = onlineUser.get(r.getUserId())) != null) {
//                        customResponse = new CustomResponse<>(
//                                String.format("\"%s\"에서 \"%s\"님이 나가셨습니다", r.getRoomName(), r.getNickname())
//                        );
                        sender.println(customResponse.getResponse());
                        sender.flush();
                    }
                }
            }

            customResponse = new CustomResponse<>(String.format("\"%s\"에서 나왔습니다.", roomDTO.get(1).getRoomName()));
        } catch (CustomException e) {
            customResponse = new CustomResponse<>(e.getStatus());
        } finally {
            out.println(customResponse.getResponse());
            out.flush();
        }
    }

    public void methodError(PrintWriter out) {
        out.println(new CustomResponse<>(new CustomException(ResponseStatus.METHOD_ERROR)));
        out.flush();
    }
}
