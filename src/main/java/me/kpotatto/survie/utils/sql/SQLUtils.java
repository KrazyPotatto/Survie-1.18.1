package me.kpotatto.survie.utils.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLUtils {

    private Connection connection;

    public SQLUtils(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://172.17.0.1/s10_survie", "u10_Z19088EVUC", "nfLDA0s5ji7Jgbk^hXFmKVYJ");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
