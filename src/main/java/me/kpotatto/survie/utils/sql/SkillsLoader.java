package me.kpotatto.survie.utils.sql;

import me.kpotatto.survie.skills.FightingSkills;
import me.kpotatto.survie.skills.MiningSkills;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class SkillsLoader extends SQLLoader{

    public HashMap<UUID, MiningSkills> uuidMiningSkillsHashMap = new HashMap<>();
    public HashMap<UUID, FightingSkills> uuidFightingSkillsHashMap = new HashMap<>();

    public SkillsLoader(Connection connection, JavaPlugin pl) {
        super(connection, pl);
        load(pl);
    }



    @Override
    protected SkillsLoader load(JavaPlugin pl) {
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM skills");
            while (rs.next()) {
                uuidMiningSkillsHashMap.put(UUID.fromString(rs.getString(1)), new MiningSkills(rs.getInt(2), Bukkit.getOfflinePlayer(UUID.fromString(rs.getString(1)))));
                uuidFightingSkillsHashMap.put(UUID.fromString(rs.getString(1)), new FightingSkills(rs.getInt(3), Bukkit.getOfflinePlayer(UUID.fromString(rs.getString(1)))));
            }
            pl.getLogger().log(Level.INFO, "Loaded " + uuidMiningSkillsHashMap.size() + " user mining skills from database");
            pl.getLogger().log(Level.INFO, "Loaded " + uuidFightingSkillsHashMap.size() + " user fighting skills from database");
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
    public void createPlayer(UUID uuid, String username) {
        try {
            PreparedStatement sts = connection.prepareStatement("INSERT INTO skills (uuid, username) VALUES(?, ?)");
            sts.setString(1, uuid.toString());
            sts.setString(2, username);
            sts.executeUpdate();
            sts.close();
            uuidMiningSkillsHashMap.put(uuid, new MiningSkills(0, Bukkit.getOfflinePlayer(uuid)));
            uuidFightingSkillsHashMap.put(uuid, new FightingSkills(0, Bukkit.getOfflinePlayer(uuid)));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean updatePlayer(UUID uuid) {
        boolean success = false;
        try {
            if(uuidMiningSkillsHashMap.get(uuid) != null) {
                PreparedStatement sts = connection.prepareStatement("UPDATE skills SET mining_exp = ?, fighting_exp = ?, last_update = ? WHERE uuid = ?");
                sts.setInt(1, uuidMiningSkillsHashMap.get(uuid).getExperience());
                sts.setInt(2, uuidFightingSkillsHashMap.get(uuid).getExperience());
                sts.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                sts.setString(4, uuid.toString());
                sts.executeUpdate();
                sts.close();
                success = true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return success;
    }

    public boolean updatePlayer(UUID uuid, String username) {
        boolean success = false;
        try {
            if(uuidMiningSkillsHashMap.get(uuid) != null) {
                PreparedStatement sts = connection.prepareStatement("UPDATE skills SET mining_exp = ?, fighting_exp = ?, last_update = ?, username = ? WHERE uuid = ?");
                sts.setInt(1, uuidMiningSkillsHashMap.get(uuid).getExperience());
                sts.setInt(2, uuidFightingSkillsHashMap.get(uuid).getExperience());
                sts.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                sts.setString(4, username);
                sts.setString(5, uuid.toString());
                sts.executeUpdate();
                sts.close();
                success = true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return success;
    }
}
