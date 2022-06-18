package me.kpotatto.survie;

import me.kpotatto.survie.utils.StartupUtils;
import me.kpotatto.survie.utils.TeleportationRequest;
import me.kpotatto.survie.utils.TpaRequest;
import me.kpotatto.survie.utils.locks.ChestPlayerProfile;
import me.kpotatto.survie.utils.save.LockedChests;
import me.kpotatto.survie.utils.sql.IpLogger;
import me.kpotatto.survie.utils.sql.SQLUtils;
import me.kpotatto.survie.utils.sql.SkillsLoader;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Survie extends JavaPlugin {

    public List<UUID> disabledActionBar = new ArrayList<>();
    public List<Location> placeBlock = new ArrayList<>();
    public Map<UUID, Long> spawnCooldown = new HashMap<>();
    public Map<UUID, Long> tpaCooldown = new HashMap<>();
    public Map<UUID, TeleportationRequest> teleportations = new HashMap<>();
    public List<TpaRequest> tpaRequests = new ArrayList<>();
    public List<TpaRequest> tpaHereRequests = new ArrayList<>();
    public Map<UUID, Integer> joinMessageID = new HashMap<>();
    public Map<UUID, Integer> skillsSaveTaskID = new HashMap<>();

    public Map<UUID, ChestPlayerProfile> lockingChestPlayers = new HashMap<>();
    public List<UUID> autoLockingChest = new ArrayList<>();

    public SQLUtils sqlUtils;
    public SkillsLoader skillsLoader;
    public IpLogger ipLogger;

    private LockedChests lockedChests;

    private static Survie instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        StartupUtils.startUp(this);
        sqlUtils = new SQLUtils();
        skillsLoader = new SkillsLoader(sqlUtils.getConnection(), this);
        ipLogger = new IpLogger(sqlUtils.getConnection());
        lockedChests = LockedChests.load(new LockedChests("lockedChests"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        StartupUtils.shutDown(this);
    }

    public static Survie getInstance(){
        return instance;
    }

    public LockedChests getLockedChests() {
        return lockedChests;
    }
}
