package me.kpotatto.survie.commands;

import me.kpotatto.survie.utils.ItemUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SoulboundCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String cmds, @NotNull String[] args) {
        if(!(s instanceof Player)) return false;

        Player p = (Player) s;
        if(!p.hasPermission("survie.admin")) return false;

        if(args.length != 1) return false;

        if(args[0].equalsIgnoreCase("autosmelt")){
            p.getInventory().addItem(ItemUtils.autosmeltBook());
        }else if(args[0].equalsIgnoreCase("soulbound")){
            p.getInventory().addItem(ItemUtils.soulboundBook());
        }else if(args[0].equalsIgnoreCase("beheading")){
            p.getInventory().addItem(ItemUtils.beheadingBook());
        }

        return false;
    }
}
