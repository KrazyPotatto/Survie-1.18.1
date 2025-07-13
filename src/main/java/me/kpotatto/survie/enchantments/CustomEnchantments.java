package me.kpotatto.survie.enchantments;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import org.bukkit.enchantments.Enchantment;

public enum CustomEnchantments {

    SOULBOUND("survie:soulbound"),
    AUTO_SMELT("survie:autosmelt"),
    BEHEADING("survie:beheading"),
    VAMPIRISM("survie:vampirism"),
    TELEKINESIS("survie:telekinesis");

    private final String key;
    CustomEnchantments(String key){
        this.key = key;
    }

    public Enchantment enchantment(){
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(key());
    }

    public Key key(){
        return Key.key(this.key);
    }

}
