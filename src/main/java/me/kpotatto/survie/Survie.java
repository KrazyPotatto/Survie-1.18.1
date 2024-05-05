package me.kpotatto.survie;

import me.kpotatto.survie.utils.StartupUtils;
import me.kpotatto.survie.utils.TeleportationRequest;
import me.kpotatto.survie.utils.TpaRequest;
import me.kpotatto.survie.utils.sql.IpLogger;
import me.kpotatto.survie.utils.sql.SQLUtils;
import me.kpotatto.survie.utils.sql.SkillsLoader;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Survie extends JavaPlugin {

    public ArrayList<UUID> disabledActionBar = new ArrayList<>();
    public ArrayList<Location> placeBlock = new ArrayList<>();
    public Map<UUID, Long> spawnCooldown = new HashMap<>();
    public Map<UUID, Long> tpaCooldown = new HashMap<>();
    public Map<UUID, TeleportationRequest> teleportations = new HashMap<>();
    public CopyOnWriteArrayList<TpaRequest> tpaRequests = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<TpaRequest> tpaHereRequests = new CopyOnWriteArrayList<>();
    public HashMap<UUID, Integer> joinMessageID = new HashMap<>();
    public HashMap<UUID, Integer> skillsSaveTaskID = new HashMap<>();

    public SQLUtils sqlUtils;
    public SkillsLoader skillsLoader;
    public IpLogger ipLogger;

    private static Survie instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        // Load config file
        saveDefaultConfig();

        // Startup Plugin
        StartupUtils.startUp(this);
        sqlUtils = new SQLUtils(this);
        skillsLoader = new SkillsLoader(sqlUtils.getConnection(), this);
        ipLogger = new IpLogger(sqlUtils.getConnection());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        StartupUtils.shutDown(this);
    }

    public static Survie getInstance(){
        return instance;
    }



}
