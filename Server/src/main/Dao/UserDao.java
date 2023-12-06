package main.Dao;

import main.Domain.User.User;
import main.Domain.User.UserDto;
import main.config.CustomException;

import java.sql.*;

import static main.config.Constant.*;
import static main.config.ResponseStatus.*;

public class UserDao {
    private static UserDao instance = new UserDao();
    private UserDao() {}
    public static UserDao getInstance() {
        return instance;
    }
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    public void signUp(User body) throws CustomException {
        String query;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(mysqlUrl, mysqlUser, mysqlPassword);

            // 아이디 중복확인
            query = "select username from users where username = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, body.getUsername());
            resultSet = statement.executeQuery();

            if (resultSet.next()) { // 아이디 중복
                throw new CustomException(ID_DUPLICATION); // 회원가입 불가
            }

            // 회원 정보 저장
            query = "insert into users (username, password, nickname) values (?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, body.getUsername());
            statement.setString(2, body.getPassword());
            statement.setString(3, body.getNickname());

            int count = statement.executeUpdate();
            if (count == 0) {
                throw new CustomException(DB_ERROR);
            }
        } catch (SQLException e) { // MySQL 에러
            throw new CustomException(DB_ERROR);
        } catch (ClassNotFoundException e) {
            throw new CustomException(DB_ERROR);
        } catch (CustomException e){
            throw e;
        }
        finally {
            try {
                if (connection != null && !connection.isClosed()) connection.close();
                if (statement != null && !statement.isClosed()) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public UserDto login(User body) throws CustomException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(mysqlUrl, mysqlUser, mysqlPassword);

            String query = "select userId, nickname from users where username=? AND password=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, body.getUsername());
            statement.setString(2, body.getPassword());

            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new CustomException(USERS_NULL);
            }

            return new UserDto(resultSet.getLong(1), resultSet.getString(2));
        } catch (SQLException e) { // MySQL 에러
            throw new CustomException(DB_ERROR);
        } catch (ClassNotFoundException e) {
            throw new CustomException(DB_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null && !connection.isClosed()) connection.close();
                if (statement != null && !statement.isClosed()) statement.close();
                if (resultSet != null && !resultSet.isClosed()) resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
