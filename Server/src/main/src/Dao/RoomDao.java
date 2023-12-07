package main.src.Dao;

import main.src.Domain.Room.Room;
import main.config.CustomException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static main.config.Constant.*;
import static main.config.ResponseStatus.*;

public class RoomDao extends DefaultDao {
    private static RoomDao instance = new RoomDao();

    private RoomDao() {
    }

    public static RoomDao getInstance() {
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
        } catch (SQLException e) { // MySQL 에러
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
        } catch (SQLException e) { // MySQL 에러
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

    public void inviteRoom(Long roomId, List<String> invitedUsers) throws CustomException {
        String query;
        List<Long> userIds = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(mysqlUrl, mysqlUser, mysqlPassword);

            query = "select userId from users where username = ?";
            for (String username : invitedUsers) {
                statement = connection.prepareStatement(query);
                statement.setString(1, username);

                resultSet = statement.executeQuery();
                if (!resultSet.next()) {
                    throw new CustomException(USERS_NULL);
                }
                userIds.add(resultSet.getLong(1));
            }

            // transaction 예정
            query = "insert into RoomUsers (roomId, userId) values (?, ?)";

            for (Long userId : userIds) {
                statement = connection.prepareStatement(query);
                statement.setLong(1, roomId);
                statement.setLong(2, userId);

                int count = statement.executeUpdate();
                if (count == 0) {
                    throw new CustomException(DB_ERROR);
                }
            }

        } catch (SQLException e) { // MySQL 에러
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

    public void exitRoom() {

    }
}
