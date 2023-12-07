package main.src.DAO;

import main.src.Domain.Chat.Chat;
import main.src.Domain.Chat.ChatDto;
import main.config.CustomException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static main.config.Constant.*;
import static main.config.ResponseStatus.DB_ERROR;
import static main.config.ResponseStatus.ROOMS_NULL;

public class ChatDAO extends DefaultDAO {
    private static ChatDAO instance = new ChatDAO();
    private ChatDAO() {}
    public static ChatDAO getInstance() {
        return instance;
    }
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    public List<ChatDto> loadChat(Chat chat) throws CustomException {
        String query;
        List<ChatDto> list = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(mysqlUrl, mysqlUser, mysqlPassword);

            query = "select cr.roomname, users.nickname, chat.message " +
                    "from UserRooms as ur " +
                    "join Chats as chat on ur.roomId = chat.roomId and ur.userId = chat.userId " +
                    "join Users as users on users.userId = chat.userId " +
                    "join ChatRooms as cr on cr.roomId = ur.roomId " +
                    "where ur.roomId = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, chat.getRoomId());
            resultSet = statement.executeQuery();

            if (!resultSet.next()) throw new CustomException(ROOMS_NULL);

            do {
                list.add(
                        new ChatDto(resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3))
                );
            } while(resultSet.next());

            return list;
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
    public List<ChatDto> sendChat(Chat chat) throws CustomException {
        String query;
        List<ChatDto> list = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(mysqlUrl, mysqlUser, mysqlPassword);

            query = "insert into Chats (roomId, message, userId) values (?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setLong(1, chat.getRoomId());
            statement.setString(2, chat.getMessage());
            statement.setLong(3, chat.getUserId());

            int count = statement.executeUpdate();
            if (count == 0) {
                throw new CustomException(DB_ERROR);
            }

            query = "select ur.userId, cr.roomname, users.nickname " +
                    "from UserRooms as ur " +
                    "join ChatRooms as cr on cr.roomId = ur.roomId " +
                    "join Users as users " +
                    "where ur.roomId = ? and users.userId = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, chat.getRoomId());
            statement.setLong(2, chat.getUserId());
            resultSet = statement.executeQuery();

            while(resultSet.next()) {
                list.add(
                        new ChatDto(
                                resultSet.getLong(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                chat.getMessage()
                        )
                );
            }

            return list;
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
}