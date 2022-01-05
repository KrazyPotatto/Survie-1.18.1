package me.kpotatto.survie.utils.sql;

import me.kpotatto.survie.skills.MiningSkills;

import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

public class SkillsLoader extends SQLLoader{

    public static HashMap<UUID, MiningSkills> uuidMiningSkillsHashMap = new HashMap<>();

    public SkillsLoader(Connection connection) {
        super(connection);
        load();
    }

    @Override
    public SkillsLoader load() {
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM skills");
            while (rs.next()) {
                uuidMiningSkillsHashMap.put(UUID.fromString(rs.getString(0)), new MiningSkills(rs.getInt(1)));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public boolean playerExists(UUID uuid){
        boolean exists = true;
        try {
            PreparedStatement sts = connection.prepareStatement("SELECT * FROM skills WHERE uuid = ?");
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

    @Override
    public void createPlayer(UUID uuid) {
        try {
            PreparedStatement sts = connection.prepareStatement("INSERT INTO skills (uuid) VALUES(?)");
            sts.setString(1, uuid.toString());
            sts.executeUpdate();
            sts.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean updatePlayer(UUID uuid) {
        boolean success = false;
        try {
            PreparedStatement sts = connection.prepareStatement("UPDATE skills SET mining_exp = ?, last_update = ? WHERE uuid = ?");
            sts.setInt(1, uuidMiningSkillsHashMap.get(uuid).getExperience());
            sts.setDate(2, new Date(System.currentTimeMillis()));
            sts.setString(3, uuid.toString());
            sts.executeUpdate();
            sts.close();
            success = true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return success;
    }
}
