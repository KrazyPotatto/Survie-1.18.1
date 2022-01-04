package me.kpotatto.survie.enchantments;

import me.kpotatto.survie.Survie;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

public abstract class CustomEnchantmentWrapper extends Enchantment {

    public CustomEnchantmentWrapper(@NotNull String key) {
        super(new NamespacedKey(Survie.getInstance(), key));
    }
}
