package me.kpotatto.survie.events;

import me.kpotatto.survie.enchantments.CustomEnchantments;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class EntityDeathEvent implements Listener {

    @EventHandler
    public void onEntityDeath(org.bukkit.event.entity.EntityDeathEvent e){
        if(e.getEntity().getKiller() != null){
            Player p = e.getEntity().getKiller();
            if(p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.BEHEADING.getEnchantment())){
                Random random = new Random();
                int r = random.nextInt(20);
                p.sendMessage("Nombre aléatoire: " + r);
                if(r != 0) return;
                // 5% de chance de drop avec succès
                if(e.getEntity().getType().equals(EntityType.ZOMBIE)){
                    e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), new ItemStack(Material.ZOMBIE_HEAD ,1));
                }else if(e.getEntity().getType().equals(EntityType.SKELETON)){
                    e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), new ItemStack(Material.SKELETON_SKULL ,1));
                }else if(e.getEntity().getType().equals(EntityType.CREEPER)){
                    e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), new ItemStack(Material.CREEPER_HEAD ,1));
                }else if(e.getEntity().getType().equals(EntityType.WITHER_SKELETON)){
                    e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), new ItemStack(Material.WITHER_SKELETON_SKULL ,1));
                }
            }
        }
    }

}
