package me.kpotatto.survie.utils.sql;

import me.kpotatto.survie.Survie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLUtils {

    private Connection connection;

    public SQLUtils(){
        initializeConnection();
    }

    private void initializeConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://172.17.0.1:3306/s10_survie?autoReconnect=true", "u10_Z19088EVUC", "nfLDA0s5ji7Jgbk^hXFmKVYJ");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void checkConnection(){
        try {
            if (connection != null && !connection.isClosed() && !connection.isValid(0)) {
                connection = null;
                initializeConnection();
                Survie.getInstance().skillsLoader.setConnection(this.connection);
                Survie.getInstance().ipLogger.setConnection(this.connection);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
