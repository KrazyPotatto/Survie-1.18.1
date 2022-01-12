package me.kpotatto.survie.utils.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public abstract class SQLLoader {

    protected Connection connection;

    public SQLLoader(Connection connection){
        this.connection = connection;
    }

    protected abstract SkillsLoader load();
    public abstract boolean playerExists(UUID uuid);
    public abstract void createPlayer(UUID uuid);
    public abstract boolean updatePlayer(UUID uuid);

}
