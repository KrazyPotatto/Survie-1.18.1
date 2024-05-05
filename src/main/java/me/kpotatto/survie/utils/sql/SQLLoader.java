package me.kpotatto.survie.utils.sql;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public abstract class SQLLoader {

    protected Connection connection;
    protected JavaPlugin pl;

    public SQLLoader(Connection connection, JavaPlugin pl){
        this.connection = connection;
        this.pl = pl;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    protected abstract SkillsLoader load();
    public abstract boolean playerExists(UUID uuid);
    public abstract void createPlayer(UUID uuid, String username);
    public abstract boolean updatePlayer(UUID uuid);

}
