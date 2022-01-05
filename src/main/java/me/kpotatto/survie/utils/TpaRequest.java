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
        return new TeleportationRequest(target.getLocation(), sender, "§6TPA §7>> §2Succès §7> §aVous avez été téléporté à §e"+target.getDisplayName() +"§a!");
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
        target.sendMessage("§6TPA §7>> §2Demande acceptée §7> §e" + sender.getDisplayName() + "§a sera téléporté à vous dans 5 secondes!");
        sender.sendMessage("§6TPA §7>> §2Demande acceptée §7> §aVous serez téléporté à §e" + target.getDisplayName() + "§a dans 5 secondes!");
        Survie.getInstance().teleportations.put(sender.getUniqueId(), getTeleportRequest());
    }

    public void deny(){
        target.sendMessage("§6TPA §7>> §4Demande refusée §7> §e" + sender.getDisplayName() + "§c ne sera pas téléporté à vous!");
        sender.sendMessage("§6TPA §7>> §4Demande refusée §7> §cVous ne serez pas téléporté à §e" + target.getDisplayName());
    }

    public void expires(){
        target.sendMessage("§6TPA §7>> §4Demande expirée §7> §cLa demande de téléportation de §e" + sender.getDisplayName() + "§c a expiré!");
        sender.sendMessage("§6TPA §7>> §4Demande expirée §7> §cVotre demande de téléportation vers §e" + target.getDisplayName() + "§c a expiré!");
    }

    public Player getTarget(){
        return target;
    }

    public Player getSender(){
        return sender;
    }

}
