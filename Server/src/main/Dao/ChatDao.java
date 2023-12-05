package main.Dao;

import main.Domain.Chat.Chat;
import main.config.CustomException;

import java.sql.*;
import java.util.List;

import static main.config.Constant.*;
import static main.config.ResponseStatus.DB_ERROR;

public class ChatDao {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    public List<Chat> loadChat(Long roomId) throws CustomException {
        String query;
        List<Chat> list = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(mysqlUrl, mysqlUser, mysqlPassword);

            query = "select (roomId, userId) from Chats where roomId = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, roomId);
            resultSet = statement.executeQuery();

            while(resultSet.next()) {
                list.add(new Chat(resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getLong(3)));
            }

            return list;
        } catch (SQLException e) { // MySQL 에러
            throw new CustomException(DB_ERROR);
        } catch (ClassNotFoundException e) {
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
    public void sendChat(Long roomId, String message, Long userId) throws CustomException {
        String query;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(mysqlUrl, mysqlUser, mysqlPassword);

            // 채팅방 생성
            query = "insert into Chats (roomId, message, userId) values (?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setLong(1, roomId);
            statement.setString(2, message);
            statement.setLong(3, userId);

            int count = statement.executeUpdate();
            if (count == 0) {
                throw new CustomException(DB_ERROR);
            }
        } catch (SQLException e) { // MySQL 에러
            throw new CustomException(DB_ERROR);
        } catch (ClassNotFoundException e) {
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
