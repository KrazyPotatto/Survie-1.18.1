package me.kpotatto.survie.utils.sql;

import java.sql.*;
import java.util.UUID;

public class IpLogger{

    Connection connection;

    public IpLogger(Connection connection) {
        this.connection = connection;
    }

    public boolean playerExists(UUID uuid){
        boolean exists = true;
        try {
            PreparedStatement sts = connection.prepareStatement("SELECT * FROM uuid_id WHERE uuid = ?");
            sts.setString(1, uuid.toString());
            ResultSet rs = sts.executeQuery();
            exists = rs.next();
            rs.close();
            sts.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return exists;
    }

    public void createPlayer(UUID uuid, String ip, boolean online) {
        try {
            PreparedStatement sts = connection.prepareStatement("INSERT INTO uuid_id (uuid, ip_addr, online) VALUES(?, ?, ?)");
            sts.setString(1, uuid.toString());
            sts.setString(2, ip);
            sts.setBoolean(3, online);
            sts.executeUpdate();
            sts.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean updatePlayer(UUID uuid, String ip, boolean online) {
        boolean success = false;
        try {
                PreparedStatement sts = connection.prepareStatement("UPDATE uuid_id SET ip_addr = ?, online = ? WHERE uuid = ?");
                sts.setString(1, ip);
                sts.setBoolean(2, online);
                sts.setString(3, uuid.toString());
                sts.executeUpdate();
                sts.close();
                success = true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return success;
    }

    public boolean setOnline(UUID uuid, boolean online){
        boolean success = false;
        try{
            PreparedStatement sts = connection.prepareStatement("UPDATE uuid_id SET online = ? WHERE uuid = ?");
            sts.setBoolean(1, online);
            sts.setString(2, uuid.toString());
            sts.executeUpdate();
            sts.close();
            success = true;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return success;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
