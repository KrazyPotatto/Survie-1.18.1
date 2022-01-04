package me.kpotatto.survie.utils;

import me.kpotatto.survie.enchantments.CustomEnchantments;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {

    public static ItemStack soulboundBook(){
        ItemStack is = new ItemStack(Material.ENCHANTED_BOOK);
        is.addEnchantment(CustomEnchantments.SOULBOUND.getEnchantment(), 1);
        List<Component> lores = new ArrayList<>();
        lores.add(Component.text("§r§7Soulbound"));
        lores.add(Component.text("§r§7Conserver cet item quand vous mourrez"));
        lores.add(Component.text("§r§e"));
        ItemMeta im = is.getItemMeta();
        im.lore(lores);
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack autosmeltBook(){
        ItemStack is = new ItemStack(Material.ENCHANTED_BOOK);
        is.addEnchantment(CustomEnchantments.AUTO_SMELT.getEnchantment(), 1);
        List<Component> lores = new ArrayList<>();
        lores.add(Component.text("§r§7Autosmelt"));
        lores.add(Component.text("§r§7Drop les items cuit des blocs que vous cassez"));
        lores.add(Component.text("§r§f"));
        ItemMeta im = is.getItemMeta();
        im.lore(lores);
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack beheadingBook(){
        ItemStack is = new ItemStack(Material.ENCHANTED_BOOK);
        is.addEnchantment(CustomEnchantments.BEHEADING.getEnchantment(), 1);
        List<Component> lores = new ArrayList<>();
        lores.add(Component.text("§r§7Beheading"));
        lores.add(Component.text("§r§7Ajoute 5% de chance de drop une tête de mob"));
        lores.add(Component.text("§r§c"));
        ItemMeta im = is.getItemMeta();
        im.lore(lores);
        is.setItemMeta(im);
        return is;
    }

    public static boolean isWeapon(ItemStack is, boolean axe){
        return is.getType().toString().contains("SWORD") || (
                is.getType().toString().contains("AXE") && !is.getType().toString().contains("PICKAXE") && axe
                );
    }

    public static boolean isTool(ItemStack is){
        return isTool(is, false);
    }

    public static boolean isTool(ItemStack is, boolean hoe){
        if(is.getType().toString().contains("AXE") || is.getType().toString().contains("SHOVEL")){
            return true;
        }else return hoe && is.getType().toString().contains("HOE");
    }

}