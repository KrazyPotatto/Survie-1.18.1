package me.kpotatto.survie.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WikiCommand implements @Nullable CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        TextComponent text = new TextComponent("Le wiki est accessible sur: https://delisle.fortiddns.com/wiki");
        text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://delisle.fortiddns.com/wiki"));
        text.setColor(ChatColor.GOLD);
        sender.sendMessage(text);
        return false;
    }

}
