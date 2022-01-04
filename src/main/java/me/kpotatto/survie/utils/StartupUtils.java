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

public class StartupUtils {

    public static void startUp(JavaPlugin pl){
        registerEvents(pl);
        registerCommands(pl);
        registerRunnables(pl);
        registerEnchantments();
    }

    private static void registerEvents(JavaPlugin pl){
        pl.getServer().getPluginManager().registerEvents(new BlockPlaceEvent(), pl);
        pl.getServer().getPluginManager().registerEvents(new DeathEvent(), pl);
        pl.getServer().getPluginManager().registerEvents(new JoinEvent(), pl);
        pl.getServer().getPluginManager().registerEvents(new AnvilEvents(), pl);
        pl.getServer().getPluginManager().registerEvents(new EntityDeathEvent(), pl);
    }

    private static void registerCommands(JavaPlugin pl){
        pl.getCommand("spawn").setExecutor(new SpawnCommand());
        pl.getCommand("tpa").setExecutor(new TpaCommand());
        pl.getCommand("tpahere").setExecutor(new TpaHereCommand());
        pl.getCommand("tpyes").setExecutor(new TpYesCommand());
        pl.getCommand("tpaccept").setExecutor(new TpYesCommand());
        pl.getCommand("tpno").setExecutor(new TpNoCommand());
        pl.getCommand("tpdeny").setExecutor(new TpNoCommand());
        pl.getCommand("ping").setExecutor(new PingCommand());
        pl.getCommand("soulbound").setExecutor(new SoulboundCommand());
    }

    private static void registerRunnables(JavaPlugin pl){
        Bukkit.getScheduler().runTaskTimer(pl, new TeleportRunnable(), 0L, 20L);
    }

    private static void registerEnchantments(){
        registerEnchantment(CustomEnchantments.SOULBOUND.getEnchantment());
        registerEnchantment(CustomEnchantments.AUTO_SMELT.getEnchantment());
        registerEnchantment(CustomEnchantments.BEHEADING.getEnchantment());
    }

    public static void shutDown(){
        unregisterEnchantment(CustomEnchantments.SOULBOUND.getEnchantment());
        unregisterEnchantment(CustomEnchantments.AUTO_SMELT.getEnchantment());
        unregisterEnchantment(CustomEnchantments.BEHEADING.getEnchantment());
    }

    private static void registerEnchantment(Enchantment enchantment){
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
            System.out.println("§2Success §7> §aEnchantment " + enchantment.getName() + " has been registered!");
    }

    private static void unregisterEnchantment(Enchantment enchantment){
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
            System.out.println("§2Success §7> §aEnchantment §e" + enchantment.getName() + "§a has been unregistered!");
        }
    }

}
