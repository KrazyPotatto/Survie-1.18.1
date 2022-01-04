package me.kpotatto.survie.utils;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class CooldownUtils {

    public static boolean asCooldownExpired(Map<UUID, Long> cooldownMap, Player p){
        if(cooldownMap.containsKey(p.getUniqueId())){
            if(cooldownMap.get(p.getUniqueId()) <= System.currentTimeMillis()){
                cooldownMap.remove(p.getUniqueId());
                return true;
            }else{
                return false;
            }
        }else{
            return true;
        }
    }

}
