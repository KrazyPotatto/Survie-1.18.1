package me.kpotatto.survie.runnable;

import me.kpotatto.survie.Survie;
import me.kpotatto.survie.utils.TeleportationRequest;
import me.kpotatto.survie.utils.TpaRequest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class TeleportRunnable implements Runnable {

    @Override
    public void run() {
        for (Map.Entry<UUID, TeleportationRequest> e: Survie.getInstance().teleportations.entrySet())
            if(e.getValue().tick()) Survie.getInstance().teleportations.remove(e.getKey());
        for (TpaRequest tpa : Survie.getInstance().tpaRequests) if(!tpa.tick()) Survie.getInstance().tpaRequests.remove(tpa);
        for (TpaRequest tpa : Survie.getInstance().tpaHereRequests) if(!tpa.tick()) Survie.getInstance().tpaHereRequests.remove(tpa);
    }

}
