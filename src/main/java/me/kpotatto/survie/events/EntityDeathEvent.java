package me.kpotatto.survie.events;

import me.kpotatto.survie.Survie;
import me.kpotatto.survie.enchantments.CustomEnchantments;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

public class EntityDeathEvent implements Listener {

    @EventHandler
    public void onEntityDeath(org.bukkit.event.entity.EntityDeathEvent e){
        if(e.getEntity().getKiller() != null){
            Player p = e.getEntity().getKiller();

            // EXPÉRIENCE
            int exp = 0;
            switch (e.getEntityType()){
                case ENDER_DRAGON:
                    exp = 5000;
                    break;
                case WITHER:
                    exp = 2000;
                    break;
                case ELDER_GUARDIAN:
                    exp = 1250;
                    break;
                case ENDERMAN:
                case RAVAGER:
                case PILLAGER:
                case VEX:
                case EVOKER:
                case VINDICATOR:
                case ILLUSIONER:
                case EVOKER_FANGS:
                    exp = 1;
                    break;
                case MINECART:
                case ARMOR_STAND:
                case TNT_MINECART:
                case CHEST_MINECART:
                case FURNACE_MINECART:
                case HOPPER_MINECART:
                    exp = 0;
                default:
                    exp = 10;
            }
            Survie.getInstance().skillsLoader.uuidFightingSkillsHashMap.get(p.getUniqueId()).addExperience(exp);

            //Enchantements Custom
            if(p.getInventory().getItemInMainHand() == null || p.getInventory().getItemInMainHand().getItemMeta() == null) return;
            if(p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.VAMPIRISM.enchantment()))
                p.setHealth(Math.min(p.getHealth() + 2, p.getMaxHealth()));
            if(p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.TELEKINESIS.enchantment())){
                dropItem(e.getEntity().getLocation(), p, e.getDrops());
                e.getDrops().clear();
            }
            if(p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.BEHEADING.enchantment())){
                Random random = new Random();
                int r = random.nextInt(20);
                if(r != 0) return;
                // 5% de chance de drop avec succès
                if(e.getEntity().getType().equals(EntityType.ZOMBIE)){
                    dropItem(e.getEntity().getLocation(), p, new ItemStack(Material.ZOMBIE_HEAD ,1));
                }else if(e.getEntity().getType().equals(EntityType.SKELETON)){
                    dropItem(e.getEntity().getLocation(), p, new ItemStack(Material.SKELETON_SKULL ,1));
                }else if(e.getEntity().getType().equals(EntityType.CREEPER)){
                    dropItem(e.getEntity().getLocation(), p, new ItemStack(Material.CREEPER_HEAD ,1));
                }else if(e.getEntity().getType().equals(EntityType.WITHER_SKELETON)){
                    dropItem(e.getEntity().getLocation(), p, new ItemStack(Material.WITHER_SKELETON_SKULL ,1));
                }
            }
        }
    }

    private void dropItem(Location location, Player p, ItemStack is){
        if(p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.TELEKINESIS.enchantment())){
            HashMap<Integer, ItemStack> remaining = p.getInventory().addItem(is);
            remaining.values().forEach(dis -> location.getWorld().dropItemNaturally(location, dis));
            if(!remaining.isEmpty() && !Survie.getInstance().disabledActionBar.contains(p.getUniqueId())) p.sendActionBar("§cInventaire plein! §cCertains de vos items ont été drops");
        }else{
            location.getWorld().dropItemNaturally(location, is);
        }
    }
    private void dropItem(Location location, Player p, Collection<ItemStack> iss){
        if(p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.TELEKINESIS.enchantment())){
            ArrayList<ItemStack> toDrop = new ArrayList<>();
            for(ItemStack is: iss){
                HashMap<Integer, ItemStack> remaining = p.getInventory().addItem(is);
                toDrop.addAll(remaining.values());
            }
            if(!toDrop.isEmpty()) toDrop.forEach(is -> location.getWorld().dropItemNaturally(location, is));
            if(!toDrop.isEmpty() && !Survie.getInstance().disabledActionBar.contains(p.getUniqueId())) p.sendActionBar("§cInventaire plein! §cCertains de vos items ont été drops");
        }else{
            iss.forEach(is -> location.getWorld().dropItemNaturally(location, is));
        }
    }

}
