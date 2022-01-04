package me.kpotatto.survie.events;

import me.kpotatto.survie.enchantments.CustomEnchantments;
import me.kpotatto.survie.utils.ItemUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AnvilEvents implements Listener {

    @EventHandler
    public void onAnvilEvent(PrepareAnvilEvent e){
        if(e.getInventory().getSecondItem() != null &&
                e.getInventory().getSecondItem().getEnchantments().containsKey(CustomEnchantments.SOULBOUND.getEnchantment()) &&
                e.getInventory().getSecondItem().getType().equals(Material.ENCHANTED_BOOK)) addSoulbound(e);
        if(e.getInventory().getSecondItem() != null &&
                e.getInventory().getSecondItem().getEnchantments().containsKey(CustomEnchantments.AUTO_SMELT.getEnchantment()) &&
                e.getInventory().getSecondItem().getType().equals(Material.ENCHANTED_BOOK)) addAutosmelt(e);
        if(e.getInventory().getSecondItem() != null &&
                e.getInventory().getSecondItem().getEnchantments().containsKey(CustomEnchantments.BEHEADING.getEnchantment()) &&
                e.getInventory().getSecondItem().getType().equals(Material.ENCHANTED_BOOK)) addBeheading(e);
        if(e.getInventory().getFirstItem() != null && e.getInventory().getResult() != null) keepCustomEnchantments(e);

    }

    private void addSoulbound(PrepareAnvilEvent e){
        if(e.getInventory().getFirstItem() == null) return;
        ItemStack result = e.getInventory().getFirstItem().clone();
        ItemMeta rm = result.getItemMeta();
        List<Component> lores = rm.lore() == null ? new ArrayList<>() : rm.lore();
        lores.add(Component.text("§r§7Soulbound"));
        lores.add(Component.text("§r§7Conserver cet item quand vous mourrez"));
        lores.add(Component.text("§r§e"));
        rm.lore(lores);
        result.setItemMeta(rm);
        result.addEnchantment(CustomEnchantments.SOULBOUND.getEnchantment(), 1);
        e.setResult(result);
        e.getInventory().setRepairCost(35);
    }
    private void addAutosmelt(PrepareAnvilEvent e){
        if(e.getInventory().getFirstItem() == null) return;
        ItemStack result = e.getInventory().getFirstItem().clone();
        if(!ItemUtils.isTool(result)) return;
        ItemMeta rm = result.getItemMeta();
        List<Component> lores = rm.lore() == null ? new ArrayList<>() : rm.lore();
        lores.add(Component.text("§r§7Autosmelt"));
        lores.add(Component.text("§r§7Drop les items cuit des blocs que vous cassez"));
        lores.add(Component.text("§r§f"));
        rm.lore(lores);
        result.setItemMeta(rm);
        result.addEnchantment(CustomEnchantments.AUTO_SMELT.getEnchantment(), 1);
        e.setResult(result);
        e.getInventory().setRepairCost(15);
    }
    private void addBeheading(PrepareAnvilEvent e){
        if(e.getInventory().getFirstItem() == null) return;
        ItemStack result = e.getInventory().getFirstItem().clone();
        if(!ItemUtils.isWeapon(result, true)) return;
        ItemMeta rm = result.getItemMeta();
        List<Component> lores = rm.lore() == null ? new ArrayList<>() : rm.lore();
        lores.add(Component.text("§r§7Beheading"));
        lores.add(Component.text("§r§7Ajoute 5% de chance de drop une tête de mob"));
        lores.add(Component.text("§r§c"));
        rm.lore(lores);
        result.setItemMeta(rm);
        result.addEnchantment(CustomEnchantments.BEHEADING.getEnchantment(), 1);
        e.setResult(result);
        e.getInventory().setRepairCost(25);
    }
    private void keepCustomEnchantments(PrepareAnvilEvent e){
        List<Component> lores = new ArrayList<>();
        ItemStack result = e.getResult();
        ItemMeta rm = result.getItemMeta();
        List<Component> oldLores = rm.lore() != null ? rm.lore() : new ArrayList<>();
        if(e.getInventory().getFirstItem().getItemMeta().hasEnchant(CustomEnchantments.SOULBOUND.getEnchantment()) || oldLores.contains(Component.text("§r§7Soulbound"))){
            rm.addEnchant(CustomEnchantments.SOULBOUND.getEnchantment(), 1, true);
            lores.add(Component.text("§r§7Soulbound"));
            lores.add(Component.text("§r§7Conserver cet item quand vous mourrez"));
            lores.add(Component.text("§r§e"));
        }
        if(e.getInventory().getFirstItem().getItemMeta().hasEnchant(CustomEnchantments.AUTO_SMELT.getEnchantment()) || oldLores.contains(Component.text("§r§7Autosmelt"))){
            rm.addEnchant(CustomEnchantments.AUTO_SMELT.getEnchantment(), 1, true);
            lores.add(Component.text("§r§7Autosmelt"));
            lores.add(Component.text("§r§7Drop les items cuit des blocs que vous cassez"));
            lores.add(Component.text("§r§f"));
        }
        if(e.getInventory().getFirstItem().getItemMeta().hasEnchant(CustomEnchantments.BEHEADING.getEnchantment()) || oldLores.contains(Component.text("§r§7Beheading"))){
            rm.addEnchant(CustomEnchantments.BEHEADING.getEnchantment(), 1, true);
            lores.add(Component.text("§r§7Beheading"));
            lores.add(Component.text("§r§7Ajoute 5% de chance de drop une tête de mob"));
            lores.add(Component.text("§r§c"));
        }
        rm.lore(lores);
        result.setItemMeta(rm);
        e.setResult(result);
    }
}
