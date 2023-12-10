package main.src.DAO;

import main.config.CustomException;
import main.src.Domain.Room.Room;
import main.src.Domain.Room.RoomDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static main.config.Constant.*;
import static main.config.ResponseStatus.*;

public class RoomDAO extends DefaultDAO {
    private static RoomDAO instance = new RoomDAO();

    private RoomDAO() {
    }

    public static RoomDAO getInstance() {
        return instance;
    }

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    public List<Room> loadRoom(Room room) throws CustomException {
        String query;
        List<Room> list = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(mysqlUrl, mysqlUser, mysqlPassword);

            query = "select room.roomId, room.roomName " +
                    "from ChatRooms as room " +
                    "join UserRooms as ur " +
                    "on room.roomId = ur.roomId " +
                    "where ur.userId = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, room.getUserId());
            resultSet = statement.executeQuery();

            if (!resultSet.next()) throw new CustomException(ROOMS_NULL);

            do {
                list.add(new Room(resultSet.getLong(1), resultSet.getString(2)));
            } while (resultSet.next());

            return list;
        } catch (SQLException e) {
            throw new CustomException(DB_ERROR);
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (connection != null && !connection.isClosed()) connection.close();
                if (statement != null && !statement.isClosed()) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void createRoom(Room room) throws CustomException {
        String query;
        try {
            connection = DriverManager.getConnection(mysqlUrl, mysqlUser, mysqlPassword);

            // 채팅방 생성
            query = "insert into chatrooms (roomName) values (?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, room.getRoomName());

            int count = statement.executeUpdate();
            if (count == 0) {
                throw new CustomException(DB_ERROR);
            }

            // 채팅방 PK 조회
            query = "SELECT LAST_INSERT_ID()";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new CustomException(DB_ERROR);
            }
            Long roomId = resultSet.getLong(1);

            // 채팅방 : 생성자 매핑
            query = "insert into userrooms (roomId, userId) values (?, ?)";
            statement = connection.prepareStatement(query);
            statement.setLong(1, roomId);
            statement.setLong(2, room.getUserId());

            count = statement.executeUpdate();
            if (count == 0) {
                throw new CustomException(DB_ERROR);
            }
        } catch (SQLException e) {
            throw new CustomException(DB_ERROR);
        } finally {
            try {
                if (connection != null && !connection.isClosed()) connection.close();
                if (statement != null && !statement.isClosed()) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<RoomDTO> inviteRoom(Room room) throws CustomException {
        String query;
        List<RoomDTO> list = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(mysqlUrl, mysqlUser, mysqlPassword);
            query = "select * from UserRooms where roomId = ? and userId = ?";

            statement = connection.prepareStatement(query);
            statement.setLong(1, room.getRoomId());
            statement.setLong(2, room.getUserId());

            resultSet = statement.executeQuery();

            if(resultSet.next()) throw new CustomException(ALREADY_INVITED);

            // transaction 예정
            query = "insert into UserRooms (roomId, userId) values (?, ?)";

            statement = connection.prepareStatement(query);
            statement.setLong(1, room.getRoomId());
            statement.setLong(2, room.getUserId());

            int count = statement.executeUpdate();
            if (count == 0) {
                throw new CustomException(DB_ERROR);
            }

            query = "select cr.roomName, users.nickName, ur.userId " +
                    "from UserRooms as ur " +
                    "join ChatRooms as cr on cr.roomId = ur.roomId " +
                    "join Users as users " +
                    "where ur.roomId = ? and users.userId = ?";

            statement = connection.prepareStatement(query);
            statement.setLong(1, room.getRoomId());
            statement.setLong(2, room.getUserId());

            resultSet = statement.executeQuery();

            if(!resultSet.next()) throw new CustomException(DB_ERROR);
            do {
                list.add(new RoomDTO(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getLong(3))
                );
            } while (resultSet.next());

            return list;
        } catch (SQLException e) {
            throw new CustomException(DB_ERROR);
        } finally {
            try {
                if (connection != null && !connection.isClosed()) connection.close();
                if (statement != null && !statement.isClosed()) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<RoomDTO> exitRoom(Room room) throws CustomException {
        String query;
        List<RoomDTO> list = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(mysqlUrl, mysqlUser, mysqlPassword);

            query = "delete from UserRooms where roomId = ? and userId = ?";

            statement = connection.prepareStatement(query);
            statement.setLong(1, room.getRoomId());
            statement.setLong(2, room.getUserId());

            int count = statement.executeUpdate();
            if (count == 0) {
                throw new CustomException(DB_ERROR);
            }

            // transaction 예정
            query = "select cr.roomName, users.nickName, ur.userId " +
                    "from UserRooms as ur " +
                    "join ChatRooms as cr on cr.roomId = ur.roomId " +
                    "join Users as users " +
                    "where ur.roomId = ? and users.userId = ?";

            statement = connection.prepareStatement(query);
            statement.setLong(1, room.getRoomId());
            statement.setLong(2, room.getUserId());

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                list.add(new RoomDTO(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getLong(3))
                );
            }

            return list;
        } catch (SQLException e) {
            throw new CustomException(DB_ERROR);
        } finally {
            try {
                if (connection != null && !connection.isClosed()) connection.close();
                if (statement != null && !statement.isClosed()) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
