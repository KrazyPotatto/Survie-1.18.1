package me.kpotatto.survie.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportationRequest {

    private int timer;
    private final Location location;
    private final Location lastLocation;
    private Player player;
    private final String message;

    public TeleportationRequest(Location location, Player p, String message){
        this.timer = 5;
        this.location = location;
        this.lastLocation = p.getLocation();
        this.player = p;
        this.message = message;
    }

    public boolean tick(){
        if(timer <= 0){
            teleport();
            return true;
        }
        timer--;
        return hasMoved();
    }

    private boolean hasMoved(){
        if(lastLocation.getX() == player.getLocation().getX() &&
               lastLocation.getZ() == player.getLocation().getZ() &&
               lastLocation.getY() == player.getLocation().getY()){
            return false;
        }
        player.sendMessage("§6TP §7>> §4Error §7> §cYour teleportation request was cancelled because you moved.");
        return true;
    }

    private void teleport(){
        if(!Bukkit.getOnlinePlayers().contains(player)) return;
        player.teleport(location);
        player.sendMessage(message);
    }


}
