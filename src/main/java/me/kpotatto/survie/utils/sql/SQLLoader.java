package me.kpotatto.survie.utils.sql;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.util.UUID;

public abstract class SQLLoader {

    protected Connection connection;

    public SQLLoader(Connection connection, JavaPlugin pl){
        this.connection = connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    protected abstract SkillsLoader load(JavaPlugin pl);
    public abstract boolean playerExists(UUID uuid);
    public abstract void createPlayer(UUID uuid, String username);
    public abstract boolean updatePlayer(UUID uuid);

}
