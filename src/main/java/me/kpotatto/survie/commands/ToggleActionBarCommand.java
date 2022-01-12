package me.kpotatto.survie.commands;

import me.kpotatto.survie.Survie;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ToggleActionBarCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String cmds, @NotNull String[] args) {
        if(!(s instanceof Player)){
            s.sendMessage("§6Toggle ActionBar §7>> §4Erreur §7> §cVous devez être un joueur pour exécuter cette commande.");
            return true;
        }
        Player p = (Player) s;
        if(Survie.getInstance().disabledActionBar.contains(p.getUniqueId())){
            Survie.getInstance().disabledActionBar.remove(p.getUniqueId());
            p.sendMessage("§6Toggle ActionBar §7>> §2Succès §7> §aVous recevrez désormais des messages en Action Bar");
        }else{
            Survie.getInstance().disabledActionBar.add(p.getUniqueId());
            p.sendMessage("§6Toggle ActionBar §7>> §2Succès §7> §aVous ne recevrez plus de message en Action Bar");
        }
        return true;
    }

}
