package me.kpotatto.survie.utils.sql;

import me.kpotatto.survie.Survie;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLUtils {

    private Connection connection;
    private JavaPlugin pl;

    public SQLUtils(JavaPlugin pl){
        this.pl = pl;
        initializeConnection();
    }

    private void initializeConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");

            String uri = "jdbc:" +
                    pl.getConfig().getString("database.driver") +
                    "://" + pl.getConfig().getString("database.host") +
                    pl.getConfig().getString("database.params");

            connection = DriverManager.getConnection(
                    uri,
                    pl.getConfig().getString("database.user"),
                    pl.getConfig().getString("database.password")
            );
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
