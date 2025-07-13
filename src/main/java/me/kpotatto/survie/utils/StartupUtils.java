package me.kpotatto.survie.utils;

import me.kpotatto.survie.commands.*;
import me.kpotatto.survie.enchantments.CustomEnchantments;
import me.kpotatto.survie.events.*;
import me.kpotatto.survie.runnable.TeleportRunnable;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;

public class StartupUtils {

    public static void startUp(JavaPlugin pl){
        registerEvents(pl);
        registerCommands(pl);
        registerRunnables(pl);
    }

    private static void registerEvents(JavaPlugin pl){
        pl.getServer().getPluginManager().registerEvents(new BlockPlaceEvent(), pl);
        pl.getServer().getPluginManager().registerEvents(new DeathEvent(), pl);
        pl.getServer().getPluginManager().registerEvents(new JoinEvent(pl), pl);
        pl.getServer().getPluginManager().registerEvents(new AnvilEvents(), pl);
        pl.getServer().getPluginManager().registerEvents(new EntityDeathEvent(), pl);
    }

    private static void registerCommands(JavaPlugin pl){
        pl.getServer().getCommandMap().register("spawn", new SpawnCommand("spawn"));
        pl.getServer().getCommandMap().register("tpa", new TpaCommand("tpa"));
        pl.getServer().getCommandMap().register("tpahere", new TpaHereCommand("tpahere"));
        pl.getServer().getCommandMap().register("tpyes", new TpYesCommand("tpyes"));
        pl.getServer().getCommandMap().register("tpaccept", new TpYesCommand("tpaccept"));
        pl.getServer().getCommandMap().register("tpno", new TpNoCommand("tpno"));
        pl.getServer().getCommandMap().register("tpdeny", new TpNoCommand("tpdeny"));
        pl.getServer().getCommandMap().register("ping", new PingCommand("ping"));
        pl.getServer().getCommandMap().register("wiki", new WikiCommand("wiki"));
        pl.getServer().getCommandMap().register("toggleactionbar", new ToggleActionBarCommand("toggleactionbar"));
        pl.getServer().getCommandMap().register("skills", new SkillsCommand("skills"));
    }

    private static void registerRunnables(JavaPlugin pl){
        Bukkit.getScheduler().runTaskTimer(pl, new TeleportRunnable(), 0L, 20L);
    }


    public static void shutDown(JavaPlugin pl){
    }

}
