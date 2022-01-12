package me.kpotatto.survie.utils.sql;

import me.kpotatto.survie.skills.FightingSkills;
import me.kpotatto.survie.skills.MiningSkills;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.HashMap;
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

    public void createPlayer(UUID uuid, String ip) {
        try {
            PreparedStatement sts = connection.prepareStatement("INSERT INTO uuid_id (uuid, ip_addr) VALUES(?, ?)");
            sts.setString(1, uuid.toString());
            sts.setString(2, ip);
            sts.executeUpdate();
            sts.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean updatePlayer(UUID uuid, String ip) {
        boolean success = false;
        try {
                PreparedStatement sts = connection.prepareStatement("UPDATE uuid_id SET ip_addr = ? WHERE uuid = ?");
                sts.setString(1, ip);
                sts.setString(2, uuid.toString());
                sts.executeUpdate();
                sts.close();
                success = true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return success;
    }
}
