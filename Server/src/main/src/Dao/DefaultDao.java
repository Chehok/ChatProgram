package main.src.Dao;

import main.config.CustomException;

import static main.config.ResponseStatus.DB_ERROR;

public class DefaultDao {
    public void init() throws CustomException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new CustomException(DB_ERROR);
        }
    }
}
