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
        registerEnchantments(pl);
    }

    private static void registerEvents(JavaPlugin pl){
        pl.getServer().getPluginManager().registerEvents(new BlockPlaceEvent(), pl);
        pl.getServer().getPluginManager().registerEvents(new DeathEvent(), pl);
        pl.getServer().getPluginManager().registerEvents(new JoinEvent(pl), pl);
        pl.getServer().getPluginManager().registerEvents(new AnvilEvents(), pl);
        pl.getServer().getPluginManager().registerEvents(new EntityDeathEvent(), pl);
        pl.getServer().getPluginManager().registerEvents(new PlayerInteractEvents(), pl);
    }

    private static void registerCommands(JavaPlugin pl){
        Objects.requireNonNull(pl.getCommand("spawn")).setExecutor(new SpawnCommand());
        Objects.requireNonNull(pl.getCommand("tpa")).setExecutor(new TpaCommand());
        Objects.requireNonNull(pl.getCommand("tpahere")).setExecutor(new TpaHereCommand());
        Objects.requireNonNull(pl.getCommand("tpyes")).setExecutor(new TpYesCommand());
        Objects.requireNonNull(pl.getCommand("tpaccept")).setExecutor(new TpYesCommand());
        Objects.requireNonNull(pl.getCommand("tpno")).setExecutor(new TpNoCommand());
        Objects.requireNonNull(pl.getCommand("tpdeny")).setExecutor(new TpNoCommand());
        Objects.requireNonNull(pl.getCommand("ping")).setExecutor(new PingCommand());
        Objects.requireNonNull(pl.getCommand("wiki")).setExecutor(new WikiCommand());
        Objects.requireNonNull(pl.getCommand("toggleactionbar")).setExecutor(new ToggleActionBarCommand());
        Objects.requireNonNull(pl.getCommand("skills")).setExecutor(new SkillsCommand());
        Objects.requireNonNull(pl.getCommand("lc")).setExecutor(new LockCommand());
    }

    private static void registerRunnables(JavaPlugin pl){
        Bukkit.getScheduler().runTaskTimer(pl, new TeleportRunnable(), 0L, 20L);
    }

    private static void registerEnchantments(JavaPlugin pl){
        registerEnchantment(CustomEnchantments.SOULBOUND.getEnchantment(), pl);
        registerEnchantment(CustomEnchantments.AUTO_SMELT.getEnchantment(), pl);
        registerEnchantment(CustomEnchantments.BEHEADING.getEnchantment(), pl);
        registerEnchantment(CustomEnchantments.VAMPIRISM.getEnchantment(), pl);
        registerEnchantment(CustomEnchantments.TELEKINESIS.getEnchantment(), pl);
    }

    public static void shutDown(JavaPlugin pl){
        unregisterEnchantment(CustomEnchantments.SOULBOUND.getEnchantment(), pl);
        unregisterEnchantment(CustomEnchantments.AUTO_SMELT.getEnchantment(), pl);
        unregisterEnchantment(CustomEnchantments.BEHEADING.getEnchantment(), pl);
        unregisterEnchantment(CustomEnchantments.VAMPIRISM.getEnchantment(), pl);
        unregisterEnchantment(CustomEnchantments.TELEKINESIS.getEnchantment(), pl);
    }

    private static void registerEnchantment(Enchantment enchantment, JavaPlugin pl){
        boolean registered = true;
        try{
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            registered = false;
            e.printStackTrace();
        }
        if(registered)
            pl.getLogger().log(Level.INFO, "Enchantment " + enchantment.getName() + " has been registered!");
    }

    private static void unregisterEnchantment(Enchantment enchantment, JavaPlugin pl){
        boolean unregistered = true;
        try {
            Field keyField = Enchantment.class.getDeclaredField("byKey");

            keyField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>) keyField.get(null);

            if(byKey.containsKey(enchantment.getKey())) {
                byKey.remove(enchantment.getKey());
            }
            Field nameField = Enchantment.class.getDeclaredField("byName");

            nameField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) nameField.get(null);

            if(byName.containsKey(enchantment.getName())) {
                byName.remove(enchantment.getName());
            }
        } catch (Exception ignored) { unregistered = false; }
        if(unregistered){
            pl.getLogger().log(Level.INFO, "Enchantment " + enchantment.getName() + " has been unregistered!");
        }
    }

}
