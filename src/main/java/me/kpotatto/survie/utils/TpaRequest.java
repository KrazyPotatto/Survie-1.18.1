package me.kpotatto.survie.utils;

import me.kpotatto.survie.Survie;
import org.bukkit.entity.Player;

public class TpaRequest{

    private int delay = 60;
    private final Player sender, target;

    public TpaRequest(Player sender, Player target){
        this.sender = sender;
        this.target = target;
    }

    public TeleportationRequest getTeleportRequest(){
        return new TeleportationRequest(target.getLocation(), sender, "§6TPA §7>> §2Success §7> §aYou were teleported to §e"+target.getDisplayName() +"§a!");
    }

    public boolean tick(){
        if(delay <= 0){
            expires();
            return false;
        }
        delay--;
        return true;
    }

    public void accept(){
        target.sendMessage("§6TPA §7>> §2TPA Accepted §7> §e" + sender.getDisplayName() + "§a will be teleported to you in 5 seconds!");
        sender.sendMessage("§6TPA §7>> §3TPA Accepted §7> §aYou will be teleported to §e" + target.getDisplayName() + "§a in 5 seconds!");
        Survie.getInstance().teleportations.put(sender.getUniqueId(), getTeleportRequest());
    }

    public void deny(){
        target.sendMessage("§6TPA §7>> §4TPA Denied §7> §e" + sender.getDisplayName() + "§c will not be teleported to you!");
        sender.sendMessage("§6TPA §7>> §4TPA Denied §7> §cYou won't be teleported to §e" + target.getDisplayName());
    }

    public void expires(){
        target.sendMessage("§6TPA §7>> §4TPA Expired §7> §e" + sender.getDisplayName() + "§c's teleportation request has expired!");
        sender.sendMessage("§6TPA §7>> §4TPA Expired §7> §cYour teleportation request to §e" + target.getDisplayName() + "§c has expired!");
    }

    public Player getTarget(){
        return target;
    }

    public Player getSender(){
        return sender;
    }

}
