package me.kpotatto.survie.commands;

import me.kpotatto.survie.Survie;
import me.kpotatto.survie.utils.TpaRequest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpYesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String cmds, @NotNull String[] args) {

        if(!(s instanceof Player)) {
            s.sendMessage("§6Spawn §7>> §4Error §7> §cYou must be a player to use this command!");
            return true;
        }

        Player p = (Player) s;

        for(TpaRequest tpa : Survie.getInstance().tpaRequests){
            if(tpa.getTarget().getUniqueId().equals(p.getUniqueId())){
                tpa.accept();
                Survie.getInstance().tpaRequests.remove(tpa);
                return true;
            }
        }

        for(TpaRequest tpa : Survie.getInstance().tpaHereRequests){
            if(tpa.getSender().getUniqueId().equals(p.getUniqueId())){
                tpa.accept();
                Survie.getInstance().tpaHereRequests.remove(tpa);
                return true;
            }
        }

        p.sendMessage("§6TPA §7>> §4Error §7> §cYou do not have a pending teleportation request!");
        return true;
    }

}
