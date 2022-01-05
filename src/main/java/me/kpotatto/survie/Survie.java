package me.kpotatto.survie;

import me.kpotatto.survie.utils.StartupUtils;
import me.kpotatto.survie.utils.TeleportationRequest;
import me.kpotatto.survie.utils.TpaRequest;
import me.kpotatto.survie.utils.sql.SQLUtils;
import me.kpotatto.survie.utils.sql.SkillsLoader;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Survie extends JavaPlugin {

    public Map<UUID, Long> spawnCooldown = new HashMap<>();
    public Map<UUID, Long> tpaCooldown = new HashMap<>();
    public Map<UUID, TeleportationRequest> teleportations = new HashMap<>();
    public CopyOnWriteArrayList<TpaRequest> tpaRequests = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<TpaRequest> tpaHereRequests = new CopyOnWriteArrayList<>();
    public HashMap<UUID, Integer> joinMessageID = new HashMap<>();
    public HashMap<UUID, Integer> skillsSaveTaskID = new HashMap<>();

    private SQLUtils sqlUtils;
    public SkillsLoader skillsLoader;

    private static Survie instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        StartupUtils.startUp(this);
        sqlUtils = new SQLUtils();
        skillsLoader = new SkillsLoader(sqlUtils.getConnection()).load();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        StartupUtils.shutDown();
    }

    public static Survie getInstance(){
        return instance;
    }



}
