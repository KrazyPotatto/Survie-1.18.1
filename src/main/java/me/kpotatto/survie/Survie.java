package me.kpotatto.survie;

import me.kpotatto.survie.utils.StartupUtils;
import me.kpotatto.survie.utils.TeleportationRequest;
import me.kpotatto.survie.utils.TpaRequest;
import org.bukkit.plugin.java.JavaPlugin;

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

    private static Survie instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        StartupUtils.startUp(this);
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
