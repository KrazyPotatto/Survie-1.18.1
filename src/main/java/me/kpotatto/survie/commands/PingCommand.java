package me.kpotatto.survie.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String cmds, @NotNull String[] args) {
        Player p;
        boolean same = false;
        if(s instanceof Player){
            //Un joueur utilise la commande
            if(args.length >= 1){
                if(!isPlayerOnline(args[0])){
                    s.sendMessage("§6Ping §7>> §4Erreur §7> §cLe joueur spécifié est hors ligne");
                    return true;
                }
                p = Bukkit.getPlayerExact(args[0]);
            }else{
                p = (Player) s;
                same = true;
            }
        }else{
            //La console utilise la commande
            if(args.length >= 1){
                if(!isPlayerOnline(args[0])){
                    s.sendMessage("§6Ping §7>> §4Erreur §7> §cLe joueur spécifié est hors ligne");
                    return true;
                }
                p = Bukkit.getPlayerExact(args[0]);
            }else{
                s.sendMessage("§6Ping §7>> §4Erreur §7> §cVous devez spécifier un joueur");
                return true;
            }
        }

        if(same){
            s.sendMessage("§6Ping §7>> §2Succès §7> §aVotre ping est de §e" + p.getPing() + "§ams.");
        }else{
            s.sendMessage("§6Ping §7>> §2Succès §7> §aLe ping de §e" + p.getDisplayName() + "§a est de §e" + p.getPing() + "§ams.");
        }

        return false;
    }

    private boolean isPlayerOnline(String pseudo){
        return Bukkit.getPlayerExact(pseudo) != null;
    }

}
