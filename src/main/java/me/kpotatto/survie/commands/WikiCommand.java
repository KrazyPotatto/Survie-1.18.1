package me.kpotatto.survie.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WikiCommand extends BukkitCommand {

    public WikiCommand(@NotNull String name) {
        super(name);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        TextComponent text = new TextComponent("Le wiki est accessible sur: https://wiki.kpotatto.net");
        text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://wiki.kpotatto.net"));
        text.setColor(ChatColor.GOLD);
        sender.sendMessage(text);
        return false;
    }

}
