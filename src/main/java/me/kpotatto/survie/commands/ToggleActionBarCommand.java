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
            s.sendMessage("§6Toggle ActionBar §7>> §4Error §7> §cYou need to be a player to execute this command.");
            return true;
        }
        Player p = (Player) s;
        if(Survie.getInstance().disabledActionBar.contains(p.getUniqueId())){
            Survie.getInstance().disabledActionBar.remove(p.getUniqueId());
            p.sendMessage("§6Toggle ActionBar §7>> §2Success §7> §aAction Bar messages are now enabled");
        }else{
            Survie.getInstance().disabledActionBar.add(p.getUniqueId());
            p.sendMessage("§6Toggle ActionBar §7>> §2Success §7> §aAction Bar messages are now disabled");
        }
        return true;
    }

}
