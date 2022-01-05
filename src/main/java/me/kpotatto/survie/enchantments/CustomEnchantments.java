package me.kpotatto.survie.enchantments;

import org.bukkit.enchantments.Enchantment;

public enum CustomEnchantments {

    SOULBOUND(new SoulboundEnchant()),
    AUTO_SMELT(new AutoSmeltEnchant()),
    BEHEADING(new BeheadingEnchant()),
    VAMPIRISM(new VampirsmEnchant()),
    TELEKINESIS(new TelekinesisEnchant());

    private final CustomEnchantmentWrapper enchant;
    CustomEnchantments(CustomEnchantmentWrapper enchant){
        this.enchant = enchant;
    }

    public Enchantment getEnchantment(){
        return enchant;
    }

}
